package util

import android.content.Context
import android.preference.PreferenceManager
import szulc.magdalena.fitpost.MainActivity


class PrefUtil {

    companion object{

        private const val TIMER_STATE_ID = "szulc.magdalena.timer_state"
        private const val TIMER_REMAINING_ID = "szulc.magdalena.timer.seconds_remaining"
        private const val PREVIOUS_TIMER_LENGTH_ID = "szulc.magdalena.timer.previous_timer_length"

        fun getTimerState(context: Context):MainActivity.TimerStatus{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID,0)
            return MainActivity.TimerStatus.values()[ordinal]
        }

        fun setTimerState(state:MainActivity.TimerStatus,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID,ordinal)
            editor.apply()

        }

        fun getTimerLength(context: Context):Int{
            //TODO
            return 1
        }


        fun getTimerRemaining(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(TIMER_REMAINING_ID,0)
        }

        fun setTimerRemaining(seconds:Long,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(TIMER_REMAINING_ID,seconds)
            editor.apply()

        }

        fun getPrevoiusTimerLength(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_ID,0)
        }

        fun setPrevoiusTimerLength(seconds:Long,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_ID,seconds)
            editor.apply()

        }



    }
}