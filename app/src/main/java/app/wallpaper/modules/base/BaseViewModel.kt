package app.wallpaper.modules.base

import androidx.lifecycle.AndroidViewModel
import app.wallpaper.app.ApplicationLoader
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : AndroidViewModel(ApplicationLoader.get()) {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    protected fun add(disposable: Disposable) {
        disposables.add(disposable)
    }
}