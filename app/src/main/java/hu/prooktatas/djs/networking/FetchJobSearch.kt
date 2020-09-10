package hu.prooktatas.djs.networking

import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

interface JobResultCallback {
    fun consumeRawResponse(response: String)
}

class FetchJobSearch(private val serviceUrl: URL, private val callback: JobResultCallback): AsyncTask<Unit, Unit, String>() {
    override fun doInBackground(vararg params: Unit?): String {

        val conn = serviceUrl.openConnection() as HttpURLConnection

        conn.requestMethod = "GET"
        val reader = conn.inputStream.bufferedReader()

        var goOn = true

        val rawResponse = StringBuffer()

        while(goOn) {
            val line = reader.readLine()
            if (line != null) {
                rawResponse.append(line)
            } else {
                goOn = false
            }

        }

        return rawResponse.toString()
    }

    override fun onPostExecute(result: String?) {
        result?.let {
            callback.consumeRawResponse(it)
        }

    }
}