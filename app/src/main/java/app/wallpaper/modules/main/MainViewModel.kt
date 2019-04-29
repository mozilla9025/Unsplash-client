package app.wallpaper.modules.main

import android.app.Application
import android.util.Log
import app.wallpaper.app.ApplicationLoader
import app.wallpaper.data.Order
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.controllers.PhotosApiController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var photosApiController: PhotosApiController

    init {
        ApplicationLoader.applicationComponent.inject(this)
    }

    fun getPhotos() {
        disposables.add(
            photosApiController.getPhotos(0, 10, Order.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> Log.i("TAG", response.isSuccessful.toString()) },
                    { error -> Log.e("TAG", "", error) })
        )
    }
}