package co.ravneberg.swetak.map

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.ravneberg.swetak.data.colleagueProfile
import co.ravneberg.swetak.data.meProfile
import co.ravneberg.swetak.profile.ProfileScreenState

class MapViewModel : ViewModel() {
	private var userId: String = ""
	private val _userData = MutableLiveData<ProfileScreenState>()
	val userData: LiveData<ProfileScreenState> = _userData
	
	fun setUserId(newUserId: String?) {
		if (newUserId != userId) {
			userId = newUserId ?: meProfile.userId
		}
		// Workaround for simplicity
		_userData.value = if (userId == meProfile.userId || userId == meProfile.displayName) {
			meProfile
		} else {
			colleagueProfile
		}
	}
}
@Immutable
data class  MapScreenState(
	val userId: String,
	@DrawableRes val photo: Int?,
	val location: String
){
	fun isMe() = userId == meProfile.userId
}