package vsukharev.anytypeadapter.sample.tracks.presentation.view

import vsukharev.anytypeadapter.sample.common.presentation.view.BaseView
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator
import vsukharev.anytypeadapter.sample.tracks.presentation.model.TracksListItem

interface TracksView : BaseView, Paginator.PaginatorView<TracksListItem>