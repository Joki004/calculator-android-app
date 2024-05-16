package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat


class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val descriptionTextView: TextView = findViewById(R.id.Description)
        val htmlDescription = getString(R.string.app_description)
        descriptionTextView.text = HtmlCompat.fromHtml(htmlDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("SimpleCalculator", "Landscape orientation")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("SimpleCalculator", "Portrait orientation")
        }


    }
}