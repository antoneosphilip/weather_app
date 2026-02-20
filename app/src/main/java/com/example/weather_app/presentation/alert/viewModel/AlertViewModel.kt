import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.alert.model.AlertModel
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(context: Context) : ViewModel() {

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

    private val weatherRepo=WeatherRepo(context)

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

    fun saveAlert() {

        if (_startTime.value.isBlank() || _endTime.value.isBlank()) {
            return
        }

        viewModelScope.launch {
            weatherRepo.saveAlert(
                AlertModel(
                    startTime = _startTime.value.toLong(),
                    endTime = _endTime.value.toLong(),
                    type = _selectedType.value,
                    label = "weather alaram",
                    isActive = _isActive.value
                )
            )
        }

        _showDialog.value = false
        _startTime.value = ""
        _endTime.value = ""
        _selectedType.value = "Alarm"
    }

    fun getAlerts(){

    }

}
class AlertViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(context) as T

    }
}