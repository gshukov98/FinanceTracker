package com.georgishukov.financetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.georgishukov.financetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        val displayHomeAsUpEnabled = actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddCostActivity::class.java)
            startActivity(intent)
        })
    }
}