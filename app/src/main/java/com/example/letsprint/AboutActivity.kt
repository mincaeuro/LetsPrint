package com.example.letsprint

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val actionbar = supportActionBar
        actionbar!!.title = "About me"

        actionbar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
