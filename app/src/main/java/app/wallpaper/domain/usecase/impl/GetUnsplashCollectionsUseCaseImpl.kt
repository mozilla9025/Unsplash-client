package app.wallpaper.domain.usecase.impl

import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.repo.UserRepository
import app.wallpaper.domain.usecase.GetUnsplashCollectionsUseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetUnsplashCollectionsUseCaseImpl @Inject constructor(
        private val userRepository: UserRepository
) : GetUnsplashCollectionsUseCase {

    override fun getCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>> {
        return userRepository.getUnsplashCollections(page, perPage)
    }
}