package app.wallpaper.data

import com.google.gson.annotations.SerializedName

data class Position(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longtitude: Double
) {
}