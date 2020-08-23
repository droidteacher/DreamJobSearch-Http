package hu.prooktatas.djs.model

import android.location.Location
import java.io.Serializable

data class UserPreferences(val name: String, val position: String, val workPrefs: Int, val locationPrefs: List<Location> = emptyList()): Serializable