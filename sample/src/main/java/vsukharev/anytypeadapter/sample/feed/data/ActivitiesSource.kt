package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity

object ActivitiesSource {

    val activities = listOf(
        Activity(
            id = "running",
            name = "Running",
            iconRes = R.drawable.ic_running
        ),
        Activity(
            id = "workout",
            name = "Workout",
            iconRes = R.drawable.ic_dumbbell
        ),
        Activity(
            id = "driving",
            name = "Driving",
            iconRes = R.drawable.ic_car
        ),
        Activity(
            id = "party",
            name = "Party",
            iconRes = R.drawable.ic_party
        ),
        Activity(
            id = "travelling",
            name = "Travelling",
            iconRes = R.drawable.ic_airplane
        ),
        Activity(
            id = "bedtime",
            name = "Bedtime",
            iconRes = R.drawable.ic_moon
        ),
        Activity(
            id = "romantic",
            name = "Romantic\ndinner",
            iconRes = R.drawable.ic_hearts
        ),
        Activity(
            id = "waking up",
            name = "Waking up",
            iconRes = R.drawable.ic_wakeclock
        )
    )
}