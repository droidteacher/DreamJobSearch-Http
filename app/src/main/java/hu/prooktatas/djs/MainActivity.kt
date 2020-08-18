package hu.prooktatas.djs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var cb1: CheckBox      // full time
    private lateinit var cb2: CheckBox      // part time
    private lateinit var cb3: CheckBox      // remote
    private lateinit var cb4: CheckBox      // contractor
    private lateinit var cb5: CheckBox      // freelancer

    private lateinit var etName: EditText
    private lateinit var etPosition: EditText

    private lateinit var button: Button

    private val workPrefs: Int
        get() {
            val v0 = when (cb1.isChecked) {
                true -> 1
                else -> 0
            }

            val v1 = when (cb2.isChecked) {
                true -> 2
                else -> 0
            }

            val v2 = when (cb3.isChecked) {
                true -> 4
                else -> 0
            }

            val v3 = when (cb4.isChecked) {
                true -> 8
                else -> 0
            }

            val v4 = when (cb5.isChecked) {
                true -> 16
                else -> 0
            }

            return arrayOf(v0, v1, v2, v3, v4).sum()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")

        cb1 = findViewById(R.id.cbFullTime)
        cb2 = findViewById(R.id.cbPartTime)
        cb3 = findViewById(R.id.cbRemote)
        cb4 = findViewById(R.id.cbContractor)
        cb5 = findViewById(R.id.cbFreelancer)

        etName = findViewById(R.id.etName)
        etPosition = findViewById(R.id.etPosition)

        button = findViewById(R.id.btnSubmit)

        button.setOnClickListener {
            saveAppData()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        loadAppData()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
//        saveAppData()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState")
        loadAppData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        saveAppData()
    }

    private fun loadAppData() {
        Log.d(TAG, "loadAppData() called")

        val prefs = getPreferences(Context.MODE_PRIVATE)

        prefs.getString(PREF_KEY_APPLICANT_NAME, null)?.let {
            etName.setText(it)
        }

        prefs.getString(PREF_KEY_PREFERRED_POSITION, null)?.let {
            etPosition.setText(it)
        }

        prefs.getInt(PREF_KEY_WORK_PREFS, 0).also {
            decodeWorkPrefs(it)
        }
    }

    private fun saveAppData() {
        Log.d(TAG, "saveAppData() called")

        //        val editor = getPreferences(Context.MODE_PRIVATE).edit()

        with(getPreferences(Context.MODE_PRIVATE).edit()) {
            Log.d(TAG, "workPrefs value: $workPrefs")
            putInt(PREF_KEY_WORK_PREFS, workPrefs)

            if (etName.text.isNotEmpty()) {
                putString(PREF_KEY_APPLICANT_NAME, etName.text.toString())
            }

            if (etPosition.text.isNotEmpty()) {
                putString(PREF_KEY_PREFERRED_POSITION, etPosition.text.toString())
            }

            commit()
        }
    }

    private fun decodeWorkPrefs(value: Int) {
        Log.d(TAG, "decodeWorkPrefs() called with $value")

        listOf(cb1, cb2, cb3, cb4, cb5).forEach {
            it.isChecked = false
        }

        val checkBoxes: List<CheckBox> = when (value) {
            1 -> listOf(cb1)
            2 -> listOf(cb2)
            3 -> listOf(cb1, cb2)
            4 -> listOf(cb3)
            5 -> listOf(cb1, cb3)
            6 -> listOf(cb2, cb3)
            7 -> listOf(cb1, cb2, cb3)
            8 -> listOf(cb4)
            9 -> listOf(cb1, cb4)
            10 -> listOf(cb2, cb4)
            11 -> listOf(cb1, cb2, cb4)
            12 -> listOf(cb3, cb4)
            13 -> listOf(cb1, cb3, cb4)
            14 -> listOf(cb2, cb3, cb4)
            15 -> listOf(cb1, cb2, cb3, cb4)
            16 -> listOf(cb5)
            17 -> listOf(cb1, cb5)
            18 -> listOf(cb2, cb5)
            19 -> listOf(cb1, cb2, cb5)
            20 -> listOf(cb3, cb5)
            21 -> listOf(cb1, cb3, cb5)
            22 -> listOf(cb2, cb3, cb5)
            23 -> listOf(cb1, cb2, cb3, cb5)
            24 -> listOf(cb4, cb5)
            25 -> listOf(cb1, cb4, cb5)
            26 -> listOf(cb2, cb4, cb5)
            27 -> listOf(cb1, cb2, cb4, cb5)
            28 -> listOf(cb3, cb4, cb5)
            29 -> listOf(cb1, cb3, cb4, cb5)
            30 -> listOf(cb2, cb3, cb4, cb5)
            31 -> listOf(cb1, cb2, cb3, cb4, cb5)
            else -> emptyList()
        }

        checkBoxes.forEach {
            it.isChecked = true
        }
    }
}

const val TAG = "KZs"
const val PREF_KEY_WORK_PREFS = "workPrefs"
const val PREF_KEY_APPLICANT_NAME = "applicantName"
const val PREF_KEY_PREFERRED_POSITION = "preferredPosition"

// TODO: nezzuk meg ADB-ben a futo prooktats app-ok folyamtait: adb shell ps | grep hu.prooktatas

// TODO: kuldjunk trim memory parancsot az appnak: adb shell am send-trim-memory hu.prooktatas.djs MODERATE (onTrimMemory metodust felul kell irni hozza)

// TODO: lojuk ki ADB-bol az app-ot es figyeljuk meg, hogy menti-e az adatokat: adb shell am kill hu.prooktatas.djs

// https://stackoverflow.com/questions/8710652/android-simulator-easy-way-to-simulate-a-process-restart-due-to-low-memory
// https://stackoverflow.com/questions/5287237/simulate-killing-of-activity-in-emulator
// https://stackoverflow.com/questions/3656594/simulate-low-battery-low-memory-in-android

// Hasznos ADB parancsok: https://gist.github.com/Pulimet/5013acf2cd5b28e55036c82c91bd56d8

// Shared Preferences hasznalata: https://developer.android.com/training/data-storage/shared-preferences