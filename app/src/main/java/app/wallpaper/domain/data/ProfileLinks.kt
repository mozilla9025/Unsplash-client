package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileLinks(
        @SerializedName("self") val self: String,
        @SerializedName("html") val html: String,
        @SerializedName("photos") val photos: String,
        @SerializedName("likes") val likes: String,
        @SerializedName("portfolio") val portfolio: String
) : Parcelable