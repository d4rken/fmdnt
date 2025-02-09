package eu.darken.fmdn.tracker.core.afn

import eu.darken.fmdn.common.ca.CaString
import eu.darken.fmdn.sonar.core.TrackerPing
import eu.darken.fmdn.sonar.core.devices.afn.AFNTrackerPing
import eu.darken.fmdn.tracker.core.Tracker

data class DefaultAFNTracker(
    override val id: Tracker.Id,
    override val label: CaString,
    override val lastPing: AFNTrackerPing?
) : AFNTracker