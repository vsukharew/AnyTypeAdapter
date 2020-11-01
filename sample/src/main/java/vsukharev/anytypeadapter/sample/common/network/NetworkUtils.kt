package vsukharev.anytypeadapter.sample.common.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Makes simple request
 */
suspend fun makeSimpleRequest() {
    val url = URL("https://www.google.com/")
    supervisorScope {
        withContext(Dispatchers.IO) {
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 60 * 1000
            try {
                BufferedReader(urlConnection.inputStream.reader())
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}