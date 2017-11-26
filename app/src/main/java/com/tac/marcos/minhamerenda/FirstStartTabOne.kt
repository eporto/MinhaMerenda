package com.tac.marcos.minhamerenda

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity


class FirstStartTabOne : AppCompatActivity() {
    private var btnNext: FloatingActionButton? = null
    private var appKey = hashCode().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_start_tab1)
        btnNext = findViewById(R.id.btntab1)



        btnNext!!.setOnClickListener{
            startActivity(Intent(this, FirstStartTabTwo::class.java))
//            view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
        }

    }
}
