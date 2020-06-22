package vsukharev.anytypeadapter.sample.albums.data

import vsukharev.anytypeadapter.sample.albums.di.AlbumsScope
import vsukharev.anytypeadapter.sample.albums.domain.model.Album
import java.util.*
import javax.inject.Inject

@AlbumsScope
class AlbumsRepository @Inject constructor() {
    private val albums = listOf(
        Album(
            UUID.randomUUID().toString(),
            name = "Havana",
            performer = "Camila Cabello",
            issueYear = 2017,
            coverUrl = "https://e-cdns-images.dzcdn.net/images/cover/70e7e8f63a38df718f7e713d0572969a/500x500.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "What separates me from you",
            performer = "A Day to Remember",
            issueYear = 2010,
            coverUrl = "https://cdns-images.dzcdn.net/images/cover/4397856d2d56776b342187fa4f09f864/500x500.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "ISSA",
            performer = "21 Savage",
            issueYear = 2017,
            coverUrl = "https://i.pinimg.com/originals/a0/1e/75/a01e75fc6283593b75e1d694d0280107.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "ANTI",
            performer = "Rihanna",
            issueYear = 2016,
            coverUrl = "https://i.pinimg.com/474x/24/cd/46/24cd466e5d9a17063f06a5e6b9026cd0--album-cover-rihanna.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Culture II",
            performer = "Migos",
            issueYear = 2018,
            coverUrl = "https://static.billboard.com/files/media/migos-culture-2-album-art-2018-billboard-1240-compressed.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Revolution",
            performer = "Armin van Buuren",
            issueYear = 2019,
            coverUrl = "https://img.discogs.com/XDBWLbfh6HqbhSUgNEqD3GKGbdY=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-13733099-1559987430-5122.jpeg.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "The Mouse and the Mask",
            performer = "Danger doom",
            issueYear = 2005,
            coverUrl = "https://cloud.netlifyusercontent.com/assets/344dbf88-fdf9-42bb-adb4-46f01eedd629/6b2ef062-6913-4a94-a265-7d75f4f91854/64.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "The Division Bell",
            performer = "Pink floyd",
            issueYear = 1994,
            coverUrl = "https://cloud.netlifyusercontent.com/assets/344dbf88-fdf9-42bb-adb4-46f01eedd629/68c5957a-9e3f-458d-ac48-4fa40c2b7394/27.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Places Like This",
            performer = "Architecture in Helsinki",
            issueYear = 2007,
            coverUrl = "https://cloud.netlifyusercontent.com/assets/344dbf88-fdf9-42bb-adb4-46f01eedd629/9b1ee321-26f1-4100-8c5b-9ac0f6cf4439/aih-places-like-these.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Hail to the Thief",
            performer = "Radiohead",
            issueYear = 2003,
            coverUrl = "https://cloud.netlifyusercontent.com/assets/344dbf88-fdf9-42bb-adb4-46f01eedd629/c348b23b-ab05-492b-9fc7-07144eb27edf/16.jpg"
        )
    )

    suspend fun getAlbumsBasedOnPreferences(): List<Album> {
        return albums.shuffled().take(5)
    }
}