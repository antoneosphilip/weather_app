package com.example.weather_app.presentation.favorite.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.constant.Constants
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.helper.NetworkObserver
import com.example.weather_app.presentation.home.viewModel.HomeUiState
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(val context: Context,private val weatherRepo: WeatherRepo,private val networkMonitor: NetworkObserver) :ViewModel(){
     var favoriteStates= mutableStateOf<FavoriteUiState>(FavoriteUiState.Loading)
        private set

    val isConnected = networkMonitor.isConnected

    init {
        getLocation()
    }
        fun saveLocation(locationModel: LocationModel){
           viewModelScope.launch {
               try{
                   weatherRepo.saveLocation(locationModel)
                   favoriteStates.value=FavoriteUiState.SaveSuccess(locationModel)
                   Log.i("Saveee", "saveLocation: ")
               }catch (e:Exception){
                   favoriteStates.value=FavoriteUiState.Error(e.message.toString())
               }
           }
        }
    fun deleteLocation(locationId:Int){
        viewModelScope.launch {
            try{
                weatherRepo.deleteLocation(locationId)
                Log.i("Saveee", "saveLocation: ")
                favoriteStates.value=FavoriteUiState.DeleteSuccess
            }catch (e:Exception){
                favoriteStates.value=FavoriteUiState.Error(e.message.toString())
            }
        }
    }
    fun getLocation(){
        favoriteStates.value=FavoriteUiState.Loading
        viewModelScope.launch {
            try{
                weatherRepo.getLocation().collect{
                    it->
                    favoriteStates.value=FavoriteUiState.Success(it)
                    Log.i("Saveee", "saveLocation: "+it)

                }

            }catch (e:Exception){
                favoriteStates.value=FavoriteUiState.Error(e.message.toString())

            }
        }
    }

}
class FavoriteViewModelFactory(
    private val context: Context,
    private val weatherRepo: WeatherRepo,
    private val networkMonitor: NetworkObserver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(context,weatherRepo,networkMonitor) as T

    }
}