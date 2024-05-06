package com

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wabu.MainActivity
import com.example.wabu.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val buttonIniciarSesion = findViewById<Button>(R.id.btn_Iniciar_Sesion)
        buttonIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        val buttonRegistrarse = findViewById<Button>(R.id.btn_registrarse)
        buttonRegistrarse.setOnClickListener {
            // Aquí especifica la actividad a la que deseas navegar al hacer clic en el botón "Registrarse"
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Recuperar el correo electrónico y contraseña almacenados si existen y autocompletar los campos correspondientes
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")

        findViewById<EditText>(R.id.editTextCorreoInicioSesion).setText(savedEmail)
        findViewById<EditText>(R.id.editTextContraseñaInicioSesion).setText(savedPassword)
    }

    private fun iniciarSesion() {
        val correo = findViewById<EditText>(R.id.editTextCorreoInicioSesion).text.toString()
        val contraseña = findViewById<EditText>(R.id.editTextContraseñaInicioSesion).text.toString()

        mAuth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autenticación exitosa, guardar el correo electrónico y contraseña en SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("email", correo)
                    editor.putString("password", contraseña)
                    editor.apply()

                    // Ir a la siguiente actividad
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar esta actividad para que el usuario no pueda volver atrás
                } else {
                    // Error en la autenticación
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
    }
}