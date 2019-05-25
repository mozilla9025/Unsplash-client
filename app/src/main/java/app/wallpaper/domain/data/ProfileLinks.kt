package app.wallpaper.domain.data

import com.google.gson.annotations.SerializedName

data class ProfileLinks(
    @SerializedName("self") val self: String,
    @SerializedName("html") val html: String,
    @SerializedName("photos") val photos: String,
    @SerializedName("likes") val likes: String,
    @SerializedName("portfolio") val portfolio: String
) {
}