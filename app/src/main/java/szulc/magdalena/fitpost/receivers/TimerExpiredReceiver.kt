package szulc.magdalena.fitpost.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import szulc.magdalena.fitpost.ui.main.fragments.TimerFragment
import szulc.magdalena.fitpost.util.NotificationUtil
import szulc.magdalena.fitpost.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)
        PrefUtil.setTimerState(TimerFragment.TimerStatus.Stop,context)
        PrefUtil.setAlarmTimerBackgroundSet(0,context)
    }
}
