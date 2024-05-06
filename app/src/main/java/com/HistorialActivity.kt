package com

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wabu.MainActivity
import com.example.wabu.databinding.ActivityHistorialBinding

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historialDatos = intent.getSerializableExtra("historialDatos") as? List<MainActivity.HistorialItem>

        historialDatos?.let {
            historialAdapter = HistorialAdapter(it)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@HistorialActivity)
                adapter = historialAdapter
            }
        } ?: run {
            Toast.makeText(this, "No hay datos disponibles", Toast.LENGTH_SHORT).show()
            finish()
        }
        data class HistorialItem(
            val flujo: Int,
            val litros: Double,
            val porcentajeLlenado: Double
        )

    }
}

