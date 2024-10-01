package co.ravneberg.swetak.map

import android.util.Log
import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.DeviceLocationProvider
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationObserver
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationService
import com.mapbox.common.location.LocationServiceFactory


fun mapLocationInitialize(){
	val locationProvider = mapLocationPermission()
	mapLocationListener(locationProvider!!)
}

fun mapLocationPermission(): DeviceLocationProvider? {
	val locationService : LocationService = LocationServiceFactory.getOrCreate()
	var locationProvider: DeviceLocationProvider? = null
	
	val request = LocationProviderRequest.Builder()
		.interval(IntervalSettings.Builder().interval(0L).minimumInterval(0L).maximumInterval(0L).build())
		.displacement(0F)
		.accuracy(AccuracyLevel.HIGHEST)
		.build();
	
	val result = locationService.getDeviceLocationProvider(request)
	if (result.isValue) {
		locationProvider = result.value!!
	} else {
		Log.e( "co.ravneberg.swetac","Failed to get device location provider")
	}
	return locationProvider
}
fun mapLocationListener(locationProvider: DeviceLocationProvider){
	val locationObserver = object: LocationObserver {
		override fun onLocationUpdateReceived(locations: MutableList<Location>) {
			Log.e("co.ravneberg.swetac", "Location update received: " + locations)
		}
	}
	locationProvider.addLocationObserver(locationObserver)
}