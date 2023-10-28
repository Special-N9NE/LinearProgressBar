package org.nine.linearprogressbar

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pb = findViewById<LinearProgressBar>(R.id.pb)

        pb.setProgressColor(Color.RED)
        Handler(mainLooper).postDelayed({
            pb.setProgress(80)
        }, 5000)
    }
}