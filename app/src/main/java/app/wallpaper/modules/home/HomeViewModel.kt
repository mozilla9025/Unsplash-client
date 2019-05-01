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


    init {
        ApplicationLoader.applicationComponent.inject(this)
    }

}