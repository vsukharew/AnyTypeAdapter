package vsukharev.anytypeadapter.sample.feed.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.sample.Injector
import vsukharev.anytypeadapter.sample.NoInternetDelegate
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment
import vsukharev.anytypeadapter.sample.common.presentation.delegate.*
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.presentation.FeedPresenter
import vsukharev.anytypeadapter.sample.feed.presentation.model.FeedUi
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumsSectionDelegate
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.activity.ActivitySectionDelegate
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceSectionDelegate
import javax.inject.Inject

/**
 * Fragment showing the list of albums
 */
class FeedFragment : BaseFragment(), FeedView {
    private val adapter = AnyTypeAdapter()
    private val noInternetDelegate = NoInternetDelegate { showSnackBar(it) }
    private val activitiesDelegate = ActivitySectionDelegate { showSnackBar(it.name) }
    private val iconWithTextDelegate = IconWithTextDelegate { showSnackBar(it.text) }
    private val albumsSectionDelegate = AlbumsSectionDelegate {
        showSnackBar("${it.name} - ${it.performer}")
    }
    private val editorsChoiceDelegate = EditorsChoiceSectionDelegate {
        showSnackBar("${it.starName} - ${it.description}")
    }
    private val headerDelegate = PartiallyColoredHeaderDelegate(
        android.R.color.white,
        R.color.colorSecondary
    )
    private val dividerDelegate = DividerDelegate()
    private val headerWithButtonDelegate = HeaderWithButtonDelegate()
    private val editorsChoiceHeaderItem = HeaderWithButtonAdapterItem(
        R.string.feed_fragment_editors_choice,
        R.string.feed_fragment_view_all_btn,
        { showSnackBar(it) }
    )
    private val activitiesHeaderItem = HeaderWithButtonAdapterItem(
        R.string.feed_fragment_activities,
        R.string.feed_fragment_view_all_btn,
        { showSnackBar(it) }
    )

    @Inject
    @InjectPresenter
    lateinit var presenter: FeedPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.buildFeedComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feed_toolbar.inflateMenu(R.menu.menu_feed)
        feed_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_feed_is_static_interface -> {
                    it.isChecked = !it.isChecked
                    presenter.reloadData(it.isChecked)
                    true
                }
                else -> false
            }
        }
        albums_rv.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.apply {
            if (isFinishing) {
                Injector.destroyFeedComponent()
            }
        }
    }

    override fun showData(data: FeedUi) {
        AnyTypeCollection.Builder()
            .apply {
                with(data) {
                    addAlbumsSection(albums)
                    addMenuItems(menuItemsUi)
                    addActivitiesSection(activities)
                    addEditorsChoiceSection(editorsChoice)
                }
            }
            .build()
            .also { adapter.setCollection(it) }
    }

    override fun showProgress() {
        albums_pb.isVisible = true
    }

    override fun hideProgress() {
        albums_pb.isVisible = false
    }

    override fun showNoInternetError(e: Throwable) {
        AnyTypeCollection.Builder()
            .add(noInternetDelegate)
            .build()
            .let { adapter.setCollection(it) }
    }

    override fun hideError() {
        AnyTypeCollection.Builder().build().let { adapter.setCollection(it) }
    }

    private fun showSnackBar(message: String) {
        Snackbar
            .make(feed_root_layout, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun AnyTypeCollection.Builder.addAlbumsSection(
        items: List<Album>
    ): AnyTypeCollection.Builder {
        return apply {
            addIf(
                getString(R.string.feed_fragment_based_on_your_preferences),
                headerDelegate
            ) { items.isNotEmpty() }
            add(items, albumsSectionDelegate)
        }
    }

    private fun AnyTypeCollection.Builder.addMenuItems(items: List<IconWithTextAdapterItem>): AnyTypeCollection.Builder {
        return apply {
            addDividerIfItemsNotEmpty(items)
            add(items, iconWithTextDelegate)
        }
    }

    private fun AnyTypeCollection.Builder.addActivitiesSection(
        activities: List<Activity>
    ): AnyTypeCollection.Builder {
        return apply {
            addDividerIfItemsNotEmpty(activities)
            addIf(activitiesHeaderItem, headerWithButtonDelegate) { activities.isNotEmpty() }
            add(activities, activitiesDelegate)
        }
    }

    private fun AnyTypeCollection.Builder.addEditorsChoiceSection(
        editorsChoices: List<EditorsChoice>
    ): AnyTypeCollection.Builder {
        return apply {
            addDividerIfItemsNotEmpty(editorsChoices)
            addIf(editorsChoiceHeaderItem, headerWithButtonDelegate) { editorsChoices.isNotEmpty() }
            add(editorsChoices, editorsChoiceDelegate)
        }
    }

    private fun <T> AnyTypeCollection.Builder.addDividerIfItemsNotEmpty(
        items: List<T>
    ): AnyTypeCollection.Builder {
        return apply {
            addIf(R.dimen.dp16, dividerDelegate) { items.isNotEmpty() }
        }
    }

    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }
}