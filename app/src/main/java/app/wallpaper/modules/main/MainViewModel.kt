package app.wallpaper.modules.main

import android.app.Application
import app.wallpaper.app.ApplicationLoader
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.controllers.PhotosApiController
import javax.inject.Inject

class MainViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var photosApiController: PhotosApiController

    init {
        ApplicationLoader.applicationComponent.inject(this)
    }
}