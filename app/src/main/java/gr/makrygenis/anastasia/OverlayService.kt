package gr.makrygenis.anastasia

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlin.math.abs

/** To koumpi pou epiplei pano apo tin efarmogi YouTube. */
class OverlayService : Service() {

    companion object {
        const val ENERGEIA_STOP = "gr.makrygenis.anastasia.STOP"
    }


    private lateinit var wm: WindowManager
    private var koumpi: TextView? = null
    private var zitimaKleisimou = 0L
    private lateinit var parametroi: WindowManager.LayoutParams

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Apothiki.fortose(this)
        ftiaxeEidopoiisi()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ftiaxeKoumpi()
        Logiki.arxikopoiise()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ENERGEIA_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (intent?.getBooleanExtra("ksekina", false) == true) {
            Logiki.alliMousiki(this) { deikse(it) }
        }
        return START_STICKY
    }

    private fun dp(v: Int): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), resources.displayMetrics
    ).toInt()

    private fun ftiaxeKoumpi() {
        val t = TextView(this).apply {
            text = getString(R.string.allagi)
            setTextColor(0xFFFFFFFF.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            gravity = Gravity.CENTER
            setPadding(dp(26), dp(18), dp(26), dp(18))
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(26).toFloat()
                colors = intArrayOf(0xFFB472F7.toInt(), 0xFF6B21A8.toInt())
                orientation = GradientDrawable.Orientation.TOP_BOTTOM
                setStroke(dp(2), 0x55FFFFFF)
            }
            elevation = dp(8).toFloat()
        }

        val typos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE

        parametroi = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            typos,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            x = dp(24)
            y = dp(90)
        }

        var xArxi = 0
        var yArxi = 0
        var afiArxi = 0f
        var afiArxiY = 0f
        var metakinithike = false
        var xronosKato = 0L

        t.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    xArxi = parametroi.x; yArxi = parametroi.y
                    afiArxi = e.rawX; afiArxiY = e.rawY
                    metakinithike = false
                    xronosKato = System.currentTimeMillis()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (afiArxi - e.rawX).toInt()
                    val dy = (afiArxiY - e.rawY).toInt()
                    if (abs(dx) > dp(10) || abs(dy) > dp(10)) {
                        metakinithike = true
                        parametroi.x = maxOf(0, xArxi + dx)
                        parametroi.y = maxOf(0, yArxi + dy)
                        try { wm.updateViewLayout(t, parametroi) } catch (_: Exception) {}
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!metakinithike) {
                        val diarkeia = System.currentTimeMillis() - xronosKato
                        if (diarkeia > 1200) zitiseKleisimo() else patithike()
                    }
                    true
                }
                else -> false
            }
        }

        koumpi = t
        try { wm.addView(t, parametroi) } catch (e: Exception) {
            Toast.makeText(this, R.string.lathos_overlay, Toast.LENGTH_LONG).show()
            stopSelf()
        }
    }

    private fun patithike() {
        koumpi?.animate()?.scaleX(0.92f)?.scaleY(0.92f)?.setDuration(70)?.withEndAction {
            koumpi?.animate()?.scaleX(1f)?.scaleY(1f)?.duration = 90
        }?.start()
        Logiki.patima(this) { deikse(it) }
    }

    /** Parateameno patima dyo fores = kleisimo. Mia fora den arkei. */
    private fun zitiseKleisimo() {
        val tora = System.currentTimeMillis()
        if (tora - zitimaKleisimou < 6000) {
            Toast.makeText(this, R.string.kleinei, Toast.LENGTH_SHORT).show()
            stopSelf()
        } else {
            zitimaKleisimou = tora
            Toast.makeText(this, R.string.epivevaiosi_kleisimatos, Toast.LENGTH_LONG).show()
        }
    }

    private fun deikse(minima: String) {
        Toast.makeText(this, minima, Toast.LENGTH_SHORT).show()
    }

    private fun ftiaxeEidopoiisi() {
        val kanali = "anastasia_koumpi"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(
                NotificationChannel(kanali, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW)
            )
        }
        val anoigma = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val stopIntent = PendingIntent.getService(
            this, 1,
            Intent(this, OverlayService::class.java).setAction(ENERGEIA_STOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        val e = Notification.Builder(this, kanali)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.trexei))
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(anoigma)
            .setOngoing(true)
            .addAction(
                Notification.Action.Builder(
                    null, getString(R.string.stamata_konta), stopIntent
                ).build()
            )
            .build()
        startForeground(1, e)
    }

    override fun onDestroy() {
        try { koumpi?.let { wm.removeView(it) } } catch (_: Exception) {}
        koumpi = null
        super.onDestroy()
    }
}
