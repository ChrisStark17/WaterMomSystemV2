package com.example.wabu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wabu.adapters.HistorialAdp
import com.example.wabu.databinding.ActivityHistorialBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private val adapter: HistorialAdp = HistorialAdp()

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Proyecto")

        //- SetUp RecyclerView
        val verticalLayoutManager = LinearLayoutManager(this@HistorialActivity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = verticalLayoutManager

        obtenerDatos()
    }

    private fun obtenerDatos() = with(binding) {

        val records = mutableListOf<HistorialRecord>()
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        snapshot.child("Historial").children.forEach { childSnapshot ->
                            childSnapshot.getValue(HistorialRecord::class.java)?.let { record ->
                                records.add(record)
                            }
                        }

                        adapter.setList(records)
                        recyclerView.adapter = adapter

                        adapter.onClickRecord = { record ->
                            Toast.makeText(this@HistorialActivity, "Litros: ${record.Litros}", Toast.LENGTH_SHORT).show()
                        }

                        Log.i("SMM_DEBUG", "HistorialList: $records")
                    } catch (e: Exception) {
                        Log.e("SMM_DEBUG", "Error al obtener los datos (TryCatch). Message: ${e.message}")
                    }
                } else {
                    Log.i("SMM_DEBUG", "No existe el nodo :(")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SMM_DEBUG", "Error: ${error.message}")
            }
        })
    }
}

// Nodo "Historial" //
data class HistorialRecord(
    val Litros: Double ?= null,
    val FechaHora: String ?= null
)

