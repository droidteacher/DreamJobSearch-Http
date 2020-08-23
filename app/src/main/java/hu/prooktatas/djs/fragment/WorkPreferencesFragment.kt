package hu.prooktatas.djs.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import hu.prooktatas.djs.*
import hu.prooktatas.djs.model.UserPreferences


private const val ARG_PARAM1 = "prefs"

class WorkPreferencesFragment : Fragment() {

    private var userPreferences: UserPreferences? = null

    private lateinit var cb1: CheckBox      // full time
    private lateinit var cb2: CheckBox      // part time
    private lateinit var cb3: CheckBox      // remote
    private lateinit var cb4: CheckBox      // contractor
    private lateinit var cb5: CheckBox      // freelancer

    private lateinit var etName: EditText
    private lateinit var etPosition: EditText

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
        arguments?.let {
            userPreferences = it.getSerializable(ARG_PARAM1) as? UserPreferences
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_work_preferences, container, false)

        cb1 = rootView.findViewById(R.id.cbFullTime)
        cb2 = rootView.findViewById(R.id.cbPartTime)
        cb3 = rootView.findViewById(R.id.cbRemote)
        cb4 = rootView.findViewById(R.id.cbContractor)
        cb5 = rootView.findViewById(R.id.cbFreelancer)

        etName = rootView.findViewById(R.id.etName)
        etPosition = rootView.findViewById(R.id.etPosition)

        return rootView
    }

    private fun loadAppData() {
        Log.d(TAG, "loadAppData() called")

        val prefs = activity!!.getPreferences(Context.MODE_PRIVATE)

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

        with(activity!!.getPreferences(Context.MODE_PRIVATE).edit()) {
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

    companion object {

        @JvmStatic
        fun newInstance(prefs: UserPreferences? = null) =
            WorkPreferencesFragment().apply {

                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, prefs)
                }
            }
    }
}

