package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.feed.di.FeedScope
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

@FeedScope
class FeedRepository @Inject constructor() {

    private val originalFeed = Feed(
        albums = AlbumsSource.albums,
        menuItems = MenuItemsSource.menuItems,
        editorsChoice = EditorsChoiceSource.editorsChoiceMusic,
        activities = ActivitiesSource.activities
    )
    private val shuffledFeed
        get() = Feed(
            albums = AlbumsSource.albums.shuffled(),
            menuItems = MenuItemsSource.menuItems.shuffled(),
            editorsChoice = EditorsChoiceSource.editorsChoiceMusic.shuffled(),
            activities = ActivitiesSource.activities.shuffled()
        )
    private val noAlbumsFeed = shuffledFeed.copy(
        albums = emptyList()
    )
    private val noMenuItemsAndActivitiesFeed = shuffledFeed.copy(
        activities = emptyList(),
        menuItems = emptyList()
    )
    private val noMenuItemsShuffledActivitiesFeed
        get() = with(shuffledFeed) {
            copy(
                menuItems = emptyList(),
                activities = activities.shuffled()
            )
        }
    private val noEditorsChoiceShuffledFeed
        get() = with(shuffledFeed) {
            copy(
                menuItems = menuItems.shuffled(),
                activities = activities.shuffled(),
                editorsChoice = emptyList()
            )
        }
    private val noActivitiesShuffledMenuItemsFeed
        get() = with(shuffledFeed) {
            copy(
                activities = emptyList(),
                menuItems = menuItems.shuffled()
            )
        }
    private val shuffledAlbumsFeed
        get() = with(shuffledFeed) {
            copy(albums = albums.shuffled())
        }
    private val onlyAlbumsFeed = shuffledAlbumsFeed.copy(
        activities = emptyList(),
        menuItems = emptyList(),
        editorsChoice = emptyList()
    )
    private val randomActivitiesFeed
        get() = with(shuffledFeed) {
            val n = Random(System.currentTimeMillis()).nextInt(
                from = 1,
                until = activities.size + 1
            )
            copy(activities = activities.shuffled().take(n))
        }
    private val randomMenuItemsFeed
        get() = with(shuffledFeed) {
            val n = Random(System.currentTimeMillis()).nextInt(
                from = 1,
                until = menuItems.size + 1
            )
            copy(menuItems = menuItems.shuffled().take(n))
        }

    private val feeds
        get() = listOf(
            originalFeed,
            shuffledFeed,
            noAlbumsFeed,
            noMenuItemsShuffledActivitiesFeed,
            noActivitiesShuffledMenuItemsFeed,
            noMenuItemsAndActivitiesFeed,
            noEditorsChoiceShuffledFeed,
            onlyAlbumsFeed,
            shuffledAlbumsFeed,
            randomActivitiesFeed,
            randomMenuItemsFeed
        )

    private var iterator = feeds.iterator()
    private val nextFeed: Feed
        get() {
            val possibleAlbumsListSizes = listOf(2, 4, 5, 6)
            val index = Random(System.currentTimeMillis()).nextInt(
                from = 0,
                until = possibleAlbumsListSizes.size
            )
            val n = possibleAlbumsListSizes[index]
            val feed = when {
                iterator.hasNext() -> iterator.next()
                else -> {
                    iterator = feeds.iterator()
                    iterator.next()
                }
            }
            return feed.copy(albums = feed.albums.take(n))
        }

    fun getFeed(
        isStaticInterface: Boolean
    ): Feed {
        return when {
            isStaticInterface -> originalFeed
            else -> {
                tryImitateError()
                nextFeed
            }
        }
    }


    private fun tryImitateError() {
        Random(System.currentTimeMillis()).nextInt(9).let {
            //One of 10 cases results in an error
            if (it == 0) {
                throw IOException()
            }
        }
    }
}