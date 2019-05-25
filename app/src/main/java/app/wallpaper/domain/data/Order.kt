package app.wallpaper.domain.data

enum class Order(val value: String) {
    LATEST("latest"),
    OLDEST("oldest"),
    POPULAR("popular")
}