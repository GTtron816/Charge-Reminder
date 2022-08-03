package com.gttron.yukino.chsr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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



        val savebt=findViewById<Button>(R.id.limitsave)
        savebt.setOnClickListener { t.setText(ch)
            savechargelimit() }

        val start = findViewById<Button>(R.id.st)
        start.setOnClickListener {

            startService(Intent(this, ChargeRService::class.java))


        }
        val stop = findViewById<Button>(R.id.stop)
        stop.setOnClickListener{
            stopService(Intent(this,ChargeRService::class.java))
            ring.stop()
        }



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

