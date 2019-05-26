package app.wallpaper.domain.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        @SerializedName("id") val id: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("username") val userName: String,
        @SerializedName("name") val name: String,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("instagram_username") val instagramName: String,
        @SerializedName("twitter_username") val twitterName: String,
        @SerializedName("portfolio_url") val portfolioUrl: String,
        @SerializedName("bio") val bio: String,
        @SerializedName("location") val location: String,
        @SerializedName("total_likes") val likesCount: Int,
        @SerializedName("total_photos") val photosCount: Int,
        @SerializedName("total_collections") val collectionsCount: Int,
        @SerializedName("followed_by_user") val followed: Boolean,
        @SerializedName("followers_count") val followersCount: Int,
        @SerializedName("following_count") val followingCount: Int,
        @SerializedName("profile_image") val avatar: ProfileImage,
        @SerializedName("badge") val badge: ProfileBadge,
        @SerializedName("links") val links: ProfileLinks
) : Parcelable