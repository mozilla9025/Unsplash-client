package app.wallpaper.modules.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.usecase.GetPhotosUseCase
import io.reactivex.disposables.CompositeDisposable

class PhotoDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                             private val getPhotosUseCase: GetPhotosUseCase) : DataSource.Factory<Int, Photo>() {

    val dataSourceLiveData = MutableLiveData<PhotoDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val photosDataSource = PhotoDataSource(compositeDisposable, getPhotosUseCase)
        dataSourceLiveData.postValue(photosDataSource)
        return photosDataSource
    }
}
