package com.example.weather_app

import AlertViewModelFactory
import LocationProvider
import android.content.Context
import android.net.Network
import com.example.weather_app.data.WeatherRepo
import com.example.weather_app.data.alert.datasouce.AlertDao
import com.example.weather_app.data.alert.datasouce.AlertLocalDataBase
import com.example.weather_app.data.favorite.datasource.FavoriteDao
import com.example.weather_app.data.favorite.datasource.FavoriteLocalDataBase
import com.example.weather_app.data.location.ILocationProvider
import com.example.weather_app.data.setting.datasource.SettingDao
import com.example.weather_app.data.setting.datasource.SettingLocalDataBase
import com.example.weather_app.data.weather.datasource.local.WeatherLocalDataBase
import com.example.weather_app.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weather_app.db.DataBase
import com.example.weather_app.helper.NetworkMonitor
import com.example.weather_app.helper.NetworkObserver
import com.example.weather_app.prefs.PreferenceStorage
import com.example.weather_app.prefs.SharedPreferencesHelper
import com.example.weather_app.presentation.favorite.viewModel.FavoriteViewModelFactory
import com.example.weather_app.presentation.home.viewModel.HomeViewModelFactory
import com.example.weather_app.presentation.setting.viewModel.SettingViewModelFactory

interface AppContainer {

    val alertDao: AlertDao

    val favoriteDao: FavoriteDao

    val alertLocalDataBase: AlertLocalDataBase

    val favoriteLocalDataBase: FavoriteLocalDataBase

    val weatherLocalDataBase: WeatherLocalDataBase

    val weatherRemoteDataSource: WeatherRemoteDataSource

    val weatherRepo: WeatherRepo

    val homeViewModelFactory: HomeViewModelFactory

    val favoriteViewModelFactory: FavoriteViewModelFactory

    val alertViewModelFactory: AlertViewModelFactory

    val settingViewModelFactory: SettingViewModelFactory

    val locationProvider : ILocationProvider

    val prefs: PreferenceStorage

    val networkMonitor: NetworkObserver

    val settingDao: SettingDao

    val settingLocalDataBase: SettingLocalDataBase


}
class AppContainerImpl(
    private val context: Context
) : AppContainer {

    override val alertDao by lazy {
        DataBase.getInstance(context).getAlertDao()
    }
    override val favoriteDao by lazy {
        DataBase.getInstance(context).getFavoriteDao()
    }
    override val settingDao by lazy {
        DataBase.getInstance(context).getSettingDao()
    }


    override val alertLocalDataBase by lazy {
        AlertLocalDataBase(alertDao)
    }

    override val favoriteLocalDataBase by lazy {
        FavoriteLocalDataBase(favoriteDao)
    }


    override val settingLocalDataBase by lazy {
        SettingLocalDataBase(context)
    }

    override val weatherLocalDataBase by lazy {
        WeatherLocalDataBase(context)
    }

    override val weatherRemoteDataSource by lazy {
        WeatherRemoteDataSource()
    }
    override val prefs by lazy { SharedPreferencesHelper.getInstance(context) as PreferenceStorage }

    override val weatherRepo by lazy {
        WeatherRepo(
            weatherLocalData = weatherLocalDataBase,
            favoriteLocalDataBase = favoriteLocalDataBase,
            alertLocalDataBase = alertLocalDataBase,
            weatherRemoteData = weatherRemoteDataSource,
            settingLocalDataBase
        )
    }
    override val homeViewModelFactory by lazy {
        HomeViewModelFactory(weatherRepo, prefs,locationProvider,networkMonitor)
    }

    override val favoriteViewModelFactory by lazy {
        FavoriteViewModelFactory(context, weatherRepo,networkMonitor
        )
    }

    override val alertViewModelFactory by lazy {
        AlertViewModelFactory(context, weatherRepo,networkMonitor)
    }
    override val settingViewModelFactory by lazy {
        SettingViewModelFactory(context, weatherRepo)
    }
    override val locationProvider by lazy {
        LocationProvider(context)
    }
    override val networkMonitor = NetworkMonitor(context)


}