package eu.darken.fmdn.main.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.fmdn.R
import eu.darken.fmdn.common.BuildConfigWrap
import eu.darken.fmdn.common.PrivacyPolicy
import eu.darken.fmdn.common.WebpageTool
import eu.darken.fmdn.common.datastore.PreferenceScreenData
import eu.darken.fmdn.common.uix.PreferenceFragment2
import eu.darken.fmdn.main.core.GeneralSettings
import javax.inject.Inject

@AndroidEntryPoint
class SettingsIndexFragment : PreferenceFragment2() {

    @Inject lateinit var generalSettings: GeneralSettings
    override val settings: PreferenceScreenData
        get() = generalSettings
    override val preferenceFile: Int = R.xml.preferences_index

    @Inject lateinit var webpageTool: WebpageTool

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupMenu(R.menu.menu_settings_index) { item ->
            when (item.itemId) {
                R.id.menu_item_twitter -> {
                    webpageTool.open("https://twitter.com/d4rken")
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPreferencesCreated() {
        findPreference<Preference>("core.changelog")!!.summary = BuildConfigWrap.VERSION_DESCRIPTION
        findPreference<Preference>("core.privacy")!!.setOnPreferenceClickListener {
            webpageTool.open(PrivacyPolicy.URL)
            true
        }
        super.onPreferencesCreated()
    }
}