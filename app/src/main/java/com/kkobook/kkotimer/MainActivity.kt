package com.kkobook.kkotimer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.kkobook.kkotimer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var timerFragment: TimerFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        if (savedInstanceState == null) {
            timerFragment = TimerFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TimerFragment.newInstance())
                .commitNow()
        }
        settingsFragment = SettingsFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, settingsFragment)
                    .commitNow()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TimerFragment.newInstance())
            .commitNow()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return super.onSupportNavigateUp()
    }
}