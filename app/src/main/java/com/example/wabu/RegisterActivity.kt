package com.example.wabu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        val buttonRegistrar = findViewById<Button>(R.id.btnRegistrarse)
        buttonRegistrar.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.editTextNombre).text.toString()
            val apellido = findViewById<EditText>(R.id.editTextApellido).text.toString()
            val correo = findViewById<EditText>(R.id.editTextCorreo).text.toString()
            val contraseña = findViewById<EditText>(R.id.editTextContraseña).text.toString()

            registrarUsuario(correo, contraseña)
        }

        val btn = findViewById<Button>(R.id.btn_Encuesta)
        btn.setOnClickListener {
            val intent = Intent(this, EncuestaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registrarUsuario(correo: String, contraseña: String) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    mostrarMensajeRegistroExitoso()

                    // Redirigir a otra actividad
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                    finish() // Finalizar esta actividad para que el usuario no pueda volver atrás

                    // Aquí podrías agregar lógica adicional si deseas realizar alguna acción adicional después del registro exitoso
                } else {
                    // Error en el registro
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        // Si el usuario ya existe, mostrar un mensaje de error
                        Toast.makeText(applicationContext, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
                    } else {
                        // Otro tipo de error
                        Toast.makeText(applicationContext, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun mostrarMensajeRegistroExitoso() {
        Toast.makeText(applicationContext, "Registro Exitoso!", Toast.LENGTH_SHORT).show()
    }
}

