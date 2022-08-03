package com.gttron.yukino.chsr

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.gttron.yukino.chsr.NConst.CHANNEL_ID
import com.gttron.yukino.chsr.NConst.NOTIFICATION_ID
import com.gttron.yukino.chsr.NConst.blevel
import com.gttron.yukino.chsr.NConst.ring


class ChargeRService : Service() {
    lateinit var chargeval:String


    override fun onBind(p0: Intent?): IBinder? {

        return null
    }





    override fun onCreate() {
        super.onCreate()
        registerReceiver(this.mBatteryInfoReciver,IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        loadcharge()

        createNotificationChannel()

    }
private val mBatteryInfoReciver: BroadcastReceiver = object : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        val scale = intent!!.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
       val batteryPct = level * 100 / scale.toDouble()

        blevel= batteryPct.toInt()
        val bchk = blevel.toString()


        if (bchk == chargeval)
        {
            Toast.makeText(applicationContext, "Charge Level=$bchk", Toast.LENGTH_LONG).show()
            notif()
        }

    }
}
    fun notif(){
        ring = MediaPlayer.create(this, R.raw.bs)
        ring.isLooping=false
        ring.start()
        unregisterReceiver(mBatteryInfoReciver)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        showNotification()


        return START_STICKY
    }


    private fun showNotification() {
       val notificationIntent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,0,notificationIntent,0
        )

        val notification= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification
                .Builder(this, CHANNEL_ID)
                .setContentText("Charge Reminder Service")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        startForeground(NOTIFICATION_ID, notification)

    }





   private fun createNotificationChannel(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

         val serviceChannel = NotificationChannel(
             CHANNEL_ID, "Charge Reminder",
             NotificationManager.IMPORTANCE_DEFAULT
         )

        val manager = getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(serviceChannel)

    }


}





    private fun loadcharge() {
        val sharedPreferences=getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
        val charge=sharedPreferences.getInt("charge",80)
        chargeval=charge.toString()


    }



}




