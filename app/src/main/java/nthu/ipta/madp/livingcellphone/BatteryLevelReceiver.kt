package nthu.ipta.madp.livingcellphone

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_powerlevel.mp_powerHigh
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_powerlevel.mp_powerLow
import nthu.ipta.madp.livingcellphone.MainActivity.Mp_powerlevel.mp_powerMid


class BatteryLevelReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?){

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context?.registerReceiver(null, ifilter)
        }
        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        Log.i(TAG, batteryPct.toString());

        when (batteryPct) {
            90.0f ->  mp_powerHigh?.start()

            50.0f ->  mp_powerMid?.start();
            15.0f ->  mp_powerLow?.start();
            else -> { // 注意这个块
                mp_powerHigh?.stop()
                mp_powerMid?.stop();
                mp_powerLow?.stop();
            }
        }

    }
}