package szulc.magdalena.fitpost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import szulc.magdalena.fitpost.settings.AboutActivity
import szulc.magdalena.fitpost.settings.SettingsActivity
import szulc.magdalena.fitpost.settings.TimerSetTimeActivity
import szulc.magdalena.fitpost.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

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
            val formulaIntent = Intent(view.context,AddFormulaActivity::class.java)
                startActivity(formulaIntent)
        }

        val toolBar:androidx.appcompat.widget.Toolbar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolBar)
        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }


            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }


            override fun onPageSelected(position: Int) {
                if(position == 1){
                    fab.show()
                }else{
                    fab.hide()
                }
            }


        })
        if(intent.extras!= null){
            Log.d("MAIN","EXTRAS FROM WIDGET")
            when {
                intent.extras!!.get("PAGE") == 0 -> viewPager.setCurrentItem(0,false)
                intent.extras!!.get("PAGE") == 2 -> viewPager.setCurrentItem(2,false)
                else -> viewPager.setCurrentItem(1,false)
            }
        }
        else {
            Log.d("MAIN","EXTRAS FROM WIDGET IS NULL")
            viewPager.setCurrentItem(1, false)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         val inflater = menuInflater
        inflater.inflate(R.menu.menu_mastodon,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){

            R.id.settingsButtoon -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                      startActivity(intent)
                true
            }
            R.id.aboutButton -> {
                  val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                true
            }
            R.id.action_settings_timer -> {
                val intent = Intent(this,
                    TimerSetTimeActivity::class.java)
                startActivity(intent)
                true
            }
             else -> super.onOptionsItemSelected(item)
        }
    }


}