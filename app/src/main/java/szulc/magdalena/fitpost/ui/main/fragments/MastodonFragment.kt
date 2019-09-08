package szulc.magdalena.fitpost.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.method.Timelines
import kotlinx.android.synthetic.main.fragment_tab2.*
import kotlinx.android.synthetic.main.fragment_tab2.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import szulc.magdalena.fitpost.R
import szulc.magdalena.fitpost.mastodon.*
import szulc.magdalena.fitpost.services.UpdateIntentService


/**
 * Class to manage mastodon fragment
 * */

class MastodonFragment : Fragment() {

    private lateinit var viewOfFragment:View
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var content: MutableList<MyStatus>
    private var position = 0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment =  inflater.inflate(R.layout.fragment_tab2,container,false)

        Log.i("Mastodon","Post activity Tab started.")
        viewManager = LinearLayoutManager(viewOfFragment.context) as RecyclerView.LayoutManager
        content = mutableListOf<MyStatus>()
        viewAdapter  = RecycleAdapter(content)
        recyclerView = viewOfFragment.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                   position = LinearLayoutManager(recyclerView.context).findFirstVisibleItemPosition()
               }
           }

        })

        viewOfFragment.swipeRefreshLayout.setOnRefreshListener {
             UpdateTimeline()

         }

        return viewOfFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("position",position)
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState!=null) {
            if (savedInstanceState!!.getInt("position", 0) > 0) {
                recyclerView.layoutManager?.scrollToPosition(position)

            }
        }
    }


    fun UpdateTimeline():Unit{
        val db = viewOfFragment.context.database.readableDatabase
        UpdateIntentService.startActionUpdate(viewOfFragment.context,"any")



        doAsync {
            val newStatuses = MyDatabaseOpenHelper.getStatus(db)
            content.clear()
            content.addAll(newStatuses)


            uiThread {
                viewAdapter.notifyDataSetChanged()
                swipeRefreshLayout.setRefreshing(false)
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): MastodonFragment {
            return MastodonFragment().apply {
                arguments = Bundle().apply {
                    putInt("2", sectionNumber)
                }
            }
        }
    }

}