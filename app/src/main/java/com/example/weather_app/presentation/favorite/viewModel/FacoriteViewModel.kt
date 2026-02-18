package com.example.weather_app.presentation.favorite.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.favorite.model.LocationModel
import com.example.weather_app.presentation.home.viewModel.HomeViewModel
import com.example.weather_app.presentation.setting.viewModel.SettingViewModel
import kotlinx.coroutines.launch

class FavoriteViewModel(val context: Context) :ViewModel(){
    private val weatherRepo: WeatherRepo = WeatherRepo(context)


    fun saveLocation(locationModel: LocationModel){
       viewModelScope.launch {
           try{
               weatherRepo.saveLocation(locationModel)
               Log.i("Saveee", "saveLocation: ")

           }catch (e:Exception){

           }
       }
    }


}
class FavoriteViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(context) as T

    }
}