package app.wallpaper.network

interface Refreshable : Retryable {
    fun refresh()
}