package app.wallpaper.data

import com.google.gson.annotations.SerializedName

data class ProfileBadge(
    @SerializedName("title")
    val title: String,
    @SerializedName("primary")
    val isPrimary: Boolean,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("link")
    val link: String
) {
}