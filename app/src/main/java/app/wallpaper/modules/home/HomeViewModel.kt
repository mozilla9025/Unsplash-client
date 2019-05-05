package app.wallpaper.modules.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.wallpaper.data.Photo
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.Refreshable
import app.wallpaper.network.responses.PagingResponse


class HomeViewModel(application: Application) : BaseViewModel(application), Refreshable {

    var data: LiveData<PagedList<Photo>>
    private var dataSourceFactory: PhotoDataSourceFactory

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()

        dataSourceFactory = PhotoDataSourceFactory(disposables)
        data = LivePagedListBuilder<Int, Photo>(dataSourceFactory, config).build()
    }

    fun getInitialLoadState(): LiveData<PagingResponse> =
            Transformations.switchMap<PhotoDataSource, PagingResponse>(dataSourceFactory.dataSourceLiveData, PhotoDataSource::initialLoad)

    fun getRangeLoadState(): LiveData<PagingResponse> =
            Transformations.switchMap<PhotoDataSource, PagingResponse>(dataSourceFactory.dataSourceLiveData, PhotoDataSource::rangeLoad)

    override fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

    override fun refresh() {
        dataSourceFactory.dataSourceLiveData.value?.refresh()
    }
}