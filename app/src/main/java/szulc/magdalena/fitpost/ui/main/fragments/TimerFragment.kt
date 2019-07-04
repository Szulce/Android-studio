package szulc.magdalena.fitpost.ui.main.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_tab3.*
import szulc.magdalena.fitpost.R
import util.PrefUtil

/**
 * Class managing timerA
 * */

class TimerFragment  : Fragment() {

    enum class TimerStatus {
        Run,Pause,Stop
    }

    private lateinit var timer : CountDownTimer
    private lateinit var viewOfFragment:View
    private val exerciseLength = longArrayOf(30L,60L,120L,60L,30L)//in seconds
    private var exerciseId = 0
    private var timerActualStatus = TimerStatus.Stop
    private var timeRemaining = 0L


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment =  inflater.inflate(R.layout.fragment_tab3,container,false)

        Log.i("Timer","Timer activity Tab started.")

        val playFab : FloatingActionButton =  viewOfFragment.findViewById(R.id.floatingActionButtonPlay)
        val pauseFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonPause)
        val stopFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonStop)



        playFab.setOnClickListener {
            Log.i("startTimer","Timer start button pressed.")
            startTimer()
            timerActualStatus = TimerStatus.Run
            updateTimerButtons()
        }
        pauseFab.setOnClickListener {
            Log.i("pauseTimer","Timer pause button pressed.")
            timer.cancel()
            timerActualStatus = TimerStatus.Pause
            updateTimerButtons()
        }
        stopFab.setOnClickListener {
            Log.i("stopTimer","Timer stop button pressed")
            timer.cancel()
            onTimerFinish()
        }

        return viewOfFragment
    }

    override fun onResume(){
        super.onResume()
        initTimer()
        //TODO remove background , notification
    }

    override fun onPause() {
        super.onPause()

        if(timerActualStatus == TimerStatus.Run){
            timer.cancel()
            //TODO background timer , notification
        }
        else if(timerActualStatus == TimerStatus.Pause){
            timer.cancel()
        }

        PrefUtil.setPrevoiusTimerLength(exerciseLength[exerciseId],viewOfFragment.context)
        PrefUtil.setTimerRemaining(timeRemaining,viewOfFragment.context)
        PrefUtil.setTimerState(timerActualStatus,viewOfFragment.context)
    }

    private fun initTimer(){
        Log.i("Init Timer Event","Timer init function invoked.")

        timerActualStatus = PrefUtil.getTimerState(viewOfFragment.context)

        if(timerActualStatus == TimerStatus.Stop){
            setNewTimerSeconds()
        }else{
            setPreviousTimerSeconds()
        }

        if(timerActualStatus == TimerStatus.Run || timerActualStatus == TimerStatus.Pause){
            exerciseId = PrefUtil.getExerciseId(viewOfFragment.context).toInt()
        }

        timeRemaining = if(timerActualStatus == TimerStatus.Run || timerActualStatus == TimerStatus.Pause){
            PrefUtil.getTimerRemaining(viewOfFragment.context)
        }else{
            exerciseLength[exerciseId]
        }
        //TODO change seconds remaining

        if(timerActualStatus == TimerStatus.Run){
            startTimer()
        }

        updateTimerButtons()
        updateCountDownUi()

    }

    private fun onTimerFinish(){
        timerActualStatus = TimerStatus.Stop

        setNewTimerSeconds()

        progressBarCountDown.progress = 0
        if(exerciseId!=exerciseLength.size) {
            exerciseId++
            PrefUtil.setTimerRemaining(exerciseLength[exerciseId],viewOfFragment.context)
            timeRemaining = exerciseLength[exerciseId]

            updateTimerButtons()
            updateCountDownUi()
        }else{
            //TODO Fin viewOfFragment
        }
    }

    private fun startTimer(){
        timerActualStatus = TimerStatus.Run

        timer = object : CountDownTimer(timeRemaining*1000,1000){
            override fun onFinish() = onTimerFinish()
            override fun onTick(p0: Long) {
                timeRemaining = p0/1000
                updateCountDownUi()
            }
        }.start()

    }

    private fun setNewTimerSeconds(){
        val timeInMinutes = PrefUtil.getTimerLength(viewOfFragment.context)
        exerciseLength[exerciseId] = timeInMinutes * 60L
        progressBarCountDown.max = exerciseLength[exerciseId].toInt()
    }

    private fun setPreviousTimerSeconds(){
        exerciseLength[exerciseId] = PrefUtil.getPrevoiusTimerLength(viewOfFragment.context)
        progressBarCountDown.max = exerciseLength[exerciseId].toInt()
    }

    private fun updateCountDownUi(){
        val minutesToFinish = timeRemaining/60
        var minutesString = minutesToFinish.toString()
        val secondsPlusMinutesToFinish = timeRemaining - minutesToFinish * 60
        var secondsString = secondsPlusMinutesToFinish.toString()

        Log.i("Timer","minutes $minutesString seconds $secondsString")

        if(secondsString.length != 2){
            secondsString = "0$secondsPlusMinutesToFinish"
        }

        textViewTimer.text = "$minutesToFinish:$secondsString"
        progressBarCountDown.progress = (exerciseLength[exerciseId] - timeRemaining).toInt()
    }

    private fun updateTimerButtons(){
        when(timerActualStatus)
        {
            TimerStatus.Stop ->{
                floatingActionButtonPlay.isEnabled = true
                floatingActionButtonPause.isEnabled = false
                floatingActionButtonStop.isEnabled = false
            }
            TimerStatus.Run ->{
                floatingActionButtonPlay.isEnabled = false
                floatingActionButtonPause.isEnabled = true
                floatingActionButtonStop.isEnabled = true
            }
            TimerStatus.Pause ->{
                floatingActionButtonPlay.isEnabled = true
                floatingActionButtonPause.isEnabled = false
                floatingActionButtonStop.isEnabled = true
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): TimerFragment {
            return TimerFragment().apply {
                arguments = Bundle().apply {
                    putInt("3", sectionNumber)
                }
            }
        }
    }

}