package app.wallpaper.modules.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import app.wallpaper.app.ApplicationLoader
import app.wallpaper.data.Order
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.controllers.PhotosApiController
import app.wallpaper.network.responses.PhotoResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var photosApiController: PhotosApiController

    val photosLiveData: MutableLiveData<PhotoResponse> by lazy {
        MutableLiveData<PhotoResponse>()
    }

    init {
        ApplicationLoader.applicationComponent.inject(this)
    }

    fun getPhotos() {
        disposables.add(
                photosApiController.getPhotos(0, 30, Order.LATEST)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { photosLiveData.setValue(PhotoResponse.loading()) }
                        .subscribe({ response ->
                            photosLiveData.setValue(PhotoResponse.success(response))
                        },
                                { error -> photosLiveData.setValue(PhotoResponse.failure(error)) })
        )
    }
}