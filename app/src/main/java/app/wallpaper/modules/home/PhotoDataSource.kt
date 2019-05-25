package app.wallpaper.modules.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import app.wallpaper.domain.data.Order
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.usecase.GetPhotosUseCase
import app.wallpaper.network.responses.PagingResponse
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class PhotoDataSource(private val disposable: CompositeDisposable,
                      private val getPhotoUseCase: GetPhotosUseCase) : PageKeyedDataSource<Int, Photo>() {

    private var retryCompletable: Completable? = null

    val rangeLoad: MutableLiveData<PagingResponse> by lazy {
        MutableLiveData<PagingResponse>()
    }
    val initialLoad: MutableLiveData<PagingResponse> by lazy {
        MutableLiveData<PagingResponse>()
    }

    fun retry() {
        if (retryCompletable == null) return

        disposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { })
    }

    fun refresh() {
        invalidate()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        val currPage = 1
        disposable.add(getPhotoUseCase.getPhotos(currPage, params.requestedLoadSize, Order.LATEST)
                .doOnSubscribe { initialLoad.postValue(PagingResponse.loading()) }
                .subscribe({ response ->
                    initialLoad.postValue(PagingResponse.success())
                    callback.onResult(response, null, currPage + 1)
                }, { error ->
                    initialLoad.postValue(PagingResponse.failure(error))
                    setRetry(Action { loadInitial(params, callback) })
                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        disposable.add(getPhotoUseCase.getPhotos(params.key, params.requestedLoadSize, Order.LATEST)
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

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }
}