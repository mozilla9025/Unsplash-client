package app.wallpaper.modules.collections

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.wallpaper.data.Collection
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.modules.home.CollectionDataSource
import app.wallpaper.modules.home.CollectionDataSourceFactory
import app.wallpaper.network.Refreshable
import app.wallpaper.network.responses.PagingResponse

class CollectionsViewModel(application: Application) : BaseViewModel(application), Refreshable {

    internal var data: LiveData<PagedList<Collection>>
    private var dataSourceFactory: CollectionDataSourceFactory

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()

        dataSourceFactory = CollectionDataSourceFactory(disposables)
        data = LivePagedListBuilder<Int, Collection>(dataSourceFactory, config).build()
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