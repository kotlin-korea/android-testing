/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.notes

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.NavigationView
import android.support.test.espresso.IdlingResource
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.android.testing.notes.R
import com.example.android.testing.notes.statistics.StatisticsActivity
import com.example.android.testing.notes.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // Set up the toolbar.
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawer_layout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(nav_view)

        if (null == savedInstanceState) {
            initFragment(NotesFragment.newInstance())
        }
    }

    private fun initFragment(notesFragment: Fragment) {
        // Add the NotesFragment to the layout
        supportFragmentManager
                .beginTransaction()
                .add(R.id.contentFrame, notesFragment)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.statistics_navigation_menu_item -> startActivity(Intent(this@NotesActivity, StatisticsActivity::class.java))
                else -> {
                }
            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawer_layout.closeDrawers()
            true
        }
    }

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.getIdlingResource()
}
