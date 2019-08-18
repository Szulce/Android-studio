package szulc.magdalena.fitpost.mastodon


import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import szulc.magdalena.fitpost.*
import szulc.magdalena.fitpost.services.UpdateIntentService

class MainActivityOLD : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var content: MutableList<MyStatus>
    private var position = 0


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("position",position)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState.getInt("position", 0) >0){
            recyclerView.layoutManager?.scrollToPosition(position)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        content = mutableListOf<MyStatus>()
        viewAdapter  = RecycleAdapter(content)

        //recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
         //   setHasFixedSize(true)
          //  layoutManager = viewManager
           // adapter = viewAdapter
        }

        //recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){

        //    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        //        super.onScrollStateChanged(recyclerView, newState)
        //        if(newState == RecyclerView.SCROLL_STATE_IDLE){
         //           position = LinearLayoutManager(recyclerView.context).findFirstVisibleItemPosition()
         //       }
         //   }

        //})


       // swiperRefreshLayout.setOnRefreshListener {
        //     UpdateTimeline()

       // }
//        var pref = PreferenceManager.getDefaultSharedPreferences(this)
//
//        var username = pref.getString("username_input","szulcmagdalena241d@gmail.com")
//        var password = pref.getString("password_input","OnomatopejeA97!#")
//            download(username,password)


    }

    fun sendMessage(view:View){
        //val edit = findViewById<EditText>(R.id.editText)
        //val message  = editText.text.toString()
       // UpdateIntentService.startActionSend(this,message)
        //UpdateIntentService.startActionUpdate(this,message)

       // edit.text.clear()
//        var pref = PreferenceManager.getDefaultSharedPreferences(this)
//
//        var username = pref.getString("username_input","szulcmagdalena241d@gmail.com")
//        var password = pref.getString("password_input","OnomatopejeA97!#")
//
//        post(username,password,message)
//
//       download(username,password)



    }

    fun post(username:String,password:String,message:String):Unit{
       // doAsync{

         //   val file = filesDir.absolutePath
         //   val client = Authenticator.appRegistrationIfNeeded("qoto.org",file+"/credl.xml",true,username,password)
         //   val status = Statuses(client)
          //  val r = status.postStatus(message,null,null,false,null, Status.Visibility.Private).execute()
     //   }
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       // val inflater = menuInflater
        //inflater.inflate(R.menu.menu_mastodon,menu)
        return true
    }*/
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){

            R.id.settingsButtoon -> {
            //    val intent = Intent(this, SettingsActivity::class.java)
          //      startActivity(intent)
                true
            }
            R.id.aboutButton -> {
              //  val intent = Intent(this,AboutActivity::class.java)
            //    startActivity(intent)
                true
            }
           // else -> super.onOptionsItemSelected(item!!)
        }
    }
*/
//    fun dispplayStream(view:View){
//        val intent = Intent(this,ShowStream::class.java)
//        startActivity(intent)
//    }
//
//    fun displaySettings(view:View){
//            val intent = Intent(this,SettingsActivity::class.java).apply { }
//        startActivity(intent)
//    }

//    fun download(username:String,password:String):Unit{
//        doAsync {
//
//            val file = filesDir.absolutePath
//            val client =
//                Authenticator.appRegistrationIfNeeded("qoto.org", file + "/credl.xml", true, username, password)
//            val timelines = Timelines(client)
//            var result = timelines.getHome(Range(limit = 10)).execute()
//
//            //val resultList = result.part.map { Html.fromHtml(it.content) }.toMutableList()
//
//            content.clear()
//            content.plusAssign(result.part)
//            uiThread { viewAdapter?.notifyDataSetChanged() }
//        }
//
//        }

    fun UpdateTimeline():Unit{
    /*    val db = this.database.readableDatabase
        UpdateIntentService.startActionUpdate(this,"any")



        doAsync {
            val newStatuses = MyDatabaseOpenHelper.getStatus(db)
            content.clear()
            content.addAll(newStatuses)


            uiThread {
                viewAdapter?.notifyDataSetChanged()
                swiperRefreshLayout.setRefreshing(false)
            }
        }

    }*/
}

