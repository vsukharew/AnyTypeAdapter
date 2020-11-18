package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import java.util.*

/**
 * The repository making actions with a collection of the [EditorsChoice]
 */
object EditorsChoiceSource {

    val editorsChoiceMusic = listOf(
        EditorsChoice(
            UUID.randomUUID(),
            starName = "Ariana Grande",
            description = "Has what she wants",
            imageUrl = "https://thearrowhead.net/wp-content/uploads/2019/06/Thank-U-Next-900x900.jpg"
        ),
        EditorsChoice(
            UUID.randomUUID(),
            starName = "Wiz Khalifa and Snoop Dogg",
            description = "Advise to live young, wild and free",
            imageUrl = "https://m.media-amazon.com/images/I/81ZghP368YL._SS500_.jpg"
        ),
        EditorsChoice(
            UUID.randomUUID(),
            starName = "A day to Remember",
            description = "Call on to be sincere",
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/b/b0/Bad_Vibrations.jpg"
        )
    )
}