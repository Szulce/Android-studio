package szulc.magdalena.fitpost.mastodon

import android.app.Activity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import kotlinx.android.synthetic.main.activity_show_message.*
import org.jetbrains.anko.doAsync
import szulc.magdalena.fitpost.R

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
class ShowMessage : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_message)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val textView = findViewById<TextView>(R.id.textView).apply { text = message }
        download()

    }
    fun download():Unit{
        doAsync{
/*
            val accessToken = "65e3c1e1d8532e8445392c9c3cc387d39fdd2d6f9589d2398b54607d9ea10ac8"

            val client =
                MastodonClient.Builder("qoto.org", OkHttpClient.Builder(), Gson()).accessToken(accessToken).build()
*/

            val username = "szulcmagdalena241d@gmail.com"//pref.getString("username_input","szulcmagdalena241d@gmail.com")
            val password = "OnomatopejeA97!#"//pref.getString("password_input","OnomatopejeA97!#")

            val file = filesDir.absolutePath
            val client = Authenticator.appRegistrationIfNeeded("qoto.org",file+"/credl.xml",true,username,password)


            val status = Statuses(client)

            val r = status.postStatus(textView.text.toString(),null,null,false,null, Status.Visibility.Private).execute()


        }

}


}