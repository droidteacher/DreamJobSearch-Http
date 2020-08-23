package hu.prooktatas.djs.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import hu.prooktatas.djs.R
import hu.prooktatas.djs.fragment.PhotoFragment
import hu.prooktatas.djs.fragment.PreferredLoacationFragment
import hu.prooktatas.djs.fragment.WorkPreferencesFragment
import hu.prooktatas.djs.model.UserPreferences

class TabContentAdapter(private val ctx: Context, private val prefs: UserPreferences, mgr: FragmentManager): FragmentStatePagerAdapter(mgr) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> WorkPreferencesFragment.newInstance(prefs)
            1 -> PhotoFragment.newInstance()
            else -> PreferredLoacationFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arrayOf(
            ctx.resources.getString(R.string.tab_1),
            ctx.resources.getString(R.string.tab_3),
            ctx.resources.getString(R.string.tab_2)

        )[position]
    }
}