package com.example.weatherapp.ui.experiments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class APBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        StringBuilder().apply {
            append("СООБЩЕНИЕ ОТ СИСТЕМЫ\n")
            if (intent != null) {
                append("Action: ${intent.action}")
            }
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

    }
}