package app.wallpaper.modules.collections

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import app.wallpaper.domain.data.Collection
import app.wallpaper.domain.usecase.GetCollectionsUseCase
import app.wallpaper.network.responses.PagingResponse
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class CollectionDataSource(private val disposable: CompositeDisposable,
                           private val getCollectionUseCase: GetCollectionsUseCase) : PageKeyedDataSource<Int, Collection>() {

    private var retryCompletable: Completable? = null

    val rangeLoad: MutableLiveData<PagingResponse> by lazy {
        MutableLiveData<PagingResponse>()
    }
    val initialLoad: MutableLiveData<PagingResponse> by lazy {
        MutableLiveData<PagingResponse>()
    }

    fun retry() {
        if (retryCompletable != null) {
            disposable.add(retryCompletable!!
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

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Collection>) {
        val currPage = 1
        disposable.add(getCollectionUseCase.getCollections(currPage, params.requestedLoadSize)
                .doOnSubscribe { initialLoad.postValue(PagingResponse.loading()) }
                .subscribe({ response ->
                    callback.onResult(response, null, currPage + 1)
                    initialLoad.postValue(PagingResponse.success())
                }, { error ->
                    initialLoad.postValue(PagingResponse.failure(error))
                    setRetry(Action { loadInitial(params, callback) })
                }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Collection>) {
        disposable.add(getCollectionUseCase.getCollections(params.key, params.requestedLoadSize)
                .doOnSubscribe { rangeLoad.postValue(PagingResponse.loading()) }
                .subscribe({ response ->
                    rangeLoad.postValue(PagingResponse.success())
                    callback.onResult(response, params.key + 1)
                }, { error ->
                    rangeLoad.postValue(PagingResponse.failure(error))
                    setRetry(Action { loadAfter(params, callback) })
                }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Collection>) {
    }
}