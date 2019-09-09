package szulc.magdalena.fitpost.services

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.util.Log
import androidx.constraintlayout.widget.Group
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import com.sys1yagi.mastodon4j.api.method.Timelines
import org.jetbrains.anko.db.replace
import org.jetbrains.anko.db.select
import szulc.magdalena.fitpost.mastodon.database
import szulc.magdalena.fitpost.mastodon.AppWidget
import szulc.magdalena.fitpost.mastodon.Authenticator

private const val ACTION_SEND = "szulc.magdalena.fitpost.mastodon.action.SEND"
private const val ACTION_SEND_WITH_IMAGE = "szulc.magdalena.fitpost.mastodon.action.SEND_WITH_IMAGE"
private const val ACTION_UPDATE = "szulc.magdalena.fitpost.mastodon.action.UPDATE"

private const val EXTRA_STREAM_TYPE = "szulc.magdalena.fitpost.mastodon.extra.STREAM_TYPE"
private const val EXTRA_SEND_TEXT = "szulc.magdalena.fitpost.mastodon.extra.SEND_TEXT"
private const val EXTRA_SEND_BITMAP = "szulc.magdalena.fitpost.mastodon.extra.SEND_BITMAP"


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
                Log.d("MASTODON","ACTION SEND : $msg")
                handleActionSendWithImage(msg,bitmap)
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

    private fun handleActionSendWithImage(msg: String?, bitmap: Bitmap) {
        println("Service:Send")
        Log.d("MASTODON","Handle Action Send: $msg")
        if (client == null) return
        val status = Statuses(client!!)
        val gson:Gson = Gson().fromJson()
        val mediaIds = client?.post("media",
        { 'file' :[ $file, undef ], %$params },
        headers => { Content_Type => 'form-data' },
        );
        val r = status.postStatus(msg!!, null, mediaIds, false, null, Status.Visibility.Private).execute()
        Log.d("MASTODON","Send status: $r")
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
                    "mediaAttachments" to res.mediaAttachments,
                    "tags" to res.tags
                )
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
        fun startActionSendWithImage(context: Context, msg: String,imgBitmap: Bitmap) {
            val intent = Intent(context, UpdateIntentService::class.java).apply {
                action = ACTION_SEND_WITH_IMAGE
                putExtra(EXTRA_SEND_TEXT, msg)
                putExtra(EXTRA_SEND_BITMAP,imgBitmap)
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
