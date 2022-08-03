package com.gttron.yukino.chsr

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.gttron.yukino.chsr.NConst.playing

import com.gttron.yukino.chsr.NConst.ring

class MainActivity : AppCompatActivity() {
    var chargevalue = 80

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val cl=findViewById<EditText>(R.id.charge)
        val ch=cl.text
        val t=findViewById<TextView>(R.id.t)
        setcurrentlimit()
        savechargelimit()


        val savebt=findViewById<Button>(R.id.limitsave)
        savebt.setOnClickListener {
            val dbox = findViewById<TextInputLayout>(R.id.box)
            if (cl.text?.length == 0) {
                cl.error = "Field cannot be empty"
                dbox.helperText = "Field cannot be empty"

            } else {

                t.setText(ch)
                savechargelimit()
            }
        }

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
                Toast.makeText(this,"Service is not running",Toast.LENGTH_LONG).show()

            }

            else {
                Toast.makeText(this,"Service stopped",Toast.LENGTH_LONG).show()
                stopService(Intent(this, ChargeRService::class.java))
                if(playing==1)
                {
                    ring.stop()
                }



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

