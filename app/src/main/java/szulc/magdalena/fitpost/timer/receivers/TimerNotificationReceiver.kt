package szulc.magdalena.fitpost.timer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import szulc.magdalena.fitpost.timer.AppConstants
import szulc.magdalena.fitpost.ui.main.fragments.TimerFragment
import szulc.magdalena.fitpost.timer.util.NotificationUtil
import szulc.magdalena.fitpost.timer.util.PrefUtil

class TimerNotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                TimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerFragment.TimerStatus.Stop, context)
                NotificationUtil.hideTimerNotificationUtil(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getTimerRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmTimerBackgroundSet(context)
                val seconds = TimerFragment.nowSeconds

                secondsRemaining -= seconds - alarmSetTime
                PrefUtil.setTimerRemaining(secondsRemaining, context)

                TimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerFragment.TimerStatus.Pause, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getTimerRemaining(context)
                setAlarmTimer(context, secondsRemaining)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemainig = minutesRemaining * 60L
                PrefUtil.setTimerRemaining(secondsRemainig,context)
                setAlarmTimer(context, secondsRemainig)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setAlarmTimer(context: Context, secondsRemainig: Long) {
        val wakeUpTime = TimerFragment.setAlarm(context, TimerFragment.nowSeconds, secondsRemainig)
        PrefUtil.setTimerState(TimerFragment.TimerStatus.Run, context)
        NotificationUtil.showTimerRunning(context,wakeUpTime)
    }
}
