package gr.makrygenis.anastasia

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Vazei to video se pliri othoni. Den kanei tipota allo.
 *
 * Prota psaxnei to koumpi megistopoiisis. An den fainetai (ta xeiristiria
 * tou YouTube kryvontai monadika), kanei sirsimo pros ta pano pano sto
 * video - i kinisi pou vazei to YouTube se pliri othoni.
 */
class Prosvasimotita : AccessibilityService() {

    companion object {
        private const val TAG = "AnastasiaYT"
        @Volatile
        var energi: Prosvasimotita? = null

        private val LEKSEIS = listOf(
            "πλήρης οθόνη", "πληρης οθονη", "πλήρη οθόνη",
            "full screen", "fullscreen"
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        energi = this
        Log.i(TAG, "Prosvasimotita energi")
    }

    override fun onDestroy() {
        energi = null
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    fun megistopoiise() {
        if (patiseKoumpi()) {
            Log.i(TAG, "Megistopoiisi me koumpi")
            return
        }
        sirePano()
    }

    // ------------------------------------------------------------
    private fun patiseKoumpi(): Boolean {
        val riza = try { rootInActiveWindow } catch (e: Exception) { null } ?: return false
        val komvos = vres(riza) ?: return false
        var p: AccessibilityNodeInfo? = komvos
        var vathos = 0
        while (p != null && vathos < 4) {
            if (p.isClickable) {
                return p.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
            p = p.parent
            vathos++
        }
        return false
    }

    private fun vres(komvos: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        val perigrafi = komvos.contentDescription?.toString()?.lowercase()
        if (perigrafi != null && LEKSEIS.any { perigrafi.contains(it) }) return komvos
        for (i in 0 until komvos.childCount) {
            val paidi = komvos.getChild(i) ?: continue
            vres(paidi)?.let { return it }
        }
        return null
    }

    private fun sirePano() {
        val m = resources.displayMetrics
        val x = m.widthPixels / 2f
        val apo = m.heightPixels * 0.20f
        val pros = m.heightPixels * 0.02f
        val diadromi = Path().apply {
            moveTo(x, apo)
            lineTo(x, pros)
        }
        val kinisi = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(diadromi, 0L, 280L))
            .build()
        try {
            dispatchGesture(kinisi, null, null)
            Log.i(TAG, "Megistopoiisi me sirsimo")
        } catch (e: Exception) {
            Log.w(TAG, "Apetyxe to sirsimo", e)
        }
    }
}
