package edu.temple.audibleonmobileapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val tabs = findViewById<TabLayout>(R.id.tabLayout)

        val signUp = SignUpFragment()
        val login = LoginFragment()


        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                when(tab?.position){
                    0 -> {
                        transaction.replace(R.id.fragmentContainerView2, signUp)
                        transaction.commit()
                    }
                    1 -> {
                        transaction.replace(R.id.fragmentContainerView2, login)
                        transaction.commit()
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })*/

    }
}