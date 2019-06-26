package szulc.magdalena.fitpost

import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import szulc.magdalena.fitpost.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {


    //activity of timer

    enum class TimerStatus {
        Run,Pause,Stop
    }

    private lateinit var timer :CountDownTimer
    private val excerciseLength = intArrayOf(30,60,120,60,30)//in seconds
    private var timerActualStatus = TimerStatus.Stop
    private var timeRemaining = 0

    //end Activity of timer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        //set icons for tabs
        tabs.getTabAt(0)!!.setIcon(R.drawable.map_tab)
        tabs.getTabAt(1)!!.setIcon(R.drawable.edit)
        tabs.getTabAt(2)!!.setIcon(R.drawable.workout_play)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //timer Activity
        val playFab :FloatingActionButton = findViewById(R.id.floatingActionButtonPlay)
        val pauseFab:FloatingActionButton = findViewById(R.id.floatingActionButtonPause)
        val stopFab:FloatingActionButton = findViewById(R.id.floatingActionButtonStop)

        playFab.setOnClickListener {
                runTimer()
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
    }

    //Timer Activity functions

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
        }else{

        }
    }
    //end timer activity functions


}