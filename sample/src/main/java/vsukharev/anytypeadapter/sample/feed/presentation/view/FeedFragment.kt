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
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice
import vsukharev.anytypeadapter.sample.feed.presentation.AlbumsPresenter
import vsukharev.anytypeadapter.sample.feed.presentation.model.HomePageUi
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumAdapterItem
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumsSectionAdapterItem
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumsSectionDelegate
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceAdapterItem
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceSectionAdapterItem
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.editorschoice.EditorsChoiceSectionDelegate
import vsukharev.anytypeadapter.sample.common.network.ConnectivityObserver
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment
import vsukharev.anytypeadapter.sample.common.presentation.delegate.*
import vsukharev.anytypeadapter.sample.feed.presentation.FeedPresenter
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
    private val albumsSectionDelegate = AlbumsSectionDelegate {
        if (it) {
            presenter.onAlbumHeld()
        } else {
            presenter.onAlbumReleased()
        }
    }
    private val dividerDelegate = DividerDelegate()
    private val iconWithTextDelegate = IconWithTextDelegate()
    private val headerWithButtonDelegate = HeaderWithButtonDelegate()
    private val editorsChoiceDelegate = EditorsChoiceSectionDelegate()

    private val connectivityObserver by lazy {
        ConnectivityObserver(requireActivity()) { presenter.reloadData() }
    }

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
        albums_rv.adapter = adapter
        albums_retry_btn.setOnClickListener { presenter.reloadData() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectivityObserver.let { lifecycle.addObserver(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.apply {
            if (isFinishing) {
                Injector.destroyAlbumsScreenComponent()
            }
        }
    }

    override fun showData(data: HomePageUi) {
        albums_error_container.isVisible = false
        Collection.Builder()
            .apply {
                with(data) {
                    addAlbumsSection(albums)
                    addMenuItems()
                    addEditorsChoiceSection(editorsChoice)
                }
            }
            .build()
            .also { adapter.setItems(it) }
    }

    override fun showProgress() {
        albums_pb.isVisible = true
    }

    override fun hideProgress() {
        albums_pb.isVisible = false
    }

    override fun showNoInternetError(e: Throwable) {
        albums_error_container.isVisible = true
    }

    override fun hideError() {
        albums_error_container.isVisible = false
    }

    private fun Collection.Builder.addAlbumsSection(
        items: List<Album>
    ): Collection.Builder {
        return apply {
            with(items) {
                add(
                    HeaderAdapterItem(getString(R.string.albums_fragment_based_on_your_preferences)),
                    headerDelegate
                )
                val section = AlbumsSectionAdapterItem(
                    map { AlbumAdapterItem(it) }
                )
                add(section, albumsSectionDelegate)
            }
        }
    }

    private fun Collection.Builder.addMenuItems(): Collection.Builder {
        return apply {
            add(DividerAdapterItem(R.dimen.dp16), dividerDelegate)
            add(
                IconWithTextAdapterItem(
                    R.drawable.ic_fresh_release,
                    R.string.albums_fragment_fresh_releases
                ),
                iconWithTextDelegate
            )
            add(
                IconWithTextAdapterItem(
                    R.drawable.ic_chart,
                    R.string.albums_fragment_chart
                ),
                iconWithTextDelegate
            )
            add(
                IconWithTextAdapterItem(
                    R.drawable.ic_mic,
                    R.string.albums_fragment_podcasts
                ),
                iconWithTextDelegate
            )
        }
    }

    private fun Collection.Builder.addEditorsChoiceSection(
        editorsChoices: List<EditorsChoice>
    ): Collection.Builder {
        return apply {
            with(editorsChoices) {
                add(
                    HeaderWithButtonAdapterItem(
                        R.string.albums_fragment_often_listened_to,
                        R.string.albums_fragment_view_all_btn
                    ),
                    headerWithButtonDelegate
                )
                val section = EditorsChoiceSectionAdapterItem(
                    map { EditorsChoiceAdapterItem(it) }
                )
                add(
                    section,
                    editorsChoiceDelegate
                )
            }
        }
    }

    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }
}