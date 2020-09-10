package hu.prooktatas.djs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hu.prooktatas.djs.adapter.TabContentAdapter
import hu.prooktatas.djs.fragment.PreferredLoacationFragment
import hu.prooktatas.djs.fragment.WorkPreferencesFragment
import hu.prooktatas.djs.model.JobSearchResult
import hu.prooktatas.djs.model.UserPreferences
import hu.prooktatas.djs.networking.FetchJobSearch
import hu.prooktatas.djs.networking.JobResultCallback
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class TabHostingActivity : AppCompatActivity(), JobResultCallback {

//    private lateinit var fragmentWorkPrefs: WorkPreferencesFragment
//    private lateinit var fragmentPhoto: PhotoFragment
//    private lateinit var fragmentLocation: PreferredLoacationFragment

    private lateinit var btnSearch: Button
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private lateinit var tabContentAdapter: TabContentAdapter

    private val okHttpClient = OkHttpClient()

    var userPrefs = UserPreferences("John Doe", "CEO", 31)

    val httpGetParam_Description = "description"
    val httpGetParam_Location = "location"
    val httpGetParam_Latitude = "lat"
    val httpGetParam_Longitude = "long"
    val httpGetParam_FullTime = "full_time"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_hosting)

        btnSearch = findViewById(R.id.btnSearch)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        tabContentAdapter = TabContentAdapter(this, userPrefs, supportFragmentManager)
        viewPager.adapter = tabContentAdapter
        tabLayout.setupWithViewPager(viewPager)

        btnSearch.setOnClickListener {
            searchForDreamJobs()
        }
    }

    override fun consumeRawResponse(response: String) {
//        Log.d(TAG, "Raw response: $response")

        val typeForDesarialization: Type = object : TypeToken<List<JobSearchResult>>() {}.type
        val list = Gson().fromJson<List<JobSearchResult>>(response, typeForDesarialization)

        Log.d(TAG, "list: $list")

        list.forEach {
            Log.d(TAG, "${it.title}: ${it.location}")
        }

        Intent(this, JobListActivity::class.java).also {
            it.putExtra(EXTRA_PARAM_SEARCH_RESULTS, list as Serializable)
            startActivity(it)
        }
    }



    private fun searchForDreamJobs() {
//        simpleFetch()

//        fetchUsingAsyncTaskDefaultUrl()

//        fetchUsingAsyncTaskWithFakeParams()

//        fetchUsingAsyncTaskWithUserInput()

//        fetchUsingOKHttpSampleSync()

//        fetchUsingOKHttpSampleAsync()

        fetchUsingOKHttpAsyncWithUserInput()

//        fetchUsingSpringBootBackendLocalhost()

    }


    // Ez kivetelt dob: main thread-en nem hivunk halozati operaciot vegzo kodot!
    private fun simpleFetch() {
        val url = URL(URL_JSON_RESPONSE_TYPE)
        val conn = url.openConnection() as HttpURLConnection

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

        Log.d(TAG, "response JSON: ${rawResponse.toString()}")
    }

    private fun fetchUsingAsyncTaskDefaultUrl() {
        FetchJobSearch(URL(URL_JSON_RESPONSE_TYPE), this).execute()
    }

    private fun fetchUsingAsyncTaskWithFakeParams() {

        /*
             Parameters

                description — A search term, such as "ruby" or "java". This parameter is aliased to search.
                location — A city name, zip code, or other location search term.
                lat — A specific latitude. If used, you must also send long and must not send location.
                long — A specific longitude. If used, you must also send lat and must not send location.
                full_time — If you want to limit results to full time positions set this parameter to 'true'.

         */

//        val params = mapOf(
//            getParamDescription to "python",
//            getParamLocation to "Chicago",
//            getParamFullTime to "true"
//        )

        val params = mapOf(
            httpGetParam_Description to "kotlin"
        )

        val url = URLBuilder.createUrl(params)

        Log.d(TAG, "Builder created URL: $url")

        FetchJobSearch(url, this).execute()
    }

    private fun fetchUsingAsyncTaskWithUserInput() {
        val fragmentWorkPrefs = tabContentAdapter.getItem(0) as WorkPreferencesFragment
        val locationFragment = tabContentAdapter.getItem(2) as PreferredLoacationFragment

        val position = fragmentWorkPrefs.userInputPosition ?: ""
        val fullTime = fragmentWorkPrefs.isFullTimeAcceptable.toString()
        val preferredLocation = locationFragment.preferredLocation ?: ""


        val url = URLBuilder.createUrl(mapOf(
            httpGetParam_Description to position,
            httpGetParam_FullTime to fullTime,
            httpGetParam_Location to preferredLocation
        ))

        Log.d(TAG, "Builder created URL: $url")

        FetchJobSearch(url, this).execute()

    }

    // szinkron hivas!!! hibat dob!
    private fun fetchUsingOKHttpSampleSync() {
        val request = Request.Builder()
            .url("https://publicobject.com/helloworld.txt")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                Log.d(TAG, "$name: $value")
            }

            Log.d(TAG, response.body!!.string())
        }
    }

    private fun fetchUsingOKHttpSampleAsync() {
        Log.d(TAG, "fetchUsingOKHttpSampleAsync")
        val request = Request.Builder()
            .url("https://publicobject.com/helloworld.txt")
            .build()

        Log.d(TAG, "request: $request")

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        Log.d(TAG, "$name: $value")
                    }

                    Log.d(TAG, response.body!!.string())
                }
            }
        })
    }

    // Request url parameterezese: https://stackoverflow.com/questions/30142626/how-to-add-query-parameters-to-a-http-get-request-by-okhttp
    private fun fetchUsingOKHttpAsyncWithUserInput() {
        val fragmentWorkPrefs = tabContentAdapter.getItem(0) as WorkPreferencesFragment
        val locationFragment = tabContentAdapter.getItem(2) as PreferredLoacationFragment

        val position = fragmentWorkPrefs.userInputPosition ?: ""
        val fullTime = fragmentWorkPrefs.isFullTimeAcceptable.toString()
        val preferredLocation = locationFragment.preferredLocation ?: ""



//        val urlBuilder = BASE_URL.toHttpUrlOrNull()?.newBuilder()

        val urlBuilder = BASE_URL.toHttpUrlOrNull()?.newBuilder()?.also {
            it.addQueryParameter(httpGetParam_Description, position)
            it.addQueryParameter(httpGetParam_FullTime, fullTime)
            it.addQueryParameter(httpGetParam_Location, preferredLocation)
        }

        val fullyConfiguredUrl = urlBuilder?.let {
            it.build()
        }

        val request = fullyConfiguredUrl?.let {
            Request.Builder().url(it).build()
        }


        request?.let {

            Log.d(TAG, "request: $it")

            okHttpClient.newCall(it).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            Log.d(TAG, "$name: $value")
                        }

//                        Log.d(TAG, response.body!!.string())

                        consumeRawResponse(response.body!!.string())
                    }
                }
            })

        }

    }

    /**
     * FONTOS!!!
     *
     * Ahhoz, hogy ez mukodjon, az AndroidManifest.xml-ben az application elemhez hozza kell adni a kovetkezo attributumot:
     *
     * android:usesCleartextTraffic="true"
     */
    private fun fetchUsingSpringBootBackendLocalhost() {
        Log.d(TAG, "Trying to call Spring Bot backend...")
        val url  = URL("http://10.0.2.2:8080/jobs")

        FetchJobSearch(url, this).execute()
    }



    object URLBuilder {

        fun createUrl(params: Map<String, String>): URL {
            return StringBuilder(BASE_URL).apply {
                var first = true
                params.forEach {
                    if (!first) {
                        append("&")
                    }

                    append(it.key).append("=").append(URLEncoder.encode(it.value))
                    first = false
                }

            }.run {
                URL(toString())
            }
        }
    }
}

const val TAG = "KZs"
const val PREF_KEY_WORK_PREFS = "workPrefs"
const val PREF_KEY_APPLICANT_NAME = "applicantName"
const val PREF_KEY_PREFERRED_POSITION = "preferredPosition"

const val URL_JSON_RESPONSE_TYPE = "https://jobs.github.com/positions.json?description=python&location=new+york"

const val BASE_URL = "https://jobs.github.com/positions.json?"

// TODO: nezzuk meg ADB-ben a futo prooktatas app-ok folyamatait: adb shell ps | grep hu.prooktatas

// TODO: kuldjunk trim memory parancsot az appnak: adb shell am send-trim-memory hu.prooktatas.djs MODERATE (onTrimMemory metodust felul kell irni hozza)

// TODO: lojuk ki ADB-bol az app-ot es figyeljuk meg, hogy menti-e az adatokat: adb shell am kill hu.prooktatas.djs

// https://stackoverflow.com/questions/8710652/android-simulator-easy-way-to-simulate-a-process-restart-due-to-low-memory
// https://stackoverflow.com/questions/5287237/simulate-killing-of-activity-in-emulator
// https://stackoverflow.com/questions/3656594/simulate-low-battery-low-memory-in-android
