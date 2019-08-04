package util

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import szulc.magdalena.fitpost.AppConstants
import szulc.magdalena.fitpost.R
import szulc.magdalena.fitpost.Receivers.TimerNotificationReceiver
import szulc.magdalena.fitpost.ui.main.fragments.TimerFragment

class NotificationUtil {
    companion object{
        private const val  CHANNEL_TIMER_ID = "menu_timer"
        private const val  CHANNEL_NAME_TIMER = "Timer Fragment"
        private  const val  TIMER_ID = 0

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        fun showTimerExpired(context: Context){
            val startIntent = Intent(context,TimerNotificationReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(context,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            val notificationBuilder = getNotificationBuilder(context, CHANNEL_TIMER_ID,true)
            notificationBuilder.setContentTitle("Timer Expired")
                .setContentText("Start Again?")
                .setContentIntent(getPendingIntentWithStack(context,TimerFragment::class.java))
                .addAction(R.drawable.play,"Start",startPendingIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //notificationManager.createNotificationChannel()

        }

        private fun getNotificationBuilder(context: Context, channelId: String, playSound: Boolean):NotificationCompat.Builder {

            val notificationSound:Uri   = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val basicNotificationBuilder = NotificationCompat.Builder(context,channelId).setSmallIcon(R.drawable.time_img)
                .setAutoCancel(true)
                .setDefaults(0)
            if(playSound)basicNotificationBuilder.setSound(notificationSound)
            return basicNotificationBuilder

        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        private fun <T> getPendingIntentWithStack(context: Context, javaClass: Class<T>):PendingIntent{
            val resultIntent = Intent(context,javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

        }

        @TargetApi(24)
        private fun NotificationManager.createNotificationChannel(channelName :String,channelId: String,playSound: Boolean){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if(playSound)NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
               val notificationChannel  = NotificationChannel(channelId,channelName,channelImportance)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.BLUE
                this.createNotificationChannel(notificationChannel)
            }
        }
    }
}