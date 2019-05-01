package app.wallpaper.data

import com.google.gson.annotations.SerializedName

data class CollectionLinks(
        @SerializedName("self") val self: String,
        @SerializedName("html") val html: String,
        @SerializedName("photos") val photos: String,
        @SerializedName("related") val related: String
) {
}