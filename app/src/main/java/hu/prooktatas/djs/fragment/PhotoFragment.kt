package hu.prooktatas.djs.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.prooktatas.djs.R
import hu.prooktatas.djs.TAG


class PhotoFragment : Fragment() {

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_photo, container, false)
        fab = rootView.findViewById(R.id.fab)
        fab.setOnClickListener {
            Log.d(TAG, "FAB clicked...")
        }

        return rootView
    }

    companion object {

        @JvmStatic
        fun newInstance() = PhotoFragment()
    }
}