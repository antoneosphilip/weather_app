import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.weather_app.data.location.ILocationProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await

class LocationProvider(private val context: Context): ILocationProvider {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

   override fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLocation(): LatLng? {
        if (!hasPermission()) return null
        return try {
            val location = fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            location?.let { LatLng(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }

    override fun checkLocationSettings(
        activity: android.app.Activity,
        onSuccess: () -> Unit,
        onResolvable: (ResolvableApiException) -> Unit,
        onFailed: () -> Unit
    ) {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()
        val settingsRequest = LocationSettingsRequest.Builder().addLocationRequest(request).build()

        LocationServices.getSettingsClient(activity)
            .checkLocationSettings(settingsRequest)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) onResolvable(exception)
                else onFailed()
            }
    }

    override fun registerLocationReceiver(onLocationEnabled: () -> Unit): BroadcastReceiver? {
        return try {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(ctx: Context, intent: Intent) {
                    if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            onLocationEnabled()
                        }
                    }
                }
            }
            context.registerReceiver(receiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
            receiver
        } catch (e: Exception) {
            null
        }
    }
}