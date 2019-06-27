package szulc.magdalena.fitpost

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_tab3.*
import util.PrefUtil

/**
 * Class managing timerA
 * */

class TimerActivity  : Fragment() {

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

        val playFab : FloatingActionButton =  viewOfFragment.findViewById(R.id.floatingActionButtonPlay)
        val pauseFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonPause)
        val stopFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonStop)

        playFab.setOnClickListener {
            startTimer()
            timerActualStatus = TimerStatus.Run
            updateTimerButtons()
        }
        pauseFab.setOnClickListener {
            timer.cancel()
            timerActualStatus = TimerStatus.Pause
            updateTimerButtons()
        }
        stopFab.setOnClickListener {
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
        val secondsPlusMinutesToFinish = timeRemaining - minutesToFinish * 60
        val secondsString = secondsPlusMinutesToFinish.toString()
        textViewTimer.text = "$minutesToFinish${
        if(secondsString.length == 2){
            secondsPlusMinutesToFinish
        }else{
            "0"+secondsPlusMinutesToFinish
        }}"
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

}