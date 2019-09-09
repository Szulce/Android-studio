package szulc.magdalena.fitpost.timer.util

import android.content.Context
import android.preference.PreferenceManager
import szulc.magdalena.fitpost.ui.main.fragments.TimerFragment


class PrefUtil {

    companion object{

        private const val TIMER_STATE_ID = "szulc.magdalena.timer_state"
        private const val TIMER_REMAINING_ID = "szulc.magdalena.timer.seconds_remaining"
        private const val PREVIOUS_TIMER_LENGTH_ID = "szulc.magdalena.timer.previous_timer_length"
        private const val PREVIOUS_EXERCISE_ID = "szulc.magdalena.timer.exercise_id"
        private const val ALARMSET_TIME_ID = "szulc.magdalena.timer.background_time"
        private  const val TIMER_LENGTH_ID = "szulc.magdalena.timer.timer_lenght"


        fun getTimerState(context: Context): TimerFragment.TimerStatus{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID,0)
            return TimerFragment.TimerStatus.values()[ordinal]
        }

        fun setTimerState(state: TimerFragment.TimerStatus, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID,ordinal)
            editor.apply()

        }

        fun getTimerLength(context: Context):Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)

            return preferences.getInt(TIMER_LENGTH_ID,3)
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


        fun getExerciseId(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_EXERCISE_ID,0)
        }

        fun setExerciseId(id:Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_EXERCISE_ID,id)
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

        //backgroundTimer

        fun getAlarmTimerBackgroundSet(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARMSET_TIME_ID,0)
        }

        fun setAlarmTimerBackgroundSet(seconds: Long,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARMSET_TIME_ID,seconds)
            editor.apply()

        }



    }
}