package szulc.magdalena.fitpost.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import szulc.magdalena.fitpost.R

/**
 * Class to manage map fragment
 * */

class MapFragment {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment =  inflater.inflate(R.layout.fragment_tab3,container,false)

        Log.i("Timer","Timer activity Tab started.")

        val playFab : FloatingActionButton =  viewOfFragment.findViewById(R.id.floatingActionButtonPlay)
        val pauseFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonPause)
        val stopFab: FloatingActionButton = viewOfFragment.findViewById(R.id.floatingActionButtonStop)



        playFab.setOnClickListener {
            Log.i("startTimer","Timer start button pressed.")
            startTimer()
            timerActualStatus = TimerFragment.TimerStatus.Run
            updateTimerButtons()
        }
        pauseFab.setOnClickListener {
            Log.i("pauseTimer","Timer pause button pressed.")
            timer.cancel()
            timerActualStatus = TimerFragment.TimerStatus.Pause
            updateTimerButtons()
        }
        stopFab.setOnClickListener {
            Log.i("stopTimer","Timer stop button pressed")
            timer.cancel()
            onTimerFinish()
        }

        return viewOfFragment
    }

    private val NUMBER_SECTION = "1"

    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): TimerFragment {
            return TimerFragment().apply {
                arguments = Bundle().apply {
                    putInt(NUMBER_SECTION, sectionNumber)
                }
            }
        }
    }

}