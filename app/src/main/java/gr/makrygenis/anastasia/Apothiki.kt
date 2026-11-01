package gr.makrygenis.anastasia

import android.content.Context
import org.json.JSONObject

/** Rythmiseis, mathisi kai nekres listes - ola se SharedPreferences. */
object Apothiki {

    private const val ARXEIO = "anastasia"

    // Idies proepiloges me to PC
    const val ORIO_PROEP = 48
    const val ANOXI_PROEP = 1
    const val SKIP_PROEP = 3
    const val VAROS_PROEP = 0.2f

    private fun pref(c: Context) = c.getSharedPreferences(ARXEIO, Context.MODE_PRIVATE)

    var orioDeuterolepta: Int = ORIO_PROEP
    var anoxiPatimaton: Int = ANOXI_PROEP
    var orioSkip: Int = SKIP_PROEP
    var varosSkip: Float = VAROS_PROEP

    private val skips = HashMap<String, Int>()
    private val nekres = HashSet<String>()

    fun fortose(c: Context) {
        val p = pref(c)
        orioDeuterolepta = p.getInt("orio", ORIO_PROEP)
        anoxiPatimaton = p.getInt("anoxi", ANOXI_PROEP)
        orioSkip = p.getInt("orioSkip", SKIP_PROEP)
        varosSkip = p.getFloat("varosSkip", VAROS_PROEP)
        skips.clear(); nekres.clear()
        try {
            val o = JSONObject(p.getString("skips", "{}") ?: "{}")
            for (k in o.keys()) skips[k] = o.getInt(k)
        } catch (_: Exception) {}
        p.getStringSet("nekres", emptySet())?.let { nekres.addAll(it) }
    }

    fun apothikefseRythmiseis(c: Context) {
        pref(c).edit()
            .putInt("orio", orioDeuterolepta)
            .putInt("anoxi", anoxiPatimaton)
            .putInt("orioSkip", orioSkip)
            .putFloat("varosSkip", varosSkip)
            .apply()
    }

    private fun apothikefseSkips(c: Context) {
        val o = JSONObject()
        for ((k, v) in skips) o.put(k, v)
        pref(c).edit().putString("skips", o.toString()).apply()
    }

    /** Grigoro patima = tin prospernaei. Argo = tis arese. */
    fun simeioseSkip(c: Context, lista: String?, pidikse: Boolean) {
        if (lista == null) return
        if (pidikse) skips[lista] = (skips[lista] ?: 0) + 1 else skips[lista] = 0
        apothikefseSkips(c)
    }

    fun varosListas(lista: String): Float {
        if (orioSkip <= 0) return 1f
        return if ((skips[lista] ?: 0) >= orioSkip) maxOf(0.02f, varosSkip) else 1f
    }

    fun einaiNekri(lista: String) = nekres.contains(lista)

    fun simeioseNekri(c: Context, lista: String?) {
        if (lista == null) return
        nekres.add(lista)
        pref(c).edit().putStringSet("nekres", HashSet(nekres)).apply()
    }

    fun plithosNekron() = nekres.size

    fun katharise(c: Context, kaiNekres: Boolean) {
        skips.clear()
        if (kaiNekres) nekres.clear()
        pref(c).edit()
            .putString("skips", "{}")
            .putStringSet("nekres", HashSet(nekres))
            .apply()
    }
}
