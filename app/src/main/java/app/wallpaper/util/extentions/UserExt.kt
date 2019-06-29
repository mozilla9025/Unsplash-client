package app.wallpaper.util.extentions

import app.wallpaper.domain.data.ProfileLinks

fun ProfileLinks.getLinkName() = this.html.substringAfterLast("/")
