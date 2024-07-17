package com.example.videoviewapp

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlEditText: EditText
    private lateinit var playButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        urlEditText = findViewById(R.id.urlEditText)
        playButton = findViewById(R.id.playButton)

        // Configure WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                Toast.makeText(this@MainActivity, "Error loading video: ${error.description}", Toast.LENGTH_SHORT).show()
                Log.e("VideoViewApp", "Error loading video: ${error.description}")
            }
        }
        webView.webChromeClient = WebChromeClient()

        playButton.setOnClickListener {
            val videoUrl = urlEditText.text.toString()
            if (videoUrl.isNotEmpty()) {
                playVideo(videoUrl)
            } else {
                Toast.makeText(this, "Please enter a video URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playVideo(videoUrl: String) {
        if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) {
            val embeddedUrl = getEmbeddedYouTubeUrl(videoUrl)
            webView.loadUrl(embeddedUrl)
        } else {
            webView.loadUrl(videoUrl)
        }
    }

    private fun getEmbeddedYouTubeUrl(videoUrl: String): String {
        return if (videoUrl.contains("youtube.com")) {
            videoUrl.replace("watch?v=", "embed/")
        } else {
            val videoId = videoUrl.substringAfterLast("/")
            "https://www.youtube.com/embed/$videoId"
        }
    }
}
