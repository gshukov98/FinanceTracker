package com.georgishukov.financetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgishukov.financetracker.database.AppDatabase
import com.georgishukov.financetracker.databinding.ActivityMainBinding
import com.georgishukov.financetracker.model.Cost
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var db: AppDatabase = AppDatabase(this)

    var costsList: List<Cost> = ArrayList<Cost>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id:Long = intent.getLongExtra("ID",-1)

        val actionBar = supportActionBar
        val displayHomeAsUpEnabled = actionBar?.setDisplayHomeAsUpEnabled(true)

        costsList = db.getCosts()
        Collections.sort(costsList,
            Comparator<Cost> { o1: Cost, o2: Cost -> o1.timestamp.compareTo(o2.timestamp) })

        var sum:Double = 0.0
        for (cost in costsList){
            sum = sum + cost.price
        }

        binding.tvPrice.setText(sum.toString())

        val recyclerView = findViewById<RecyclerView>(R.id.costs_list_view)
        val adapter = CostsAdapter(this, costsList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddCostActivity::class.java)
            intent.putExtra("ID",id)
            startActivity(intent)
        })
    }
}