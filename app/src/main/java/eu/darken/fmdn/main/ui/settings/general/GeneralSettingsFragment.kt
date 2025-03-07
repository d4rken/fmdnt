package eu.darken.fmdn.main.ui.settings.general

import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.fmdn.R
import eu.darken.fmdn.common.preferences.ListPreference2
import eu.darken.fmdn.common.preferences.setupWithEnum
import eu.darken.fmdn.common.uix.PreferenceFragment2
import eu.darken.fmdn.main.core.GeneralSettings
import javax.inject.Inject

@Keep
@AndroidEntryPoint
class GeneralSettingsFragment : PreferenceFragment2() {

    private val vdc: GeneralSettingsFragmentVM by viewModels()

    @Inject lateinit var debugSettings: GeneralSettings

    override val settings: GeneralSettings by lazy { debugSettings }
    override val preferenceFile: Int = R.xml.preferences_general

    private val themeModePref: ListPreference2
        get() = findPreference(settings.themeMode.keyName)!!
    private val themeStylePref: ListPreference2
        get() = findPreference(settings.themeStyle.keyName)!!

    override fun onPreferencesCreated() {
        themeModePref.setupWithEnum(settings.themeMode)
        themeStylePref.setupWithEnum(settings.themeStyle)

        super.onPreferencesCreated()
    }

}