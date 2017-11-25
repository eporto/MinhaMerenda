package com.tac.marcos.minhamerenda

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

/**
 * Created by Marcos on 18/11/2017.
 */

class HelperActivity : Activity() {
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.helper_activity)

        prefs = getSharedPreferences("mypref", Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    override fun onResume() {
        super.onResume()

        if (prefs!!.getBoolean("firststart", true)) {
            //Do first Run Stuff
            Log.i("TAG", "Primeira vez")

            prefs!!.edit().putBoolean("firststart", false).commit()
            startActivity(Intent(this, FirstStartTabOne::class.java))
            finish()
        }
        else{
            Log.i("TAG", "Não é primeira vez")
            startActivity(Intent(this, FirstStartTabTwo::class.java))
//            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            finish()
        }
    }
}
