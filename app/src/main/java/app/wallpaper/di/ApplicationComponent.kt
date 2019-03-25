package app.wallpaper.di

import dagger.Component

@Component(modules = arrayOf(NetworkModule::class))
interface ApplicationComponent {

}
