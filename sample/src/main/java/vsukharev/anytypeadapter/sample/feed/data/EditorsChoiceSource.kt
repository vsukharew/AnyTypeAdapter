package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import java.util.*

/**
 * The repository making actions with a collection of the [EditorsChoice]
 */
object EditorsChoiceSource {

    val editorsChoiceMusic = listOf(
        EditorsChoice(
            UUID.randomUUID(), // TODO make default value for id
            starName = "Ariana Grande",
            description = "Has what she wants",
            imageUrl = "https://i.imgur.com/fI9qNhe.jpg"
        ),
        EditorsChoice(
            UUID.randomUUID(),
            starName = "Wiz Khalifa and Snoop Dogg",
            description = "Advise to live young, wild and free",
            imageUrl = "https://i.imgur.com/pVjoHNM.jpg"
        ),
        EditorsChoice(
            UUID.randomUUID(),
            starName = "A day to Remember",
            description = "Call on to be sincere",
            imageUrl = "https://i.imgur.com/8YQibd4.jpg"
        )
    )
}