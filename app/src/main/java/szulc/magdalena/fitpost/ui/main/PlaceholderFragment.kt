package szulc.magdalena.fitpost.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import szulc.magdalena.fitpost.R


class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private val NUMBER_SECTION = "section_number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(NUMBER_SECTION) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_tab1, container, false)
        when (arguments?.getInt(NUMBER_SECTION)?:1) {
            1-> {
                root = inflater.inflate(R.layout.fragment_tab1, container, false)
            }
            2->{
                root = inflater.inflate(R.layout.fragment_tab2, container, false)
            }
            3->{
                root = inflater.inflate(R.layout.fragment_tab3, container, false)

            }
        }
        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(NUMBER_SECTION, sectionNumber)
                }
            }
        }
    }
}