package eu.darken.fmdn.tracker.ui.list.items

import android.view.ViewGroup
import eu.darken.fmdn.R
import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.common.lists.binding
import eu.darken.fmdn.databinding.TrackerListGfdItemBinding
import eu.darken.fmdn.tracker.core.gfd.GFDTracker
import eu.darken.fmdn.tracker.ui.list.TrackerAdapter
import kotlin.math.abs

class GFDTrackerCardVH(parent: ViewGroup) :
    TrackerAdapter.BaseVH<GFDTrackerCardVH.Item, TrackerListGfdItemBinding>(
        R.layout.tracker_list_gfd_item,
        parent
    ) {

    override val viewBinding = lazy { TrackerListGfdItemBinding.bind(itemView) }

    override val onBindData: TrackerListGfdItemBinding.(
        item: Item,
        payloads: List<Any>
    ) -> Unit = binding { item ->
        title.text = item.tracker.label.get(context)
        subtitle.text = item.tracker.lastPing?.quickInfo
        distance.text = item.tracker.lastPing?.let { (((100 - abs(it.rssi)) / 100f).toString()) }
    }

    data class Item(
        val tracker: GFDTracker,
    ) : TrackerAdapter.Item {
        override val stableId: Long = tracker.id.hashCode().toLong()
    }
}