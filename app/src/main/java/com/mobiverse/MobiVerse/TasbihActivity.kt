package com.mobiverse.MobiVerse

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasbihActivity : AppCompatActivity() {

    private lateinit var tvTasbihCount: TextView
    private lateinit var btnTasbihCount: Button
    private lateinit var fabResetTasbih: FloatingActionButton
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbih)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_tasbih)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tvTasbihCount = findViewById(R.id.tv_tasbih_count)
        btnTasbihCount = findViewById(R.id.btn_tasbih_count)
        fabResetTasbih = findViewById(R.id.fab_reset_tasbih)

        btnTasbihCount.setOnClickListener {
            count++
            tvTasbihCount.text = count.toString()
            vibrate()
        }

        fabResetTasbih.setOnClickListener {
            count = 0
            tvTasbihCount.text = count.toString()
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Deprecated in API 26
            vibrator.vibrate(50)
        }
    }
}
