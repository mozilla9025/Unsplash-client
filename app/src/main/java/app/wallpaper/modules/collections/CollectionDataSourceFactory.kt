package app.wallpaper.modules.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.usecase.GetCollectionsUseCase
import app.wallpaper.modules.collections.CollectionDataSource
import io.reactivex.disposables.CompositeDisposable

class CollectionDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val getCollectionsUseCase: GetCollectionsUseCase
) : DataSource.Factory<Int, PhotoCollection>() {

    val dataSourceLiveData = MutableLiveData<CollectionDataSource>()

    override fun create(): DataSource<Int, PhotoCollection> {
        val photosDataSource = CollectionDataSource(compositeDisposable, getCollectionsUseCase)
        dataSourceLiveData.postValue(photosDataSource)
        return photosDataSource
    }
}
