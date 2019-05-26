package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
        @SerializedName("city") val city: String,
        @SerializedName("country") val country: String,
        @SerializedName("position") val position: Position
) : Parcelable
