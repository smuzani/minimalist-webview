package com.webview

import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.webview.designSystem.theme.TemplateTheme

class MainActivity : ComponentActivity() {

  private var url by mutableStateOf("https://www.kagi.com")
  private var observedUrl by mutableStateOf(url)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TemplateTheme {
        Column {
          Row {
            TextField(
              value = url,
              onValueChange = { url = it },
              modifier = Modifier
                .weight(1f)
                .padding(4.dp)
            )
            Button(onClick = { observedUrl = url }) {
              Text("Go")
            }
          }
          WebViewContainer(url = observedUrl)
        }
      }
    }
  }
}

@Composable
fun WebViewContainer(url: String) {
  var webView: WebView? by remember { mutableStateOf(null) }

  AndroidView(
    modifier = Modifier.fillMaxSize(),
    factory = { context ->
      WebView(context).apply {
        webView = this
        WebView.setWebContentsDebuggingEnabled(true)
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true

        webViewClient = object : WebViewClient() {
          override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
          }

          override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
          ) {
            super.onReceivedError(view, request, error)
            // Log error details
            error?.let {
              Log.e("WebViewError", "Error: ${it.description}")
            }
          }
        }
      }
    }
  )

  // Reload the WebView whenever the URL changes
  LaunchedEffect(url) {
    webView?.loadUrl(url)
  }
}
