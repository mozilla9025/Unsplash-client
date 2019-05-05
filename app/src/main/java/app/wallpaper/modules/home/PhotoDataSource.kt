package app.wallpaper.modules.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import app.wallpaper.app.ApplicationLoader
import app.wallpaper.data.Order
import app.wallpaper.data.Photo
import app.wallpaper.network.controllers.PhotosApiController
import app.wallpaper.network.responses.PagingResponse
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class PhotoDataSource(private val disposable: CompositeDisposable?) : PageKeyedDataSource<Int, Photo>() {

    @Inject
    lateinit var photosApiController: PhotosApiController

    private var retryCompletable: Completable? = null

    val rangeLoad: MutableLiveData<PagingResponse> by lazy {
        MutableLiveData<PagingResponse>()
    }
    val initialLoad: MutableLiveData<PagingResponse> by lazy {
        MutableLiveData<PagingResponse>()
    }

    init {
        ApplicationLoader.applicationComponent.inject(this)
    }

    fun retry() {
        if (retryCompletable != null) {
            disposable?.add(retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { })
        }
    }

    fun refresh() {
        invalidate()
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        val currPage = 1
        disposable?.add(photosApiController.getPhotos(currPage, params.requestedLoadSize, Order.LATEST)
                .doOnSubscribe { initialLoad.postValue(PagingResponse.loading()) }
                .subscribe({ response ->
                    callback.onResult(response, null, currPage + 1)
                    initialLoad.postValue(PagingResponse.success())
                }, { error ->
                    initialLoad.postValue(PagingResponse.failure(error))
                    setRetry(Action { loadInitial(params, callback) })
                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        disposable?.add(photosApiController.getPhotos(params.key, params.requestedLoadSize, Order.LATEST)
                .doOnSubscribe { rangeLoad.postValue(PagingResponse.loading()) }
                .subscribe({ response ->
                    rangeLoad.postValue(PagingResponse.success())
                    callback.onResult(response, params.key + 1)
                }, { error ->
                    rangeLoad.postValue(PagingResponse.failure(error))
                    setRetry(Action { loadAfter(params, callback) })
                }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    }
}