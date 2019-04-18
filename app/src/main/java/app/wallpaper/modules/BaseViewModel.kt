package app.wallpaper.modules

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}