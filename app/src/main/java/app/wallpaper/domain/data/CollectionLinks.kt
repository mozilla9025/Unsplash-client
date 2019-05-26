package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionLinks(
        @SerializedName("self") val self: String,
        @SerializedName("html") val html: String,
        @SerializedName("photos") val photos: String,
        @SerializedName("related") val related: String
) : Parcelable