package eu.darken.fmdn.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.fmdn.common.debug.logging.Logging.Priority.ERROR
import eu.darken.fmdn.common.debug.logging.log
import javax.inject.Inject

@Reusable
class WebpageTool @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun open(address: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            log(ERROR) { "Failed to launch" }
        }
    }

}