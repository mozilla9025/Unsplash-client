package app.wallpaper.modules.home.collections

import android.app.Application
import androidx.lifecycle.MutableLiveData
import app.wallpaper.app.ApplicationLoader
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.controllers.CollectionApiController
import app.wallpaper.network.responses.CollectionResponse
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CollectionsViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var collectionApiController: CollectionApiController

    init {
        ApplicationLoader.applicationComponent.inject(this)
    }

    val photosLiveData: MutableLiveData<CollectionResponse> by lazy {
        MutableLiveData<CollectionResponse>()
    }

    fun getCollections() {
        disposables.add(collectionApiController.getCollections(0, 30)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { photosLiveData.value = CollectionResponse.instance.loading() }
                .subscribe({ result -> photosLiveData.value = CollectionResponse.instance.success(result) },
                        { error ->
                            photosLiveData.value = CollectionResponse.instance.failure(error)
                        }
                ))
    }
}