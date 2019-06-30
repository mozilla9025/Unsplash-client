package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        @SerializedName("id") val id: String,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("updated_at") val updatedAt: String?,
        @SerializedName("width") val width: Int?,
        @SerializedName("height") val height: Int?,
        @SerializedName("color") val color: String?,
        @SerializedName("downloads") val downloads: Int,
        @SerializedName("views") val views: Int?,
        @SerializedName("likes") val likes: Int?,
        @SerializedName("liked_by_user") val likedByUser: Boolean?,
        @SerializedName("description") val description: String?,
        @SerializedName("exif") val exif: Exif?,
        @SerializedName("location") val location: Location?,
        @SerializedName("user") val user: User?,
        @SerializedName("urls") val urls: PhotoUrls?
) : Parcelable
