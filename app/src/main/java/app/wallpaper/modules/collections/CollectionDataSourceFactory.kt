package app.wallpaper.modules.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import app.wallpaper.domain.data.Collection
import io.reactivex.disposables.CompositeDisposable

class CollectionDataSourceFactory(private val compositeDisposable: CompositeDisposable?) : DataSource.Factory<Int, Collection>() {

    val dataSourceLiveData = MutableLiveData<CollectionDataSource>()

    override fun create(): DataSource<Int, Collection> {
        val photosDataSource = CollectionDataSource(compositeDisposable)
        dataSourceLiveData.postValue(photosDataSource)
        return photosDataSource
    }
}
