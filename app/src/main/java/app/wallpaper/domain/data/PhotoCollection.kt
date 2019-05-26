package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoCollection(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String,
        @SerializedName("published_at") val publishedAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("curated") val curated: Boolean,
        @SerializedName("total_photos") val totalPhotos: Int,
        @SerializedName("private") val isPrivate: Boolean,
        @SerializedName("share_key") val shareKey: String,
        @SerializedName("cover_photo") val coverPhoto: Photo,
        @SerializedName("user") val user: User,
        @SerializedName("links") val links: CollectionLinks,
        @SerializedName("preview_photos") val previews: List<Photo>
) : Parcelable