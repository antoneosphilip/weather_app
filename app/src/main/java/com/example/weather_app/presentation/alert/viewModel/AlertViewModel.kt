import android.content.Context
import android.util.Log

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weather_app.AlertWorker
import com.example.weather_app.NoInternetAlertWorker

import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.helper.NetworkMonitor
import com.example.weather_app.presentation.alert.viewModel.AlertUiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.max

class AlertsViewModel(val context: Context,private val weatherRepo:WeatherRepo,private val networkMonitor: NetworkMonitor) : ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _startTime = MutableStateFlow("")
    val startTime = _startTime.asStateFlow()

    private val _endTime = MutableStateFlow("")
    val endTime = _endTime.asStateFlow()

    private val _selectedType = MutableStateFlow("Alarm")
    val selectedType = _selectedType.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive = _isActive.asStateFlow()

    var alertStates = mutableStateOf<AlertUiState>(AlertUiState.Loading)
        private set
    val isConnected = networkMonitor.isConnected


    init {
        getAlerts()
    }
    fun showDialog() {
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    fun updateStartTime(time: String) {
        _startTime.value = time
    }

    fun updateEndTime(time: String) {
        _endTime.value = time
    }

    fun updateType(type: String) {
        _selectedType.value = type
    }

    fun toggleAlert(alert: AlertModel) {
        if (alert.isActive) {
            cancelWork(alert.workId)
            viewModelScope.launch {
                weatherRepo.saveAlert(alert.copy(isActive = !alert.isActive))
            }
        }
        else{
            val workId=  scheduleAlert(startTimeMillis = alert.startTime, endTimeMillis = alert.endTime?:0L,
                _selectedType.value=="Notification",alert.id.toLong()
            )
            viewModelScope.launch {
                weatherRepo.saveAlert(
                    alert.copy(
                        isActive = true,
                        workId = workId.toString()
                    )
                )
            }
        }

    }

    fun deleteAlert(alertId: Int,workId:String){
        cancelWork(workId)

        viewModelScope.launch {
            weatherRepo.deleteAlert(alertId)
        }
    }

    fun saveAlert() {
        if (_startTime.value.isBlank() || _endTime.value.isBlank()) return

        val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val parsedStart = format.parse(normalizeTimeString(_startTime.value)) ?: return
        val parsedEnd = format.parse(normalizeTimeString(_endTime.value)) ?: return

        val startCalParsed = Calendar.getInstance().apply { time = parsedStart }
        val endCalParsed = Calendar.getInstance().apply { time = parsedEnd }

        val now = Calendar.getInstance()

        val startCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, startCalParsed.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, startCalParsed.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < now.timeInMillis) add(Calendar.DAY_OF_YEAR, 1)
        }

        val endCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, endCalParsed.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, endCalParsed.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            while (timeInMillis <= startCal.timeInMillis) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val startMillis = startCal.timeInMillis
        val endMillis = endCal.timeInMillis

        Log.i("Alerts", "saveAlert start: $startMillis")
        Log.i("Alerts", "saveAlert end: $endMillis")
        Log.i("Alerts", "now: ${System.currentTimeMillis()}")
        Log.i("Alerts", "delay: ${startMillis - System.currentTimeMillis()}")

        viewModelScope.launch {
            val alertId = weatherRepo.saveAlert(
                AlertModel(
                    startTime = startMillis,
                    endTime = endMillis,
                    type = _selectedType.value,
                    label = "weather alarm",
                    isActive = true,
                    workId = ""
                )
            )

            val workId = scheduleAlert(
                startMillis,
                endMillis,
                _selectedType.value == "Notification",
                alertId = alertId
            )

            weatherRepo.saveAlert(
                AlertModel(
                    id = alertId.toInt(),
                    startTime = startMillis,
                    endTime = endMillis,
                    type = _selectedType.value,
                    label = "weather alarm",
                    isActive = true,
                    workId = workId.toString()
                )
            )
        }
        _showDialog.value = false
        _startTime.value = ""
        _endTime.value = ""
    }

    private fun normalizeTimeString(time: String): String {
        return time
            .replace('٠', '0').replace('١', '1').replace('٢', '2')
            .replace('٣', '3').replace('٤', '4').replace('٥', '5')
            .replace('٦', '6').replace('٧', '7').replace('٨', '8')
            .replace('٩', '9')
            .replace("ص", "AM")
            .replace("م", "PM")
    }

    private fun scheduleAlert(startTimeMillis: Long, endTimeMillis: Long, isNotification: Boolean, alertId: Long): UUID {
        val delay = max(0, startTimeMillis - System.currentTimeMillis())

        val inputData = androidx.work.Data.Builder()
            .putLong("START_TIME", startTimeMillis)
            .putLong("END_TIME", endTimeMillis)
            .putBoolean("IS_NOTIFICATION", isNotification)
            .putLong("ALERT_ID", alertId)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<AlertWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        val noInternetCheckRequest = OneTimeWorkRequestBuilder<NoInternetAlertWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(request)
        WorkManager.getInstance(context).enqueue(noInternetCheckRequest)
        return request.id
    }

    private fun cancelWork(workId: String) {
        if (workId.isNotBlank()) {
            WorkManager.getInstance(context).cancelWorkById(UUID.fromString(workId))
        }
    }
    fun getAlerts(){
        alertStates.value=AlertUiState.Loading
        viewModelScope.launch {
            try {
                weatherRepo.getAlert().collect {
                    alertStates.value = AlertUiState.Success(it)
                }
            }catch (e:Exception){
                alertStates.value=AlertUiState.Error(e.message.toString())
            }
        }
    }

}
class AlertViewModelFactory(
    private val context: Context,
    private val weatherRepo: WeatherRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(context,weatherRepo,networkMonitor) as T

    }
}