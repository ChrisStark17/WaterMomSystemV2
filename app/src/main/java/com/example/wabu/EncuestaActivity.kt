package com.example.wabu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EncuestaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuesta)

        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            if (todasLasRespuestasMarcadas()) {
                enviarRespuestas()
            } else {
                Toast.makeText(this, "Por favor, responde todas las preguntas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun todasLasRespuestasMarcadas(): Boolean {
        val radioGroupQuestion1 = findViewById<RadioGroup>(R.id.radioGroupQuestion1)
        val radioGroupQuestion2 = findViewById<RadioGroup>(R.id.radioGroupQuestion2)
        val radioGroupQuestion3 = findViewById<RadioGroup>(R.id.radioGroupQuestion3)
        val radioGroupQuestion4 = findViewById<RadioGroup>(R.id.radioGroupQuestion4)
        val radioGroupQuestion5 = findViewById<RadioGroup>(R.id.radioGroupQuestion5)

        return (radioGroupQuestion1.checkedRadioButtonId != -1 &&
                radioGroupQuestion2.checkedRadioButtonId != -1 &&
                radioGroupQuestion3.checkedRadioButtonId != -1 &&
                radioGroupQuestion4.checkedRadioButtonId != -1 &&
                radioGroupQuestion5.checkedRadioButtonId != -1)
    }

    private fun enviarRespuestas() {
        Toast.makeText(this, "Respuestas Enviadas", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
