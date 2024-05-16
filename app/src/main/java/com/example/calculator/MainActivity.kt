package com.example.calculator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonSimple: Button = findViewById(R.id.buttonSimple)
        val buttonAdvanced: Button = findViewById(R.id.buttonAdvanced)
        val buttonAbout: Button = findViewById(R.id.buttonAbout)
        val buttonExit: Button = findViewById(R.id.buttonExit)
        buttonSimple.setOnClickListener {
            val intent = Intent(this, SimpleCalculator::class.java)
            startActivity(intent)
        }

        buttonAdvanced.setOnClickListener {
            val intent = Intent(this, AdvancedCalculator::class.java)
            startActivity(intent)
        }
        buttonAbout.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }
        buttonExit.setOnClickListener {
            finishAffinity()
        }

    }
}

