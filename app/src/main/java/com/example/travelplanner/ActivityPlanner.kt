package com.example.travelplanner

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ActivityPlanner : AppCompatActivity() {

    private val selectedActivities = mutableListOf<String>()
    private lateinit var country: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        country = intent.getStringExtra("COUNTRY") ?: ""
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        tvTitle.text = "Select your travel plans for $country"

        loadTravelPlans()

        val spinnerActivity: Spinner = findViewById(R.id.spinnerActivity)
        ArrayAdapter.createFromResource(
            this,
            R.array.activities_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerActivity.adapter = adapter
        }

        val btnSavePlan: Button = findViewById(R.id.btnSavePlan)
        btnSavePlan.setOnClickListener {
            val selectedActivity = spinnerActivity.selectedItem as String
            if (selectedActivity.isNotEmpty() && !selectedActivities.contains(selectedActivity)) {
                selectedActivities.add(selectedActivity)
                updateActivityList()
                saveTravelPlans()
            }
        }

        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            saveTravelPlans()
            finish()
        }
    }

    private fun updateActivityList() {
        val activityList: LinearLayout = findViewById(R.id.activityList)
        activityList.removeAllViews()

        selectedActivities.forEach { activity ->
            val activityView = layoutInflater.inflate(R.layout.activity_item, activityList, false)
            val tvActivityName: TextView = activityView.findViewById(R.id.tvActivityName)
            tvActivityName.text = activity
            val btnRemove: Button = activityView.findViewById(R.id.btnRemove)
            btnRemove.setOnClickListener {
                selectedActivities.remove(activity)
                updateActivityList()
                saveTravelPlans()
            }
            activityList.addView(activityView)
        }
    }

    private fun saveTravelPlans() {
        val sharedPreferences = getSharedPreferences("TravelPlanner", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(selectedActivities)
        editor.putString("plans_$country", json)
        editor.apply()
    }

    private fun loadTravelPlans() {
        val sharedPreferences = getSharedPreferences("TravelPlanner", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("plans_$country", "")
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<String>>() {}.type
            selectedActivities.clear()
            selectedActivities.addAll(gson.fromJson(json, type))
            updateActivityList()
        }
    }
}