package eu.darken.fmdn.sonar.core.devices.afn

import android.bluetooth.le.ScanFilter
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import eu.darken.fmdn.common.asHex
import eu.darken.fmdn.common.coroutine.AppScope
import eu.darken.fmdn.common.debug.logging.Logging.Priority.ERROR
import eu.darken.fmdn.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.fmdn.common.debug.logging.Logging.Priority.WARN
import eu.darken.fmdn.common.debug.logging.asLog
import eu.darken.fmdn.common.debug.logging.log
import eu.darken.fmdn.common.debug.logging.logTag
import eu.darken.fmdn.common.flow.replayingShare
import eu.darken.fmdn.sonar.core.ble.BleScanResult
import eu.darken.fmdn.sonar.core.devices.PingFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Reusable
class AFNPingFilter @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
) : PingFilter {

    override val scanFilters = flowOf<Set<ScanFilter>>(
        // General continuity protocol style https://adamcatley.com/AirTag.html
        setOf(
            // Maintained FindMy / Near owner
            // 07 FF 4C 00|12 02#00#03
            // 07 FF 4C 00|12 02#10#03
            // 07 FF 4C 00|12 02#14#03
            // Unmaintained FindMy
            // 1E FF 4C 00|12 19#10#EA C8 E2 B3 93 54 98 E0 1F EA 10 10 38 A9 BF CC 89 6A 82 14 04 B8 03 E2
            ScanFilter.Builder().setManufacturerData(
                APPLE_IDENTIFIER,
                byteArrayOf(0x12),
                byteArrayOf(0xFF.toByte())
            ).build(),

            // Unregistered FindMy
            // 1E FF 4C 00|07 19#05#00 55 10 00 00 01 3C 0B AF 07 2D 1F 1B 2B 85 B2 23 2E B9 67 97 E7 36 D3
            ScanFilter.Builder().setManufacturerData(
                APPLE_IDENTIFIER,
                byteArrayOf(0x07),
                byteArrayOf(0xFF.toByte())
            ).build(),
        )
    ).replayingShare(appScope)

    override suspend fun parse(scan: BleScanResult): AFNTrackerPing? {
        if (!scanFilters.first().any { it.matches(scan.scanResult) }) {
            return null
        }

        val ping = try {
            val payload = scan.scanResult.scanRecord!!.manufacturerSpecificData[0x004C].asUByteArray()
            when (payload.getOrNull(0)) {
                0x12.toUByte() -> DefaultAFNPing(
                    scanResult = scan,
                    raw = payload,
                )

                0x07.toUByte() -> UnregisteredAFNPing(
                    scanResult = scan,
                    raw = payload,
                )

                else -> null
            }

        } catch (e: Exception) {
            log(TAG, ERROR) { "Failed to parse $scan: ${e.asLog()}" }
            null
        }

        if (ping == null) {
            log(TAG, WARN) { "Could not parse $scan" }
            return null
        }

        log(TAG, VERBOSE) { "Parsed $ping from [${ping.raw.asHex()}]" }
        log(TAG) { "Parsed $ping" }

        return ping
    }

    @Module @InstallIn(SingletonComponent::class)
    abstract class DIM {
        @Binds @IntoSet abstract fun mod(mod: AFNPingFilter): PingFilter
    }

    companion object {
        private const val APPLE_IDENTIFIER = 0x004C
        private val TAG = logTag("Tracker", "Sonar", "PingFilter", "AFN")
    }
}