package app.wallpaper.modules.collections

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.usecase.GetCollectionsUseCase
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.modules.home.CollectionDataSourceFactory
import app.wallpaper.network.Refreshable
import app.wallpaper.network.responses.PagingResponse
import javax.inject.Inject

class CollectionsViewModel @Inject constructor(
        application: Application,
        getCollectionsUseCase: GetCollectionsUseCase
) : BaseViewModel(application), Refreshable {

    internal var data: LiveData<PagedList<PhotoCollection>>
    private var dataSourceFactory: CollectionDataSourceFactory

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()

        dataSourceFactory = CollectionDataSourceFactory(disposables, getCollectionsUseCase)
        data = LivePagedListBuilder<Int, PhotoCollection>(dataSourceFactory, config).build()
    }

    internal fun getInitialLoadState(): LiveData<PagingResponse> =
            Transformations.switchMap<CollectionDataSource, PagingResponse>(dataSourceFactory.dataSourceLiveData, CollectionDataSource::initialLoad)

    internal fun getRangeLoadState(): LiveData<PagingResponse> =
            Transformations.switchMap<CollectionDataSource, PagingResponse>(dataSourceFactory.dataSourceLiveData, CollectionDataSource::rangeLoad)

    override fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

    override fun refresh() {
        dataSourceFactory.dataSourceLiveData.value?.refresh()
    }
}