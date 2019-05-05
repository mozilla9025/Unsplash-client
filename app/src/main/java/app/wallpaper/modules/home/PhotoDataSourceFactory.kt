package app.wallpaper.modules.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import app.wallpaper.data.Photo
import io.reactivex.disposables.CompositeDisposable

class PhotoDataSourceFactory(private val compositeDisposable: CompositeDisposable?) : DataSource.Factory<Int, Photo>() {

    val dataSourceLiveData = MutableLiveData<PhotoDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val photosDataSource = PhotoDataSource(compositeDisposable)
        dataSourceLiveData.postValue(photosDataSource)
        return photosDataSource
    }
}
