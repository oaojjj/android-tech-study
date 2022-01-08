package com.example.recyclerviewexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recyclerviewexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val shapeAdapter by lazy { ShapeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding.rvContainer) {
            adapter = shapeAdapter
            layoutManager = GridLayoutManager(applicationContext, 5)
            setHasFixedSize(true)
        }

        binding.btnAdd.setOnClickListener { shapeAdapter.addItem() }
        binding.btnRemove.setOnClickListener { shapeAdapter.removeItem() }
        binding.btnShuffle.setOnClickListener { shapeAdapter.shuffleItem() }

        binding.btnIncrement.setOnClickListener {
            shapeAdapter.incrementCount()
            binding.tvCount.text = shapeAdapter.count.toString()
        }
        binding.btnDecrement.setOnClickListener {
            shapeAdapter.decrementCount()
            binding.tvCount.text = shapeAdapter.count.toString()
        }

    }
}