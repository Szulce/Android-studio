package szulc.magdalena.fitpost.ui.main.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_tab3.*
import szulc.magdalena.fitpost.R
import szulc.magdalena.fitpost.timer.receivers.TimerExpiredReceiver
import szulc.magdalena.fitpost.timer.util.NotificationUtil
import szulc.magdalena.fitpost.timer.util.PrefUtil
import java.util.*

/**
 * Class managing timerA
 * */

class TimerFragment : Fragment() {

    enum class TimerStatus {
        Run, Pause, Stop
    }

    private lateinit var timer: CountDownTimer
    private lateinit var viewOfFragment: View
    private val exerciseLength = longArrayOf(30L, 60L, 120L, 60L, 30L)//in seconds
    private val exerciseImages = listOf(R.drawable.exercise1,R.drawable.exercise2,R.drawable.exercise4,R.drawable.exercise5,R.drawable.excercise3)
    private var exerciseId = 0
    private var timerActualStatus = TimerStatus.Stop
    private var timeRemaining = 0L
    var imageView:ImageView?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment = inflater.inflate(R.layout.fragment_tab3, container, false)

        Log.i("Timer", "Timer activity Tab started.")

        val playFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonPlay)
        val pauseFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonPause)
        val stopFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonStop)

        imageView= viewOfFragment.findViewById(R.id.imageView2)
        imageView?.setImageResource(exerciseImages[exerciseId])

        playFab.setOnClickListener {
            Log.i("startTimer", "Timer start button pressed.")
            startTimer()
            timerActualStatus = TimerStatus.Run
            updateTimerButtons()
        }
        pauseFab.setOnClickListener {
            Log.i("pauseTimer", "Timer pause button pressed.")
            timer.cancel()
            timerActualStatus = TimerStatus.Pause
            updateTimerButtons()
        }
        stopFab.setOnClickListener {
            Log.i("stopTimer", "Timer stop button pressed")
            timer.cancel()
            onTimerFinish()
        }

        return viewOfFragment
    }

    override fun onResume() {
        super.onResume()
        initTimer()

        removeAlarm(viewOfFragment.context)
        NotificationUtil.hideTimerNotificationUtil(viewOfFragment.context)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPause() {
        super.onPause()

        if (timerActualStatus == TimerStatus.Run) {
            timer.cancel()
            val wakeUpTime = setAlarm(viewOfFragment.context, nowSeconds, timeRemaining)
            NotificationUtil.showTimerRunning(viewOfFragment.context,wakeUpTime)
        } else if (timerActualStatus == TimerStatus.Pause) {
            timer.cancel()
            NotificationUtil.showTimerPaused(viewOfFragment.context)
        }

        PrefUtil.setPrevoiusTimerLength(exerciseLength[exerciseId], viewOfFragment.context)
        PrefUtil.setTimerRemaining(timeRemaining, viewOfFragment.context)
        PrefUtil.setTimerState(timerActualStatus, viewOfFragment.context)
    }

    private fun initTimer() {
        Log.i("Init Timer Event", "Timer init function invoked.")

        timerActualStatus = PrefUtil.getTimerState(viewOfFragment.context)


        if (timerActualStatus == TimerStatus.Stop) {
            setNewTimerSeconds()
        } else {
            setPreviousTimerSeconds()
        }

        if (timerActualStatus == TimerStatus.Run || timerActualStatus == TimerStatus.Pause) {
            exerciseId = PrefUtil.getExerciseId(viewOfFragment.context).toInt()
        }

        timeRemaining = if (timerActualStatus == TimerStatus.Run || timerActualStatus == TimerStatus.Pause) {
            PrefUtil.getTimerRemaining(viewOfFragment.context)
        } else {
            exerciseLength[exerciseId]
        }

        val alarmSetTime = PrefUtil.getAlarmTimerBackgroundSet(viewOfFragment.context)
        if (alarmSetTime > 0) {
            timeRemaining -= nowSeconds - alarmSetTime
        }
        if (timeRemaining <= 0){
            onTimerFinish()
        }
        else if (timerActualStatus == TimerStatus.Run) {
                startTimer()
            }

        updateTimerButtons()
        updateCountDownUi()

    }

    private fun onTimerFinish() {
        timerActualStatus = TimerStatus.Stop

        setNewTimerSeconds()

        progressBarCountDown.progress = 60
        if (exerciseId != exerciseLength.size-1) {
            exerciseId++
            PrefUtil.setTimerRemaining(exerciseLength[exerciseId], viewOfFragment.context)
            timeRemaining = exerciseLength[exerciseId]

            updateTimerButtons()
            updateCountDownUi()
            imageView2.setImageResource(exerciseImages[exerciseId])
        } else {
            exerciseId = 0

        }



    }

    private fun startTimer() {
        timerActualStatus = TimerStatus.Run

        timer = object : CountDownTimer(timeRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinish()
            override fun onTick(p0: Long) {
                timeRemaining = p0 / 1000
                updateCountDownUi()
            }
        }.start()

    }

    private fun setNewTimerSeconds() {
        val timeInMinutes = PrefUtil.getTimerLength(viewOfFragment.context)
        exerciseLength[exerciseId] = timeInMinutes * 60L
        progressBarCountDown.max = exerciseLength[exerciseId].toInt() + 60
        Log.i("Timer", "Progressbar max:".plus(progressBarCountDown.max))
    }

    private fun setPreviousTimerSeconds() {
        exerciseLength[exerciseId] = PrefUtil.getPrevoiusTimerLength(viewOfFragment.context)
        progressBarCountDown.max = exerciseLength[exerciseId].toInt() + 60
        Log.i("Timer", "Progressbar max:".plus(progressBarCountDown.max))
    }

    private fun updateCountDownUi() {
        val minutesToFinish = timeRemaining / 60
        var minutesString = minutesToFinish.toString()
        val secondsPlusMinutesToFinish = timeRemaining - minutesToFinish * 60
        var secondsString = secondsPlusMinutesToFinish.toString()

        Log.i("Timer", "minutes $minutesString seconds $secondsString")

        if (secondsString.length != 2) {
            secondsString = "0$secondsPlusMinutesToFinish"
        }

        textViewTimer.text = "$minutesToFinish:$secondsString"
        Log.i("Timer", "progres:".plus((exerciseLength[exerciseId] - timeRemaining).toInt()))
        progressBarCountDown.progress = (exerciseLength[exerciseId] - timeRemaining).toInt()
    }

    private fun updateTimerButtons() {
        when (timerActualStatus) {
            TimerStatus.Stop -> {
                floatingActionButtonPlay.isEnabled = true
                floatingActionButtonPause.isEnabled = false
                floatingActionButtonStop.isEnabled = false
            }
            TimerStatus.Run -> {
                floatingActionButtonPlay.isEnabled = false
                floatingActionButtonPause.isEnabled = true
                floatingActionButtonStop.isEnabled = true
            }
            TimerStatus.Pause -> {
                floatingActionButtonPlay.isEnabled = true
                floatingActionButtonPause.isEnabled = false
                floatingActionButtonStop.isEnabled = true
            }
        }
    }


    companion object {


        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

        @JvmStatic
        fun newInstance(sectionNumber: Int): TimerFragment {
            return TimerFragment().apply {
                arguments = Bundle().apply {
                    putInt("3", sectionNumber)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemainig: Long): Long {
            val wakeUpTime = (nowSeconds + secondsRemainig) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmTimerBackgroundSet(nowSeconds, context)
            return wakeUpTime

        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setTimerRemaining(0, context)


        }
    }

}