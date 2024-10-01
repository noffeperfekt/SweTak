package co.ravneberg.swetak.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import co.ravneberg.swetak.FunctionalityNotAvailablePopup
import co.ravneberg.swetak.MainViewModel
import co.ravneberg.swetak.R
import co.ravneberg.swetak.components.SweTakAppBar
import co.ravneberg.swetak.theme.SweTakTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
	// TODO: Rename and change types of parameters
	private var param1: String? = null
	private var param2: String? = null
	private val viewModel: MapViewModel by viewModels()
	private val activityViewModel: MainViewModel by activityViewModels()
	
	override fun onAttach(context: Context) {
		super.onAttach(context)
		// Consider using safe args plugin
		val userId = arguments?.getString("userId")
		viewModel.setUserId(userId)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			param1 = it.getString(ARG_PARAM1)
			param2 = it.getString(ARG_PARAM2)
		}
	}
	
	@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)
		
		rootView.findViewById<ComposeView>(R.id.toolbar_compose_view).apply {
			setContent {
				var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
				if (functionalityNotAvailablePopupShown) {
					FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
				}
				
				SweTakTheme {
					SweTakAppBar(
						// Reset the minimum bounds that are passed to the root of a compose tree
						modifier = Modifier.wrapContentSize(),
						onNavIconPressed = { activityViewModel.openDrawer() },
						title = { },
						actions = {
							// More icon
							Icon(
								imageVector = Icons.Outlined.MoreVert,
								tint = MaterialTheme.colorScheme.onSurfaceVariant,
								modifier = Modifier
									.clickable(onClick = {
										functionalityNotAvailablePopupShown = true
									})
									.padding(horizontal = 12.dp, vertical = 16.dp)
									.height(24.dp),
								contentDescription = stringResource(id = R.string.more_options)
							)
						}
						
					)
				}
				
			}
		}
		
		
		rootView.findViewById<ComposeView>(R.id.map_compose_view).apply {
			setContent {
				
				SweTakTheme{
					mapLocationInitialize()
					val mapViewportState = rememberMapViewportState{
						setCameraOptions {
							center(Point.fromLngLat(-74.0066, 40.7135))
							pitch(45.0)
							zoom(15.5)
							bearing(-17.6)
						}
					}
					MapboxMap(
						Modifier.fillMaxSize(),
						mapViewportState = mapViewportState,
					) {
						
						MapEffect(Unit) { mapView ->
							mapView.location.updateSettings {
								locationPuck = createDefault2DPuck(withBearing = true)
								enabled = true
								puckBearing = PuckBearing.COURSE
								puckBearingEnabled = false
							}
							mapViewportState.transitionToFollowPuckState()
							//val mapLatLng: LatLng = mapView.mapboxMap.
						}
					}
					
					//MapScreen()
					/*MapBoxMap(
						point = Point.fromLngLat(-0.6333, 35.6971),
						modifier = Modifier
							.fillMaxSize()
					)*/
				}
			}
		}
		
		return rootView
	}
	
	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment MapFragment.
		 */
		// TODO: Rename and change types and number of parameters
		@JvmStatic
		fun newInstance(param1: String, param2: String) =
			MapFragment().apply {
				arguments = Bundle().apply {
					putString(ARG_PARAM1, param1)
					putString(ARG_PARAM2, param2)
				}
			}
	}
}