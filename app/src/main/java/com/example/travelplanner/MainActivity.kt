package com.example.travelplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerDestination: Spinner = findViewById(R.id.spinnerDestination)
        val destinations = listOf(
            Pair("France", R.drawable.france),
            Pair("USA", R.drawable.usa),
            Pair("Japan", R.drawable.japan),
            Pair("Australia", R.drawable.australia)
        )
        val adapter = DestinationAdapter(this, R.layout.spinner_item, destinations)
        spinnerDestination.adapter = adapter

        val btnNext: Button = findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            val selectedCountry = spinnerDestination.selectedItem as Pair<String, Int>
            val intent = Intent(this, ActivityPlanner::class.java)
            intent.putExtra("COUNTRY", selectedCountry.first)
            startActivity(intent)
        }
    }

    private class DestinationAdapter(context: Context, resource: Int, private val destinations: List<Pair<String, Int>>) :
        ArrayAdapter<Pair<String, Int>>(context, resource, destinations) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(convertView, parent, position)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(convertView, parent, position)
        }

        private fun createViewFromResource(convertView: View?, parent: ViewGroup, position: Int): View {
            val view = convertView ?: View.inflate(context, R.layout.spinner_item, null)
            val textView = view.findViewById<TextView>(R.id.spinnerTextView)
            val imageView = view.findViewById<ImageView>(R.id.spinnerImageView)

            textView.text = destinations[position].first
            imageView.setImageResource(destinations[position].second)

            return view
        }
    }
}