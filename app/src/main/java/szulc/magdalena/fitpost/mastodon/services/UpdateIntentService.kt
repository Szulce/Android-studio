package szulc.magdalena.fitpost.mastodon.services

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.util.Log
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.entity.Tag
import com.sys1yagi.mastodon4j.api.method.Media
import com.sys1yagi.mastodon4j.api.method.Statuses
import com.sys1yagi.mastodon4j.api.method.Timelines
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.db.replace
import org.jetbrains.anko.db.select
import szulc.magdalena.fitpost.mastodon.Authenticator
import szulc.magdalena.fitpost.mastodon.database
import szulc.magdalena.fitpost.mastodon.providers.AppWidget
import java.io.File
import java.lang.StringBuilder


private const val ACTION_SEND = "szulc.magdalena.fitpost.mastodon.action.SEND"
private const val ACTION_SEND_WITH_IMAGE = "szulc.magdalena.fitpost.mastodon.action.SEND_WITH_IMAGE"
private const val ACTION_UPDATE = "szulc.magdalena.fitpost.mastodon.action.UPDATE"

private const val EXTRA_STREAM_TYPE = "szulc.magdalena.fitpost.mastodon.extra.STREAM_TYPE"
private const val EXTRA_SEND_TEXT = "szulc.magdalena.fitpost.mastodon.extra.SEND_TEXT"
private const val EXTRA_SEND_BITMAP = "szulc.magdalena.fitpost.mastodon.extra.SEND_BITMAP"
private const val EXTRA_SEND_FILE_PATH = "szulc.magdalena.fitpost.mastodon.extra.SEND_FILE_PAH"


class UpdateIntentService : IntentService("UpdateIntentService") {

    private var client: MastodonClient? = null

    override fun onHandleIntent(intent: Intent?) {

        if (client == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)

            val username = pref.getString("username_input", "szulcmagdalena241d@gmail.com")
            val password = pref.getString("password_input", "OnomatopejeA97!#")

            val file = filesDir.absolutePath
            try {
                client =
                    Authenticator.appRegistrationIfNeeded(
                        "qoto.org",
                        file + "/credl.xml",
                        true,
                        username,
                        password
                    )
            } catch (e: Throwable) {
                print("wrong username or password")
                Log.e("MASTODON","Wrong password")
            }
        }

        when (intent?.action) {
            ACTION_SEND -> {
                val msg = intent.getStringExtra(EXTRA_SEND_TEXT)
                Log.d("MASTODON","ACTION SEND : $msg")
                handleActionSend(msg)
            }
            ACTION_SEND_WITH_IMAGE -> {
                val msg = intent.getStringExtra(EXTRA_SEND_TEXT)
                val bitmap = intent.getParcelableExtra(EXTRA_SEND_BITMAP) as Bitmap
                val filePath = intent.getStringExtra(EXTRA_SEND_FILE_PATH)
                Log.d("MASTODON","ACTION SEND : $msg filepath: $filePath")
                handleActionSendWithImage(msg,bitmap,filePath!!)
            }
            ACTION_UPDATE -> {
                val stype = intent.getStringExtra(EXTRA_STREAM_TYPE)
                handleActionUpdate(stype)
            }
        }


    }

    /**
     * Handle action Send in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionSend(msg: String?) {
        println("Service:Send")
        Log.d("MASTODON","Handle Action Send: $msg")
        if (client == null) return
        val status = Statuses(client!!)
        val r = status.postStatus(msg!!, null, null, false, null, Status.Visibility.Private).execute()
    Log.d("MASTODON","Send status: $r")
    }

    private fun handleActionSendWithImage(msg: String?, bitmap: Bitmap,filePath :String) {
        println("Service:Send")
        Log.d("MASTODON","Handle Action Send: $msg")
        if (client == null) return
        val status = Statuses(client!!)
        val media = Media(client!!)
        val multiPartBody  = buildMultilineBody(filePath,bitmap)
        val id = media.postMedia(multiPartBody)
        Log.d("MASTODON","id: $id")
        val r = status.postStatus(msg!!, null, listOf(id.execute().id), false, null, Status.Visibility.Private).execute()
        Log.d("MASTODON","Send status: $r")
    }

    private fun buildMultilineBody(filePath: String,bitmap: Bitmap): MultipartBody.Part {

        val file = File(filePath)
        val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
        Log.d("MASTODON","fileReaBody: $fileReqBody")
       return MultipartBody.Part.createFormData(
                "file",
                filePath,
                fileReqBody
            )
    }

    /**
     * Handle action Update in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionUpdate(param1: String?) {

        val db = this.database.readableDatabase

        var maxId: Long = 0
        db.select("MyStatus", "Max(id) as maxId").exec {
            moveToNext()
            maxId = getLong(getColumnIndex("maxId"))

        }
        println(maxId)
        if (client == null) return

        val timelines = Timelines(client!!)
        val result = timelines.getHome(Range(limit = 20, sinceId = maxId.toLong())).execute()

        for (res in result.part) {
            println(res.id)
            if(res.mediaAttachments.isNotEmpty()) {
                this.database.use {
                    replace(
                        "MyStatus",
                        "id" to res.id,
                        "content" to res.content,
                        "avatar" to res.account?.avatar,
                        "favouritesCount" to res.favouritesCount,
                        "reblogsCount" to res.reblogsCount,
                        "language" to res.language,
                        "visibility" to res.visibility,
                        "createdAt" to res.createdAt,
                        "mediaAttachments" to res.mediaAttachments[0].url,
                        "tags" to tagsBinder(res.tags)
                    )
                }
            }else
            {
                this.database.use {
                    replace(
                        "MyStatus",
                        "id" to res.id,
                        "content" to res.content,
                        "avatar" to res.account?.avatar,
                        "favouritesCount" to res.favouritesCount,
                        "reblogsCount" to res.reblogsCount,
                        "language" to res.language,
                        "visibility" to res.visibility,
                        "createdAt" to res.createdAt,
                        "mediaAttachments" to "",
                        "tags" to tagsBinder(res.tags)
                    )
                }
            }
        }
        //update widget
        val intent = Intent(this, AppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(
            application
        ).getAppWidgetIds(ComponentName(application, AppWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)


    }

    private fun tagsBinder(tags: List<Tag>): String {
        Log.d("MASTODON","tags: ${tags.size}")
        val tagsTogether = StringBuilder(" ")
        for(i in tags.indices){
            tagsTogether.append(" #")
          tagsTogether.append(tags[i].name)
        }
        return tagsTogether.toString()

    }


    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionSend(context: Context, msg: String) {
            val intent = Intent(context, UpdateIntentService::class.java).apply {
                action = ACTION_SEND
                putExtra(EXTRA_SEND_TEXT, msg)
            }
            context.startService(intent)
        }
        @JvmStatic
        fun startActionSendWithImage(context: Context, msg: String,imgBitmap: Bitmap,filePath:String) {
            val intent = Intent(context, UpdateIntentService::class.java).apply {
                action = ACTION_SEND_WITH_IMAGE
                putExtra(EXTRA_SEND_TEXT, msg)
                putExtra(EXTRA_SEND_BITMAP,imgBitmap)
                putExtra(EXTRA_SEND_FILE_PATH,filePath)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun startActionUpdate(context: Context, stype: String) {
            val intent = Intent(context, UpdateIntentService::class.java).apply {
                action = ACTION_UPDATE
                putExtra(EXTRA_STREAM_TYPE, stype)
            }
            context.startService(intent)

        }
    }
}
