package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.feed.domain.model.MenuItem
import java.util.*

const val RELEASES_MENU_ITEM_ID = "releases"
const val CHART_MENU_ITEM_ID = "chart"
const val PODCASTS_MENU_ITEM_ID = "podcasts"

object MenuItemsSource {
    val menuItems = listOf(
        MenuItem(
            id = RELEASES_MENU_ITEM_ID,
            name = "Fresh releases"
        ),
        MenuItem(
            id = CHART_MENU_ITEM_ID,
            name = "Chart"
        ),
        MenuItem(
            id = PODCASTS_MENU_ITEM_ID,
            name = "Podcasts"
        )
    )
}