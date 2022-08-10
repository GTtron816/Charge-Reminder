package com.gttron.yukino.chsr

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.gttron.yukino.chsr.NConst.playing

import com.gttron.yukino.chsr.NConst.ring

class MainActivity : AppCompatActivity() {
    var chargevalue = 80

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)


        val lsbar=findViewById<SeekBar>(R.id.blimit)
        val t=findViewById<TextView>(R.id.t)
        lsbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val pr = progress.toString()
                t.text = pr
                savechargelimit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }
        })



        setcurrentlimit()
        savechargelimit()



        val start = findViewById<Button>(R.id.st)
        start.setOnClickListener {

            if(CheckServiceRunning(ChargeRService::class.java)){
                Toast.makeText(this,"Service is running",Toast.LENGTH_LONG).show()

            }

            else {

                Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show()

                startService(Intent(this, ChargeRService::class.java))
            }

        }
        val stop = findViewById<Button>(R.id.stop)
        stop.setOnClickListener{



            if(!CheckServiceRunning(ChargeRService::class.java)){
                Toast.makeText(this,"Service is not running",Toast.LENGTH_LONG).show()}



              else {
                Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show()
                stopService(Intent(this, ChargeRService::class.java))
                   if(playing==1)
                   {
                    ring.stop()
                    ring.release()
                    playing = 0}

            }






        }



    }


    private fun CheckServiceRunning(mClass: Class<ChargeRService>):Boolean
    {
        val manager: ActivityManager= getSystemService(
            Context.ACTIVITY_SERVICE
        )as ActivityManager

        for(service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(mClass.name.equals((service.service.className))){
                return true
            }


        }
           return false

    }



    private fun setcurrentlimit() {
        var limitS=""
        val t= findViewById<TextView>(R.id.t)
        val sharedPreferences=getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
        val charge=sharedPreferences.getInt("charge",80)

        limitS=charge.toString()
            t.setText(limitS)



    }

    private fun savechargelimit() {
        val t=findViewById<TextView>(R.id.t)
        chargevalue=t.text.toString().toInt()
        val sharedPreferences=getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.apply{
            putInt("charge",chargevalue)
        }.apply()
    }




}

