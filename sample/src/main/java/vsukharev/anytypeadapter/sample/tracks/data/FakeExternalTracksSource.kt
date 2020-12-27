package vsukharev.anytypeadapter.sample.tracks.data

import vsukharev.anytypeadapter.sample.tracks.domain.model.Track

object FakeExternalTracksSource {

    private val bringMeTheHorizonTracks = listOf(
        "Doomed",
        "Happy Song",
        "Throne",
        "True Friends",
        "Follow you",
        "What you need",
        "Avalanche",
        "Run",
        "Drown",
        "Blasphemy",
        "Oh no"
    ).mapToTracks(
        performer = "Bring Me The Horizon",
        albumCoverUrl = "https://i.imgur.com/db6ycGv.jpg"
    )

    private val jayZTracks = listOf(
        "The Ruler's Back",
        "Takeover",
        "Izzo (H.O.V.A.)",
        "Girls, Girls, Girls",
        "Jigga That Nigga",
        "U Don't Know",
        "Hola' Hovito",
        "Heart Of The City (Ain't No Love)",
        "Never Change",
        "Song Cry",
        "All I Need",
        "Renegade",
        "Blueprint (Momma Loves Me)",
        "Lyrical Exercise"
    ).mapToTracks(
        performer = "Jay-Z",
        albumCoverUrl = "https://i.imgur.com/OCdEbyi.jpg"
    )

    private val kanyeWestTracks = listOf(
        "Good Morning",
        "Champion",
        "Stronger",
        "I Wonder",
        "Good Life",
        "Can't Tell Me Nothing",
        "Barry Bonds",
        "Drunk and Hot Girls",
        "Flashing Lights",
        "Everything I Am",
        "The Glory",
        "Homecoming",
        "Big Brother"
    ).mapToTracks(
        performer = "Kanye West",
        albumCoverUrl = "https://i.imgur.com/NwZ1bMk.jpg"
    )

    private val arcticMonkeysTracks = listOf(
        "Do I Wanna Know?",
        "R U Mine?",
        "One For The Road",
        "Arabella",
        "I Want It All",
        "No. 1 Party Anthem",
        "Mad Sounds",
        "Fireside",
        "Why'd You Only Call Me When You're High?",
        "Snap Out Of It",
        "Knee Socks",
        "I Wanna Be Yours"
    ).mapToTracks(
        performer = "Arctic Monkeys",
        albumCoverUrl = "https://i.imgur.com/1PCmHrn.jpg"
    )

    private val russTracks = listOf(
        "Still",
        "Paid Off",
        "Hard For Me",
        "Diemon",
        "Do It Myself",
        "My Baby",
        "Flip",
        "Inside Job",
        "Take You Back",
        "Throne Talks",
        "Back To You",
        "Got this",
        "One More Chance",
        "I'm Here",
        "Last Forever"
    ).mapToTracks(
        performer = "Russ",
        albumCoverUrl = "https://i.imgur.com/XhPZb37.jpg"
    )

    private val leeKnuttilaTracks = listOf(
        "Fake Plastic Trees",
        "High And Dry",
        "Everything In Its Right Place",
        "How To Disappear Completely",
        "All I Need",
        "Man of War",
        "Knives Out",
        "Reckoner",
        "Daydreaming",
        "Pyramid Song",
        "Back To You",
        "Nude",
        "Burn the Witch",
        "Idioteque",
        "True Love Waits"
    ).mapToTracks(
        performer = "Lee Knutilla",
        albumCoverUrl = "https://i.imgur.com/smdV8sL.png"
    )

    private val architectureInHelsinkiTracks = listOf(
        "In the Future",
        "That Beep",
        "Feather In A Baseball Cap",
        "One Heavy February",
        "The Owls Go",
        "Do The Whirlwind",
        "Nothing's Wrong",
        "Lazy (Lazy)",
        "Daydreaming",
        "Scissor Paper Rock",
        "Kindling",
        "Nude",
        "Underwater",
        "It's Almost a Trap",
        "Vanishing"
    ).mapToTracks(
        performer = "Architecture In Helsinki",
        albumCoverUrl = "https://i.imgur.com/88yZF0y.jpg"
    )

    private val sharonVanEttenTracks = listOf(
        "Seventeen",
        "The End of the World",
        "Serpent",
        "We Are Fine",
        "Every Time the Sun Comes Up",
        "Blue Christmas",
        "Let Go",
        "Tarifa",
        "Comeback Kid",
        "Lemon",
        "Jupiter 4",
        "Nude",
        "Impossible Weight",
        "Give Out",
        "Silent Night",
        "You Know Me Well",
        "Taking Chances",
        "Malibu",
        "You Shadow",
        "Taking Chances"
    ).mapToTracks(
        performer = "Sharon van Etten",
        albumCoverUrl = "https://i.imgur.com/88yZF0y.jpg"
    )

    val tracks = listOf(
        bringMeTheHorizonTracks,
        jayZTracks,
        kanyeWestTracks,
        arcticMonkeysTracks,
        russTracks,
        leeKnuttilaTracks,
        architectureInHelsinkiTracks,
        sharonVanEttenTracks
    ).flatten().shuffled()

    private fun createTrack(
        name: String,
        performer: String,
        albumCoverUrl: String
    ): Track = Track(name, performer, albumCoverUrl)

    private fun List<String>.mapToTracks(performer: String, albumCoverUrl: String): List<Track> =
        map { createTrack(it, performer, albumCoverUrl) }
}