package szulc.magdalena.fitpost

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import szulc.magdalena.fitpost.ui.main.fragments.TimerFragment
import util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
     //TODO SHOW NOTIFICATION
        PrefUtil.setTimerState(TimerFragment.TimerStatus.Stop,context)
        PrefUtil.setAlarmTimerBackgroundSet(0,context)
    }
}
