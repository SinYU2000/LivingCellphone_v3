package nthu.ipta.madp.livingcellphone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

import nthu.ipta.madp.livingcellphone.ContinueService.Mp_compass.mp_compass
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_booting.mp_booting



class ContinueService : Service() {

    val TAG="ContinueService"

    private var mp_shake: MediaPlayer? = null


    private var receiver_charging:PowerConnectionReceiver?=null
    private var intentFilter_charging: IntentFilter? = null
    private var receiver_batterylevel:BatteryLevelReceiver?=null
    private var intentFilter_batterylevel: IntentFilter? = null

    private var compass: Compass? = null
    private var currentAzimuth = 0f

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val num = (0..2).random()
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "my_channel_01"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification =
                NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build()
            startForeground(1, notification)
        }
        BatteryLevelReceiver()
        Log.i(TAG,"================= BatteryServiceReceiver===============")

        /////////BatteryConnect//////
        if(num==1)
        {MainActivity.Mp_charging.mp_charging = MediaPlayer.create(this,R.raw.charge1)}
        if(num==0)
        {MainActivity.Mp_charging.mp_charging = MediaPlayer.create(this,R.raw.charge2)}
        receiver_charging =PowerConnectionReceiver()
        intentFilter_charging = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver_charging,intentFilter_charging)


        ///////////PowerLevel//////////
        if(num==1) {
            MainActivity.Mp_powerlevel.mp_powerHigh=
                MediaPlayer.create(this, R.raw.high1)
            MainActivity.Mp_powerlevel.mp_powerMid= MediaPlayer.create(this, R.raw.mid1)
            MainActivity.Mp_powerlevel.mp_powerLow= MediaPlayer.create(this, R.raw.low1)
        }
        if(num==0) {
            MainActivity.Mp_powerlevel.mp_powerHigh=
                MediaPlayer.create(this, R.raw.charging_high2)
            MainActivity.Mp_powerlevel.mp_powerMid = MediaPlayer.create(this, R.raw.charging_mid2)
            MainActivity.Mp_powerlevel.mp_powerLow= MediaPlayer.create(this, R.raw.charging_low2)
        }
        receiver_batterylevel = BatteryLevelReceiver()
        intentFilter_batterylevel = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver_batterylevel,intentFilter_batterylevel)


        //////////Shake///////////
        if(num==1) {
            mp_shake = MediaPlayer.create(this, R.raw.screech1)
        }
        if(num==0) {
            mp_shake = MediaPlayer.create(this, R.raw.screech2)
        }
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(shakeSensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)


        ////////compass//////
        if(num==1) {
            mp_compass = MediaPlayer.create(this, R.raw.screech1)
        }
        if(num==0) {
            mp_compass = MediaPlayer.create(this, R.raw.screech2)
        }
        setupCompass()

        ////////booting
        if(num==1) {
            mp_booting = MediaPlayer.create(this, R.raw.cry)
        }
        if(num==0) {
            mp_booting = MediaPlayer.create(this, R.raw.cry)
        }



    }


    private val shakeSensorListener = object: SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                val xValue = Math.abs(event.values[0]) // 加速度 - X 軸方向
                val yValue = Math.abs(event.values[1]) // 加速度 - Y 軸方向
                val zValue = Math.abs(event.values[2]) // 加速度 - Z 軸方向
                if (xValue > 20 || yValue > 20 || zValue > 20) {
                    mp_shake?.start()
                }
            }
        }
    }

    private fun setupCompass() {
        compass = Compass(this)
        val cl = compassListener
        compass!!.setListener(cl)
    }

    object Mp_compass{
        var mp_compass: MediaPlayer? = null

    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth)
        val an: Animation = RotateAnimation(-currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f)
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
    }

    private val compassListener: Compass.CompassListener
        private get() = object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {

//                runOnUiThread {
//                   adjustArrow(azimuth)
//                }
            }
        }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(TAG,"=================BatteryLevelService==============")
        return START_STICKY
    }


}