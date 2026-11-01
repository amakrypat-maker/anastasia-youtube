package gr.makrygenis.anastasia

import android.content.Context
import kotlin.random.Random

/** Dialegei lista me ta vari ton kategorion kai ti mathisi. */
object Epilogeas {

    private val dokimasmenes = HashSet<String>()

    fun midenise() = dokimasmenes.clear()

    fun simeioseApotyxia(lista: String?) {
        if (lista != null) dokimasmenes.add(lista)
        if (dokimasmenes.size > 6) dokimasmenes.clear()
    }

    /** Epistrefei (listaId, onomaKategorias) i null an den yparxei kamia. */
    fun dialekse(c: Context, trexousa: String?): Pair<String, String>? {
        val ypopsifies = ArrayList<Triple<String, String, Float>>()
        var synolo = 0f
        for (kat in Listes.oles) {
            for (id in kat.listes) {
                if (Apothiki.einaiNekri(id)) continue
                if (id == trexousa) continue
                if (dokimasmenes.contains(id)) continue
                val v = kat.varos * Apothiki.varosListas(id)
                ypopsifies.add(Triple(id, kat.onoma, v))
                synolo += v
            }
        }
        if (ypopsifies.isEmpty()) return null
        var r = Random.nextFloat() * synolo
        for ((id, onoma, v) in ypopsifies) {
            r -= v
            if (r <= 0f) return Pair(id, onoma)
        }
        val teleftaia = ypopsifies.last()
        return Pair(teleftaia.first, teleftaia.second)
    }
}
