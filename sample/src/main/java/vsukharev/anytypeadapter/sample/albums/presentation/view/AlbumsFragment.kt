package vsukharev.anytypeadapter.sample.albums.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_albums.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.sample.Injector
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.albums.presentation.AlbumsPresenter
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsSectionAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsSectionDelegate
import vsukharev.anytypeadapter.sample.common.presentation.delegate.HeaderAdapterItem
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PartiallyColoredHeaderDelegate
import javax.inject.Inject

/**
 * Fragment showing the list of albums
 */
class AlbumsFragment : MvpAppCompatFragment(), AlbumsView {
    private val adapter = AnyTypeAdapter()
    private val headerDelegate = PartiallyColoredHeaderDelegate(
        android.R.color.white,
        R.color.colorSecondary
    )
    private val albumsSectionDelegate = AlbumsSectionDelegate()

    @Inject
    @InjectPresenter
    lateinit var presenter: AlbumsPresenter

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
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun showItems(items: AlbumsSectionAdapterItem) {
        Collection.Builder()
            .add(HeaderAdapterItem(getString(R.string.albums_fragment_based_on_your_preferences)), headerDelegate)
            .add(items, albumsSectionDelegate)
            .build()
            .also { adapter.setItems(it) }
    }

    companion object {
        fun newInstance(): AlbumsFragment = AlbumsFragment()
    }
}