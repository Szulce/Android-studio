package szulc.magdalena.fitpost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import kotlinx.android.synthetic.main.activity_add_formula.*
import org.jetbrains.anko.doAsync
import szulc.magdalena.fitpost.mastodon.Authenticator
import szulc.magdalena.fitpost.services.UpdateIntentService

class AddFormulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_formula)
        //TODO
    }

    fun sendMessage(view: View){
        val edit = findViewById<EditText>(R.id.editText)
        val message  = editText.text.toString()
        UpdateIntentService.startActionSend(this,message)
        //UpdateIntentService.startActionUpdate(this,message)

        edit.text.clear()
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
        doAsync{

            val file = filesDir.absolutePath
            val client = Authenticator.appRegistrationIfNeeded("qoto.org",file+"/credl.xml",true,username,password)
            val status = Statuses(client)
            val r = status.postStatus(message,null,null,false,null, Status.Visibility.Private).execute()
        }
    }

    

}
