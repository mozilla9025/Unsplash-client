package app.wallpaper.modules.photo

import androidx.lifecycle.MutableLiveData
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.usecase.GetPhotosUseCase
import app.wallpaper.modules.base.BaseViewModel
import app.wallpaper.network.responses.Response
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class PhotoDetailsViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {

    var photo: Photo? = null

    val photoLiveData: MutableLiveData<Response<Pair<Photo, List<Photo>>>> by lazy {
        MutableLiveData<Response<Pair<Photo, List<Photo>>>>()
    }

    fun updateInfo() {
        photo?.let {
            add(Observable.zip(
                getPhotosUseCase.getPhotoById(it.id).toObservable(),
                getPhotosUseCase.getRelatedPhotos(it.id),
                BiFunction<Photo, List<Photo>, Pair<Photo, List<Photo>>> { photo, relatedPhotos ->
                    return@BiFunction Pair(photo, relatedPhotos)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { photoLiveData.value = Response.loading() }
                .subscribe({ pair ->
                    photoLiveData.value = Response.success(pair)
                    photo = pair.first
                }, { err -> photoLiveData.value = Response.failure(err) })
            )
        }
    }
}