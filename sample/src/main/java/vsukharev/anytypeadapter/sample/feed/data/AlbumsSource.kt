package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import java.util.*

/**
 * Object returning list of the albums
 */
object AlbumsSource {

    val albums = listOf(
        Album(
            UUID.randomUUID().toString(), // TODO make default value for id
            name = "Coffee Bags, Abstraction & Empathy",
            performer = "Lee Knuttila",
            coverUrl = "https://i.imgur.com/smdV8sL.png"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Some mountains",
            performer = "Unknown",
            coverUrl = "https://i.imgur.com/88yZF0y.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Still",
            performer = "Russ",
            coverUrl = "https://i.imgur.com/1bvG4Cj.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Paid Off",
            performer = "Russ",
            coverUrl = "https://i.imgur.com/CqG8RBP.png"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Graduation",
            performer = "Kanye West",
            coverUrl = "https://i.imgur.com/NwZ1bMk.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "On Art and Aesthetics",
            performer = "Nick Barclay",
            coverUrl = "https://i.imgur.com/1zvagWq.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "AM",
            performer = "Arctic monkeys",
            coverUrl = "https://i.imgur.com/1PCmHrn.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Hard for me",
            performer = "Russ",
            coverUrl = "https://i.imgur.com/XhPZb37.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Diemon",
            performer = "Russ",
            coverUrl = "https://i.imgur.com/r5rfX5s.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "That's The Spirit",
            performer = "Bring Me The Horizon",
            coverUrl = "https://i.imgur.com/db6ycGv.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Diemon",
            performer = "Russ",
            coverUrl = "https://i.imgur.com/L7KLYu9.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "To Be Honest",
            performer = "Russ feat. Bugus",
            coverUrl = "https://i.imgur.com/GkR1BUv.jpg"
        ),
        Album(
            UUID.randomUUID().toString(),
            name = "Blueprint",
            performer = "Jay-Z",
            coverUrl = "https://i.imgur.com/OCdEbyi.jpg"
        )
    )
}