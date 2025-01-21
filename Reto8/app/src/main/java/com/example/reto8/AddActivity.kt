package com.example.reto8

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {

        private lateinit var nombreInput: EditText
        private lateinit var urlInput: EditText
        private lateinit var telefonoInput: EditText
        private lateinit var mailInput: EditText
        private lateinit var productoInput: EditText
        private lateinit var spinner: Spinner

        private lateinit var addButton: Button


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        // Encuentra el Spinner por su ID
        val spinner: Spinner = findViewById(R.id.spinner)

        nombreInput = findViewById(R.id.nombre_input)
        urlInput = findViewById(R.id.url_input)
        telefonoInput = findViewById(R.id.telefono_input)
        mailInput = findViewById(R.id.mail_input)
        productoInput = findViewById(R.id.producto_input)
        addButton = findViewById(R.id.add_button)

            addButton.setOnClickListener {
            val myDB = MyDatabaseHelper(this);
                myDB.addEmpresa(
                    nombreInput.text.toString().trim(),
                    urlInput.text.toString().trim(),
                    telefonoInput.text.toString().trim(),
                    mailInput.text.toString().trim(),
                    productoInput.text.toString().trim(),
                    spinner.selectedItem.toString().trim()
                );

            }
        // Crea una lista de elementos para el Spinner
        val items = listOf("consultoría,", "desarrollo a la medida", "fábrica de software")

        // Crea un ArrayAdapter para poblar el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asigna el adaptador al Spinner
        spinner.adapter = adapter

        // Agrega un listener para manejar la selección
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position) as String
                    Toast.makeText(this@AddActivity, "Seleccionado: $selectedItem", Toast.LENGTH_SHORT).show()
                    // Aquí puedes realizar acciones adicionales con el elemento seleccionado
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se seleccionó ningún elemento
                }


        }
    }
}