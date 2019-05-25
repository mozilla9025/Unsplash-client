package app.wallpaper.domain.data

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("id") val id: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("color") val color: String,
    @SerializedName("downloads") val downloads: Int,
    @SerializedName("likes") val likes: Int,
    @SerializedName("liked_by_user") val likedByUser: Boolean,
    @SerializedName("description") val description: String,
    @SerializedName("exif") val exif: Exif,
    @SerializedName("location") val location: Location,
    @SerializedName("user") val user: User,
    @SerializedName("urls") val urls: PhotoUrls
    /*
"current_user_collections": [ // The *current user's* collections that this photo belongs to.
{
"id": 206,
"title": "Makers: Cat and Ben",
"published_at": "2016-01-12T18:16:09-05:00",
"updated_at": "2016-07-10T11:00:01-05:00",
"curated": false,
"cover_photo": null,
"user": null
},
// ... more collections
],
"links": {
"self": "https://api.unsplash.com/photos/Dwu85P9SOIk",
"html": "https://unsplash.com/photos/Dwu85P9SOIk",
"download": "https://unsplash.com/photos/Dwu85P9SOIk/download"
"download_location": "https://api.unsplash.com/photos/Dwu85P9SOIk/download"
},
*/
) {
}