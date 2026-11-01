package gr.makrygenis.anastasia

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RythmiseisActivity : AppCompatActivity() {

    private lateinit var pOrio: EditText
    private lateinit var pAnoxi: EditText
    private lateinit var pSkip: EditText
    private lateinit var pVaros: EditText
    private lateinit var plirofories: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Apothiki.fortose(this)
        setContentView(othoni())
    }

    private fun dp(v: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), resources.displayMetrics).toInt()

    private fun pedio(etiketa: String, timi: String, dekadiko: Boolean): Pair<View, EditText> {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, dp(14))
        }
        box.addView(TextView(this).apply {
            text = etiketa
            setTextColor(0xFFB8A8CE.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        })
        val e = EditText(this).apply {
            setText(timi)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            inputType = if (dekadiko)
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            else InputType.TYPE_CLASS_NUMBER
            background = GradientDrawable().apply {
                cornerRadius = dp(10).toFloat()
                setColor(0xFF2A1142.toInt())
                setStroke(dp(1), 0xFF4A2A6E.toInt())
            }
            setPadding(dp(14), dp(12), dp(14), dp(12))
        }
        box.addView(e)
        return Pair(box, e)
    }

    private fun othoni(): View {
        val riza = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF120B1E.toInt())
            setPadding(dp(20), dp(20), dp(20), dp(28))
        }

        riza.addView(TextView(this).apply {
            text = getString(R.string.rythmiseis)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            setPadding(0, 0, 0, dp(16))
        })

        val a = pedio(getString(R.string.p_orio), Apothiki.orioDeuterolepta.toString(), false)
        val b = pedio(getString(R.string.p_anoxi), Apothiki.anoxiPatimaton.toString(), false)
        val c = pedio(getString(R.string.p_skip), Apothiki.orioSkip.toString(), false)
        val d = pedio(getString(R.string.p_varos), Apothiki.varosSkip.toString(), true)
        pOrio = a.second; pAnoxi = b.second; pSkip = c.second; pVaros = d.second
        riza.addView(a.first); riza.addView(b.first)
        riza.addView(TextView(this).apply {
            text = getString(R.string.epeksigisi_end)
            setTextColor(0xFF8E7AA6.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            setPadding(0, 0, 0, dp(16))
        })
        riza.addView(c.first); riza.addView(d.first)

        plirofories = TextView(this).apply {
            setTextColor(0xFF8E7AA6.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            setPadding(0, dp(6), 0, dp(16))
        }
        riza.addView(plirofories)
        ananeosePlirofories()

        riza.addView(Button(this).apply {
            text = getString(R.string.apothikefsi)
            isAllCaps = false
            setTextColor(0xFF1A0A2B.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            background = GradientDrawable().apply {
                cornerRadius = dp(14).toFloat(); setColor(0xFFB472F7.toInt())
            }
            setPadding(dp(18), dp(14), dp(18), dp(14))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dp(10) }
            setOnClickListener { apothikefse() }
        })

        riza.addView(Button(this).apply {
            text = getString(R.string.midenismos)
            isAllCaps = false
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            background = GradientDrawable().apply {
                cornerRadius = dp(14).toFloat(); setColor(0xFF2A1142.toInt())
                setStroke(dp(1), 0xFF4A2A6E.toInt())
            }
            setPadding(dp(18), dp(14), dp(18), dp(14))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                Apothiki.katharise(this@RythmiseisActivity, true)
                ananeosePlirofories()
                Toast.makeText(this@RythmiseisActivity,
                    R.string.egine, Toast.LENGTH_SHORT).show()
            }
        })

        val scroll = ScrollView(this).apply {
            setBackgroundColor(0xFF120B1E.toInt())
            addView(riza)
        }
        ViewCompat.setOnApplyWindowInsetsListener(scroll) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        return scroll
    }

    private fun ananeosePlirofories() {
        plirofories.text = getString(
            R.string.plirofories, Listes.synolo, Apothiki.plithosNekron())
    }

    private fun apothikefse() {
        Apothiki.orioDeuterolepta =
            (pOrio.text.toString().toIntOrNull() ?: Apothiki.ORIO_PROEP).coerceIn(5, 600)
        Apothiki.anoxiPatimaton =
            (pAnoxi.text.toString().toIntOrNull() ?: Apothiki.ANOXI_PROEP).coerceIn(0, 10)
        Apothiki.orioSkip =
            (pSkip.text.toString().toIntOrNull() ?: Apothiki.SKIP_PROEP).coerceIn(0, 20)
        Apothiki.varosSkip =
            (pVaros.text.toString().replace(',', '.').toFloatOrNull()
                ?: Apothiki.VAROS_PROEP).coerceIn(0.02f, 1f)
        Apothiki.apothikefseRythmiseis(this)
        Toast.makeText(this, R.string.apothikeftike, Toast.LENGTH_SHORT).show()
        finish()
    }
}
