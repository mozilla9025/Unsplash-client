package app.wallpaper.modules.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.usecase.GetPhotosUseCase
import app.wallpaper.domain.usecase.GetUnsplashCollectionsUseCase
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.Refreshable
import app.wallpaper.network.responses.CollectionResponse
import app.wallpaper.network.responses.PagingResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        application: Application,
        getPhotosUseCase: GetPhotosUseCase,
        private val getUnsplashCollectionsUseCase: GetUnsplashCollectionsUseCase
) : BaseViewModel(application), Refreshable {

    internal var data: LiveData<PagedList<Photo>>
    internal val collectionsLiveData: MutableLiveData<CollectionResponse> by lazy {
        MutableLiveData<CollectionResponse>()
    }

    private var dataSourceFactory: PhotoDataSourceFactory

    init {

        val config = PagedList.Config.Builder().apply {
            setPageSize(10)
            setPrefetchDistance(10)
            setInitialLoadSizeHint(10)
            setEnablePlaceholders(false)
        }.build()

        dataSourceFactory = PhotoDataSourceFactory(disposables, getPhotosUseCase)
        data = LivePagedListBuilder<Int, Photo>(dataSourceFactory, config).build()
    }

    override fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

    override fun refresh() {
        dataSourceFactory.dataSourceLiveData.value?.refresh()
    }

    internal fun getInitialLoadState(): LiveData<PagingResponse> =
            Transformations.switchMap<PhotoDataSource, PagingResponse>(dataSourceFactory.dataSourceLiveData, PhotoDataSource::initialLoad)

    internal fun getRangeLoadState(): LiveData<PagingResponse> =
            Transformations.switchMap<PhotoDataSource, PagingResponse>(dataSourceFactory.dataSourceLiveData, PhotoDataSource::rangeLoad)

    internal fun getUnsplashCollections() {
        add(getUnsplashCollectionsUseCase.getCollections(1, Int.MAX_VALUE)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { collectionsLiveData.value = CollectionResponse.loading() }
                .subscribe({ response -> collectionsLiveData.value = CollectionResponse.success(response) },
                        { error -> collectionsLiveData.value = CollectionResponse.failure(error) }))
    }
}