package gr.makrygenis.anastasia

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var riza: LinearLayout
    private lateinit var katastasiOverlay: TextView
    private lateinit var katastasiEidopoiiseis: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Apothiki.fortose(this)
        setContentView(ftiaxeOthoni())
        zitiseEidopoiiseis()
    }

    override fun onResume() {
        super.onResume()
        ananeose()
    }

    private fun dp(v: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), resources.displayMetrics).toInt()

    private fun titlos(keimeno: String) = TextView(this).apply {
        text = keimeno
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        setPadding(0, dp(6), 0, dp(10))
    }

    private fun keimenaki(keimeno: String) = TextView(this).apply {
        text = keimeno
        setTextColor(0xFFB8A8CE.toInt())
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setPadding(0, 0, 0, dp(10))
    }

    private fun koumpi(keimeno: String, kyrio: Boolean = false, enegeia: () -> Unit) =
        Button(this).apply {
            text = keimeno
            isAllCaps = false
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(if (kyrio) 0xFF1A0A2B.toInt() else Color.WHITE)
            background = GradientDrawable().apply {
                cornerRadius = dp(14).toFloat()
                setColor(if (kyrio) 0xFFB472F7.toInt() else 0xFF2A1142.toInt())
                setStroke(dp(1), 0xFF4A2A6E.toInt())
            }
            setPadding(dp(18), dp(14), dp(18), dp(14))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dp(10) }
            setOnClickListener { enegeia() }
        }

    private fun ftiaxeOthoni(): View {
        riza = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF120B1E.toInt())
            setPadding(dp(20), dp(20), dp(20), dp(28))
        }

        riza.addView(titlos(getString(R.string.app_name)))
        riza.addView(keimenaki(getString(R.string.eisagogi)))

        riza.addView(titlos(getString(R.string.vima1)))
        katastasiOverlay = keimenaki("")
        riza.addView(katastasiOverlay)
        riza.addView(koumpi(getString(R.string.dose_overlay)) { anoikseOverlay() })

        riza.addView(titlos(getString(R.string.vima2)))
        katastasiEidopoiiseis = keimenaki("")
        riza.addView(katastasiEidopoiiseis)
        riza.addView(koumpi(getString(R.string.dose_eidopoiiseis)) { anoikseEidopoiiseis() })

        riza.addView(titlos(getString(R.string.vima3)))
        riza.addView(koumpi(getString(R.string.ksekina), true) { ksekina() })
        riza.addView(koumpi(getString(R.string.stamata)) { stamata() })
        riza.addView(koumpi(getString(R.string.rythmiseis)) {
            startActivity(Intent(this, RythmiseisActivity::class.java))
        })

        riza.addView(TextView(this).apply {
            text = getString(R.string.ypografi)
            setTextColor(0xFF6F5F88.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            gravity = Gravity.CENTER
            setPadding(0, dp(24), 0, 0)
        })

        val scroll = ScrollView(this).apply {
            setBackgroundColor(0xFF120B1E.toInt())
            addView(riza)
        }
        ViewCompat.setOnApplyWindowInsetsListener(scroll) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }
        return scroll
    }

    private fun ananeose() {
        val ok = getString(R.string.entaksei)
        val oxi = getString(R.string.leipei)
        katastasiOverlay.text = if (Settings.canDrawOverlays(this)) ok else oxi
        katastasiEidopoiiseis.text = if (exoumeEidopoiiseis()) ok else oxi
    }

    private fun exoumeEidopoiiseis(): Boolean {
        val energes = Settings.Secure.getString(
            contentResolver, "enabled_notification_listeners") ?: return false
        return energes.contains(packageName)
    }

    private fun anoikseOverlay() {
        startActivity(Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        ))
    }

    private fun anoikseEidopoiiseis() {
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    private fun zitiseEidopoiiseis() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 7)
        }
    }

    private fun ksekina() {
        if (!Settings.canDrawOverlays(this)) { anoikseOverlay(); return }
        if (!exoumeEidopoiiseis()) { anoikseEidopoiiseis(); return }
        val i = Intent(this, OverlayService::class.java).putExtra("ksekina", true)
        ContextCompat.startForegroundService(this, i)
        moveTaskToBack(true)
    }

    private fun stamata() {
        stopService(Intent(this, OverlayService::class.java))
    }
}
