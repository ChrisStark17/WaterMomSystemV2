package com.example.wabu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wabu.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    data class HistorialItem(
        val flujo: Int,
        val litros: Double,
        val porcentajeLlenado: Double
    )

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private val tinacoCapacidadLitros = 1100.0 // Capacidad total del tinaco en litros
    private val handler = Handler()
    private val updateInterval: Long = 30 * 1000 // 30 segundos

    // Lista para almacenar los datos del historial
    private val historialDatos: MutableList<HistorialItem> = mutableListOf()

    private val getDataRunnable = object : Runnable {
        override fun run() {
            obtenerDatos()
            handler.postDelayed(this, updateInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Proyecto")

        binding.apply {
            btnGetData.setOnClickListener {
                obtenerDatos()
            }
        }

        val btn: Button = findViewById(R.id.btn_historial)
        btn.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }

        // Comenzar la actualización automática de datos
        handler.postDelayed(getDataRunnable, updateInterval)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener la actualización automática de datos al destruir la actividad
        handler.removeCallbacks(getDataRunnable)
    }

    private fun obtenerDatos() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        val flow = snapshot.child("Flujo").getValue<Int>()
                        val tinaco = snapshot.child("Tinaco").getValue<Double>()

                        // Convertir la distancia en litros
                        val litros = convertirADistanciaEnLitros(tinaco ?: 0.0)

                        // Calcular porcentaje de llenado
                        val porcentajeLlenado = (litros / tinacoCapacidadLitros) * 100

                        binding.mtFlowValue.text = flow.toString()
                        binding.mtTimeValue.text = "%.2f%%".format(porcentajeLlenado) // Mostrar el porcentaje de llenado
                        binding.mtWaterTankValue.text = "${"%.3f".format(litros)} litros" // Redondear a tres números después del punto

                        // Mostrar la imagen correspondiente al porcentaje de llenado
                        actualizarImagenPorcentaje(porcentajeLlenado)

                        // Agregar entrada al historial
                        historialDatos.add(HistorialItem(flow ?: 0, litros, porcentajeLlenado))

                    } catch (e: Exception) {
                        Log.e("SMM_DEBUG", "Error al obtener los datos (TryCatch). Message: ${e.message}")
                        Toast.makeText(
                            this@MainActivity,
                            "Error al obtener los datos (TryCatch). Message: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.i("SMM_DEBUG", "No existe el nodo :(")
                    Toast.makeText(
                        this@MainActivity,
                        "No existe el nodo :(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SMM_DEBUG", "Error: ${error.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Función para convertir la distancia en litros
    private fun convertirADistanciaEnLitros(distanciaSensor: Double): Double {
        // Altura desde el sensor hasta el fondo del tinaco en cm
        var altura = distanciaSensor - 120

        // Multiplicar por -1 si la altura es menor que cero para invertir el valor
        if (altura < 0) {
            altura *= -1
        }

        // Calculando el volumen en cm^3
        val volumenCm3 = Math.PI * 55.0.pow(2) * altura

        // Convirtiendo a metros cúbicos
        val volumenM3 = volumenCm3 / 1000000

        // Convirtiendo a litros
        val volumenLitros = volumenM3 * 1000

        return volumenLitros
    }

    private fun actualizarImagenPorcentaje(porcentajeLlenado: Double) {
        val imageView = findViewById<ImageView>(R.id.imageView)

        // Determina qué imagen mostrar según el porcentaje de llenado
        val imagenResourceId = when {
            porcentajeLlenado >= 0 && porcentajeLlenado < 10 -> R.drawable.imagen_porcentaje_0_10
            porcentajeLlenado >= 10 && porcentajeLlenado < 20 -> R.drawable.imagen_porcentaje_10_20
            porcentajeLlenado >= 20 && porcentajeLlenado < 30 -> R.drawable.imagen_porcentaje_20_30
            porcentajeLlenado >= 30 && porcentajeLlenado < 40 -> R.drawable.imagen_porcentaje_30_40
            porcentajeLlenado >= 40 && porcentajeLlenado < 50 -> R.drawable.imagen_porcentaje_40_50
            porcentajeLlenado >= 50 && porcentajeLlenado < 60 -> R.drawable.imagen_porcentaje_50_60
            porcentajeLlenado >= 60 && porcentajeLlenado < 70 -> R.drawable.imagen_porcentaje_60_70
            porcentajeLlenado >= 70 && porcentajeLlenado < 80 -> R.drawable.imagen_porcentaje_70_80
            porcentajeLlenado >= 80 && porcentajeLlenado < 90 -> R.drawable.imagen_porcentaje_80_90
            porcentajeLlenado >= 90 && porcentajeLlenado <= 100 -> R.drawable.imagen_porcentaje_90_100
            else -> R.drawable.imagen_porcentaje_default // Por si acaso el porcentaje no cae en ninguno de los rangos especificados
        }

        // Asigna la imagen al ImageView
        imageView.setImageResource(imagenResourceId)
    }
}
