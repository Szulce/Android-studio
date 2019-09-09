package szulc.magdalena.fitpost

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import szulc.magdalena.fitpost.mastodon.providers.DataContentProvider

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {


    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        val result =  context.contentResolver.query(DataContentProvider.CONTENT_URI,null,null,null,null,null)
        result!!.moveToFirst()

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {


            val contentCol = result.getColumnIndex("content")
            // var widgetText = result.getString(content_col).toString()
            // println(widgetText)
            val widgetText = Html.fromHtml(result.getString(contentCol).toString())
            // result.close()
            result.moveToNext()


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
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int,widgetText:Spanned
        ) {

              // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.app_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}

