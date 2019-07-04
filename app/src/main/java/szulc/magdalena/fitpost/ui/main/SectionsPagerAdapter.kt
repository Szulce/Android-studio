package szulc.magdalena.fitpost.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import szulc.magdalena.fitpost.R
import szulc.magdalena.fitpost.ui.main.fragments.MapFragment
import szulc.magdalena.fitpost.ui.main.fragments.MasotodonFragment
import szulc.magdalena.fitpost.ui.main.fragments.TimerFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0->{
                return  MapFragment.newInstance(position+1)
            }
            1->{

                return MasotodonFragment.newInstance(position+1)
            }
            2->{
                return TimerFragment.newInstance(position+1)
            }
        }
        return PlaceholderFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
       return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}