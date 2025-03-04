package eu.darken.fmdn.main.ui.settings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.fmdn.common.coroutine.DispatcherProvider
import eu.darken.fmdn.common.uix.ViewModel2
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentVM @Inject constructor(
    private val handle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel2(dispatcherProvider)