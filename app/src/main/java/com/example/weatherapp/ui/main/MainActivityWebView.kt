package com.example.weatherapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainWebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {

    lateinit var binding: ActivityMainWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.okButton.setOnClickListener {
            val urlText = binding.editTextUrl.text.toString()
            //binding.webView.loadUrl(urlText)
            val uri = URL(urlText)
            //val handler = Handler()  //Запоминаем основной поток
            val urlConnection: HttpsURLConnection =
                (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                }
            Thread {
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = getLinesAsOneBigString(buffer)
                runOnUiThread {
                    // Возвращаемся к основному потоку
                    //handler.post {binding.webView.loadDataWithBaseURL(null, result, "text/html; utf-8", "utf-8", null)}
                    binding.webView.loadDataWithBaseURL(
                        null,
                        result,
                        "text/html; utf-8",
                        "utf-8", null
                    )
                }
            }.start()
        }
    }

    private fun getLinesAsOneBigString(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
}