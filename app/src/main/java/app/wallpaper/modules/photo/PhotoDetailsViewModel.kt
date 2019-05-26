package app.wallpaper.modules.photo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.usecase.GetPhotosUseCase
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.responses.PhotoListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PhotoDetailsViewModel @Inject constructor(
        application: Application,
        private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel(application) {

    var photo: Photo? = null

    val relatedPhotoLiveData: MutableLiveData<PhotoListResponse> by lazy {
        MutableLiveData<PhotoListResponse>()
    }

    fun getRelatedPhotos() {
        add(getPhotosUseCase.getRelatedPhotos(photo?.id!!)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { relatedPhotoLiveData.value = PhotoListResponse.loading() }
                .subscribe({ relatedPhotoLiveData.value = PhotoListResponse.success(it) },
                        { relatedPhotoLiveData.value = PhotoListResponse.failure(it) }))
    }
}