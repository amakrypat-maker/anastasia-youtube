package gr.makrygenis.anastasia

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.net.Uri
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

object Youtube {

    const val PAKETO = "com.google.android.youtube"
    private const val TAG = "AnastasiaYT"

    // ---------------------------------------------------------------
    //  Epomeno tragoudi: i idia entoli me to koumpi tou akoustikou
    // ---------------------------------------------------------------
    fun epomenoTragoudi(c: Context): Boolean {
        val ctrl = vresControllerYoutube(c) ?: return false
        return try {
            ctrl.transportControls.skipToNext()
            true
        } catch (e: Exception) {
            Log.w(TAG, "skipToNext apetyxe", e)
            false
        }
    }

    fun paizeiTora(c: Context): Boolean {
        val ctrl = vresControllerYoutube(c) ?: return false
        val st = ctrl.playbackState ?: return false
        return st.state == android.media.session.PlaybackState.STATE_PLAYING
    }

    private fun vresControllerYoutube(c: Context): MediaController? {
        return try {
            val msm = c.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
            val component = ComponentName(c, MediaListener::class.java)
            msm.getActiveSessions(component).firstOrNull { it.packageName == PAKETO }
        } catch (e: SecurityException) {
            Log.w(TAG, "Den exoume prosvasi se eidopoiiseis", e)
            null
        } catch (e: Exception) {
            Log.w(TAG, "Den vrethike session", e)
            null
        }
    }

    // ---------------------------------------------------------------
    //  Alli mousiki: anoigei to YouTube se tyxaio tragoudi tis listas
    // ---------------------------------------------------------------
    fun anoikseLista(c: Context, lista: String): Boolean {
        val ids = vresVinteo(lista)
        val uri = if (ids.isEmpty()) {
            // Efedriko: i selida tis listas. Den paizei moni tis, alla fainetai.
            Uri.parse("https://www.youtube.com/playlist?list=$lista")
        } else {
            val v = ids[Random.nextInt(ids.size)]
            Uri.parse("https://www.youtube.com/watch?v=$v&list=$lista")
        }
        val i = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage(PAKETO)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Zitaei apo to YouTube na anoiksei se pliri othoni
            putExtra("force_fullscreen", true)
            putExtra("finish_on_ended", false)
        }
        return try {
            c.startActivity(i)
            ids.isNotEmpty()
        } catch (e: Exception) {
            Log.w(TAG, "Den anoigei to YouTube", e)
            try {
                c.startActivity(Intent(Intent.ACTION_VIEW, uri)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                ids.isNotEmpty()
            } catch (e2: Exception) {
                false
            }
        }
    }

    /** Ta videoId tis listas - prota apo to RSS, meta apo ti selida. */
    fun vresVinteo(lista: String): List<String> {
        apoRss(lista).let { if (it.isNotEmpty()) return it }
        return apoSelida(lista)
    }

    private fun apoRss(lista: String): List<String> {
        val keimeno = katevase(
            "https://www.youtube.com/feeds/videos.xml?playlist_id=$lista"
        ) ?: return emptyList()
        return Regex("<yt:videoId>([\\w-]{11})</yt:videoId>")
            .findAll(keimeno).map { it.groupValues[1] }.distinct().toList()
    }

    private fun apoSelida(lista: String): List<String> {
        val keimeno = katevase(
            "https://www.youtube.com/playlist?list=$lista"
        ) ?: return emptyList()
        return Regex("\"videoId\":\"([\\w-]{11})\"")
            .findAll(keimeno).map { it.groupValues[1] }.distinct().take(80).toList()
    }

    private fun katevase(dieuthynsi: String): String? {
        var syndesi: HttpURLConnection? = null
        return try {
            syndesi = (URL(dieuthynsi).openConnection() as HttpURLConnection).apply {
                connectTimeout = 8000
                readTimeout = 8000
                instanceFollowRedirects = true
                setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/124.0 Safari/537.36"
                )
                setRequestProperty("Accept-Language", "el,en;q=0.8")
            }
            if (syndesi.responseCode !in 200..299) return null
            syndesi.inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.w(TAG, "Apotyxia lipsis: $dieuthynsi", e)
            null
        } finally {
            try { syndesi?.disconnect() } catch (_: Exception) {}
        }
    }
}
