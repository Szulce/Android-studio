package szulc.magdalena.fitpost

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
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

       // viewPager?.setCurrentItem(1,false)

    }




}