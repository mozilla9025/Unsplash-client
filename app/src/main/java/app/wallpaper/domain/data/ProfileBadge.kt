package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileBadge(
        @SerializedName("title") val title: String,
        @SerializedName("primary") val isPrimary: Boolean,
        @SerializedName("slug") val slug: String,
        @SerializedName("link") val link: String
) : Parcelable