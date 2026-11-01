package gr.makrygenis.anastasia

import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * I logiki tou koumpiou - akrivos i idia me to plitro END sto PC.
 *   Patima meta apo "orio" deuterolepta  -> allazei TRAGOUDI
 *   Patima noritera                      -> allazei MOUSIKI
 *   ektos an apomenoun patimata "anoxis"
 */
object Logiki {

    @Volatile var trexousaLista: String? = null
    @Volatile var trexonOnoma: String = ""
    private var teleftaiaDrasi = 0L
    private var metritisSkip = 0
    @Volatile private var apasxolimenos = false

    private val kyrioNima = Handler(Looper.getMainLooper())

    fun arxikopoiise() {
        teleftaiaDrasi = System.currentTimeMillis()
        metritisSkip = 0
    }

    fun patima(c: Context, minima: (String) -> Unit) {
        if (apasxolimenos) return
        val tora = System.currentTimeMillis()
        val perasan = (tora - teleftaiaDrasi) / 1000.0
        val paizei = Youtube.paizeiTora(c)

        Apothiki.simeioseSkip(c, trexousaLista, perasan < Apothiki.orioDeuterolepta)

        val allaxeTragoudi: Boolean = when {
            !paizei -> false
            perasan >= Apothiki.orioDeuterolepta -> { metritisSkip = 1; true }
            metritisSkip in 1 until Apothiki.anoxiPatimaton -> { metritisSkip++; true }
            else -> false
        }
        teleftaiaDrasi = tora

        if (allaxeTragoudi) {
            if (Youtube.epomenoTragoudi(c)) {
                minima("Άλλο τραγούδι")
                return
            }
            // An den apantise to YouTube, allazoume mousiki
        }
        metritisSkip = 0
        alliMousiki(c, minima)
    }

    fun alliMousiki(c: Context, minima: (String) -> Unit) {
        if (apasxolimenos) return
        apasxolimenos = true
        minima("Αλλάζω μουσική…")
        // Katevazoume to stroma gia na perasei i kinisi megistopoiisis
        OverlayService.energi?.katevaseFrakti()
        Thread {
            var epityxia = false
            var onoma = ""
            for (prospatheia in 0 until 4) {
                val epilogi = Epilogeas.dialekse(c, trexousaLista)
                if (epilogi == null) { break }
                val (id, onomaKat) = epilogi
                val ok = Youtube.anoikseLista(c, id)
                if (ok) {
                    trexousaLista = id
                    trexonOnoma = onomaKat
                    onoma = onomaKat
                    Epilogeas.midenise()
                    epityxia = true
                    break
                } else {
                    Apothiki.simeioseNekri(c, id)
                    Epilogeas.simeioseApotyxia(id)
                }
            }
            if (epityxia) {
                // Afinoume to YouTube na iremisei kai meta zitame pliri othoni
                kyrioNima.postDelayed({
                    Prosvasimotita.energi?.megistopoiise()
                }, 2500)
            }
            kyrioNima.postDelayed({
                OverlayService.energi?.anevaseFrakti()
            }, 4500)
            val telikoMinima = if (epityxia) onoma else "Δεν βρέθηκε λίστα"
            kyrioNima.post {
                apasxolimenos = false
                minima(telikoMinima)
            }
        }.start()
    }
}
