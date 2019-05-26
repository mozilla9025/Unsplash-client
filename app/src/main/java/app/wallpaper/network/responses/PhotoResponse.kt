package app.wallpaper.network.responses

import app.wallpaper.domain.data.Photo

class PhotoResponse(var status: ResponseStatus) {
    var photo: Photo? = null
    var relatedPhotos: List<Photo>? = null
    var error: Throwable? = null


}