package co.ravneberg.swetak.map

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import co.ravneberg.swetak.R
import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.DeviceLocationProvider
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationObserver
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationService
import com.mapbox.common.location.LocationServiceFactory
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


@Composable
fun MapBoxView() {

}


@Composable
fun MapBoxMapW(
	modifier: Modifier = Modifier,
	point: Point?,
) {
	
	val context = LocalContext.current
	
	val marker = remember(context) {
		context.getDrawable(R.drawable.my_marker)!!.toBitmap()
	}
	var pointAnnotationManager: PointAnnotationManager? by remember {
		mutableStateOf(null)
	}
	AndroidView(
		factory = {
			MapView(it).also { mapView ->
				//mapView.mapboxMap.styleURI(Style.TRAFFIC_DAY)
				val annotationApi = mapView.annotations
				pointAnnotationManager = annotationApi.createPointAnnotationManager()
				
				
			}
		},
		update = { mapView ->
			if (point != null) {
				pointAnnotationManager?.let {
					it.deleteAll()
					val pointAnnotationOptions = PointAnnotationOptions()
						.withPoint(point)
						.withIconImage(marker)
					
					it.create(pointAnnotationOptions)
					mapView.getMapboxMap()
						.flyTo(CameraOptions.Builder().zoom(16.0).center(point).build())
				}
			}
			NoOpUpdate
		},
		modifier = modifier
	)
}
@Composable
fun MapScreen() {
	val TAG = "co.ravneberg.swetac"
	var point: Point? by remember {
		mutableStateOf(null)
	}
	var relaunch by remember {
		mutableStateOf(false)
	}
	val context = LocalContext.current
	
	val permissionRequest = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions(),
		onResult = { permissions ->
			if (!permissions.values.all { it }) {
				//handle permission denied
			}
			else {
				relaunch = !relaunch
			}
		}
	)
	
	
	MapBoxMapW(
		point = point,
		modifier = Modifier
			.fillMaxSize()
	)
	
	LaunchedEffect(key1 = relaunch) {
		
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
			//locationProvider.getLastLocation
		} else {
			Log.e( TAG,"Failed to get device location provider")
		}
		
	}
}