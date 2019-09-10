package szulc.magdalena.fitpost.mastodon.providers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import szulc.magdalena.fitpost.AddFormulaActivity
import szulc.magdalena.fitpost.MainActivity
import szulc.magdalena.fitpost.R


/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {


    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val result = context.contentResolver.query(
            DataContentProvider.CONTENT_URI,
            null,
            null,
            null,
            null,
            null
        )
        result?.moveToFirst()

        for (appWidgetId in appWidgetIds) {

            val contentCol = result?.getColumnIndex("content")
            // var widgetText = result.getString(content_col).toString()
            // println(widgetText)

            var widgetText = if (result != null) {
                Html.fromHtml(result.getString(contentCol!!).toString())

            } else {
                null
            }
            // result.close()
            result?.moveToNext()


            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId,
                widgetText
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        //val views = RemoteViews(context.packageName, R.layout.app_widget)
        //views.setViewVisibility(R.layout.app_widget,LinearLayout.GONE)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int, widgetText: Spanned?
        ) {

            // Construct the RemoteViews object
            val views =
                RemoteViews(context.packageName, R.layout.app_widget)
            if (widgetText != null)
                views.setTextViewText(R.id.appwidget_text, widgetText)

            val intentPost = Intent(context, AddFormulaActivity::class.java)
            val pendingPostIntent = PendingIntent.getActivity(context, 0, intentPost, 0)
            views.setOnClickPendingIntent(R.id.buttonPostFromWidget, pendingPostIntent)

            val intentApp = Intent(context, MainActivity::class.java)
            val pendingAppIntent = PendingIntent.getActivity(context, 0, intentApp, 0)
            views.setOnClickPendingIntent(R.id.buttonViewFromWidget, pendingAppIntent)

            val mapIntent = Intent(context, MainActivity::class.java)
            mapIntent.putExtra("PAGE", 0)
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingMapIntent = PendingIntent.getActivity(context, 0, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(
                R.id.buttonMapFromWidget,
                pendingMapIntent
            )
            val timerIntent = Intent(context, MainActivity::class.java)
            timerIntent.putExtra("PAGE", 2)
            timerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingTimerIntent = PendingIntent.getActivity(context, 0, timerIntent, timerIntent.flags)
            views.setOnClickPendingIntent(
                R.id.buttonTimerFromWidget,
                pendingTimerIntent
            )

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}

