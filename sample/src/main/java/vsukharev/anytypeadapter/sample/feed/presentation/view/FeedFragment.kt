package vsukharev.anytypeadapter.sample.feed.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_feed.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
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
    private val headerDelegate = PartiallyColoredHeaderDelegate(
        android.R.color.white,
        R.color.colorSecondary
    )
    private val albumsSectionDelegate = AlbumsSectionDelegate {}
    private val dividerDelegate = DividerDelegate()
    private val iconWithTextDelegate = IconWithTextDelegate()
    private val headerWithButtonDelegate = HeaderWithButtonDelegate()
    private val editorsChoiceDelegate = EditorsChoiceSectionDelegate()
    private val activitiesDelegate = ActivitySectionDelegate()
    private val noInternetDelegate = NoInternetDelegate()
    private val editorsChoice = HeaderWithButtonAdapterItem(
        R.string.albums_fragment_editors_choice, R.string.albums_fragment_view_all_btn
    )

    @Inject
    @InjectPresenter
    lateinit var presenter: FeedPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.buildAlbumsScreenComponent().inject(this)
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
        feed_toolbar.inflateMenu(R.menu.interface_settings_menu)
        feed_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.is_static_interface -> {
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
                Injector.destroyAlbumsScreenComponent()
            }
        }
    }

    override fun showData(data: FeedUi) {
        Collection.Builder()
            .apply {
                with(data) {
                    addAlbumsSection(albums)
                    addMenuItems(menuItems)
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
        Collection.Builder()
            .add(noInternetDelegate)
            .build()
            .let { adapter.setCollection(it) }
    }

    override fun hideError() {
        Collection.Builder().build().let { adapter.setCollection(it) }
    }

    private fun Collection.Builder.addAlbumsSection(
        items: List<Album>
    ): Collection.Builder {
        return apply {
            addIf(
                getString(R.string.albums_fragment_based_on_your_preferences),
                headerDelegate
            ) { items.isNotEmpty() }
            add(items, albumsSectionDelegate)
        }
    }

    private fun Collection.Builder.addMenuItems(items: List<IconWithTextAdapterItem>): Collection.Builder {
        return apply {
            addDividerIfItemsNotEmpty(items)
            add(items, iconWithTextDelegate)
        }
    }

    private fun Collection.Builder.addActivitiesSection(
        activities: List<Activity>
    ): Collection.Builder {
        return apply {
            addDividerIfItemsNotEmpty(activities)
            add(activities, activitiesDelegate)
        }
    }

    private fun Collection.Builder.addEditorsChoiceSection(
        editorsChoices: List<EditorsChoice>
    ): Collection.Builder {
        return apply {
            addDividerIfItemsNotEmpty(editorsChoices)
            addIf(editorsChoice, headerWithButtonDelegate) { editorsChoices.isNotEmpty() }
            add(editorsChoices, editorsChoiceDelegate)
        }
    }

    private fun <T> Collection.Builder.addDividerIfItemsNotEmpty(
        items: List<T>
    ): Collection.Builder {
        return apply {
            addIf(R.dimen.dp16, dividerDelegate) { items.isNotEmpty() }
        }
    }

    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }
}