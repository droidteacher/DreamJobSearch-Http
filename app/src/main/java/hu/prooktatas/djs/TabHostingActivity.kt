package hu.prooktatas.djs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import hu.prooktatas.djs.adapter.TabContentAdapter
import hu.prooktatas.djs.fragment.PhotoFragment
import hu.prooktatas.djs.fragment.PreferredLoacationFragment
import hu.prooktatas.djs.fragment.WorkPreferencesFragment
import hu.prooktatas.djs.model.UserPreferences

class TabHostingActivity : AppCompatActivity() {

    private lateinit var fragmentWorkPrefs: WorkPreferencesFragment
    private lateinit var fragmentPhoto: PhotoFragment
    private lateinit var fragmentLocation: PreferredLoacationFragment

    private lateinit var btnSearch: Button
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    var userPrefs = UserPreferences("John Doe", "CEO", 31)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_hosting)

        btnSearch = findViewById(R.id.btnSearch)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = TabContentAdapter(this, userPrefs, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        btnSearch.setOnClickListener {
            searchForDreamJobs()
        }
    }

    private fun searchForDreamJobs() {
        Log.d(TAG, "searchForDreamJobs() NOT IMPLEMENTED yet...")
    }
}

const val TAG = "KZs"
const val PREF_KEY_WORK_PREFS = "workPrefs"
const val PREF_KEY_APPLICANT_NAME = "applicantName"
const val PREF_KEY_PREFERRED_POSITION = "preferredPosition"

// TODO: nezzuk meg ADB-ben a futo prooktatas app-ok folyamtait: adb shell ps | grep hu.prooktatas

// TODO: kuldjunk trim memory parancsot az appnak: adb shell am send-trim-memory hu.prooktatas.djs MODERATE (onTrimMemory metodust felul kell irni hozza)

// TODO: lojuk ki ADB-bol az app-ot es figyeljuk meg, hogy menti-e az adatokat: adb shell am kill hu.prooktatas.djs

// https://stackoverflow.com/questions/8710652/android-simulator-easy-way-to-simulate-a-process-restart-due-to-low-memory
// https://stackoverflow.com/questions/5287237/simulate-killing-of-activity-in-emulator
// https://stackoverflow.com/questions/3656594/simulate-low-battery-low-memory-in-android
