package szulc.magdalena.fitpost

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ArrayAdapter
import com.sys1yagi.mastodon4j.api.Pageable
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Public
import com.sys1yagi.mastodon4j.api.method.Timelines
import kotlinx.android.synthetic.main.activity_show_stream.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import szulc.magdalena.fitpost.mastodon.Authenticator

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class ShowStream : Activity() {

    private val content: MutableList<Spanned> = mutableListOf(Html.fromHtml("<b>text</b>"))
    private var dataAdapter : ArrayAdapter<Spanned>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_stream)

        dataAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,content)
        listViewStream.adapter = dataAdapter
    }

    fun downloadPublic(view: View):Unit{
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        val username = pref.getString("username_input","szulcmagdalena241d@gmail.com")
        val password = pref.getString("password_input","OnomatopejeA97!#")

      download(true,username,password)
    }

    fun downloadLocal(view: View):Unit{
        download(false,"","")
    }



    fun download(public_local:Boolean = true, username: String?, password: String?):Unit{
        doAsync {

            val file = filesDir.absolutePath
            val client = Authenticator.appRegistrationIfNeeded("qoto.org",file+"/credl.xml",true,username,password)

           // val accesToken = "65e3c1e1d8532e8445392c9c3cc387d39fdd2d6f9589d2398b54607d9ea10ac8"
            //val client =
               // MastodonClient.Builder("qoto.org", OkHttpClient.Builder(), Gson()).accessToken(accesToken).build()


            var result : Pageable<Status>? = null

           if(public_local)
           {
              val timelines = Public(client)
               result = timelines.getLocalPublic(Range(limit = 10)).execute()
           }else{
            val timelines = Timelines(client)
             result = timelines.getHome(Range(limit = 10)).execute()
           }



                val resultList = result.part.map { Html.fromHtml(it.content) }.toMutableList()

                content.clear()
                content += resultList
                uiThread { dataAdapter?.notifyDataSetChanged() }

        }
    }






}
