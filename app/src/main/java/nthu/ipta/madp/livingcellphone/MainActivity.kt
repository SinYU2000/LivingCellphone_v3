package nthu.ipta.madp.livingcellphone


import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.os.BatteryManager
import android.os.Build
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_charging.mp_charging
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_powerlevel.mp_powerHigh
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_powerlevel.mp_powerLow
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_powerlevel.mp_powerMid


class MainActivity : AppCompatActivity() {

    private var mp_volumeUpKey2: MediaPlayer? = null
    private var mp_volumeDownKey2: MediaPlayer? = null

    private var context: Context? = null
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        imageView1.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView2.visibility = View.GONE

        supportActionBar?.hide()
        imageView1.setOnClickListener { doSomething() }




        //////////sound///////////
        mp_volumeUpKey2 = MediaPlayer.create(this,R.raw.up2)
        mp_volumeDownKey2 = MediaPlayer.create(this,R.raw.down2)

        /////Service
        context = getApplicationContext()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context,  ContinueService::class.java))
        } else {
            context!!.startService(Intent(context,  ContinueService::class.java))
        }

    }
    private fun doSomething() {
        imageView1.visibility=View.GONE
        imageView2.visibility = View.VISIBLE
    }


    object Mp_powerlevel {
        var  mp_powerHigh : MediaPlayer? = null
        var  mp_powerMid: MediaPlayer? = null
        var  mp_powerLow: MediaPlayer? = null

    }
    object Mp_charging {
        var mp_charging : MediaPlayer? = null

    }




}
