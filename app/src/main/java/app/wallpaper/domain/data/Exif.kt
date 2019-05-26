package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exif(
        @SerializedName("make") val make: String,
        @SerializedName("model") val model: String,
        @SerializedName("exposure_time") val exposureTime: String,
        @SerializedName("aperture") val aperture: String,
        @SerializedName("focal_length") val focalLength: String,
        @SerializedName("iso") val iso: Int
) : Parcelable