package com.example.reto8

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UpdateActivity : AppCompatActivity() {

    private lateinit var nombreInput: EditText
    private lateinit var urlInput: EditText
    private lateinit var telefonoInput: EditText
    private lateinit var mailInput: EditText
    private lateinit var productoInput: EditText
    private lateinit var spinner: Spinner

    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    private var id: String? = null
    private var nombre: String? = null
    private var url: String? = null
    private var telefono: String? = null
    private var mail: String? = null
    private var producto: String? = null
    private var clase: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update)



        nombreInput = findViewById(R.id.nombre_input2)
        urlInput = findViewById(R.id.url_input2)
        telefonoInput = findViewById(R.id.telefono_input2)
        mailInput = findViewById(R.id.mail_input2)
        productoInput = findViewById(R.id.producto_input2)
        spinner = findViewById(R.id.spinner2)

        getAndSetIntentData()

        actionBar?.setTitle("Actualizar ")


        updateButton = findViewById(R.id.update_button)
        updateButton.setOnClickListener {
            val  myDB =  MyDatabaseHelper(this);
            myDB.updateData(id.toString(),nombreInput.text.toString().trim(),urlInput.text.toString().trim(),telefonoInput.text.toString().trim(),mailInput.text.toString().trim(),productoInput.text.toString().trim(),spinner.selectedItem.toString().trim())


        }

        deleteButton = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            val  myDB =  MyDatabaseHelper(this);
            confirmDialog()
        }

        val items = listOf("consultoría,", "desarrollo a la medida", "fábrica de software")
        // Crea un ArrayAdapter para poblar el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        spinner.adapter = adapter






    }




    private fun getAndSetIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("nombre") && intent.hasExtra("url") &&
            intent.hasExtra("telefono") && intent.hasExtra("mail") && intent.hasExtra("producto") && intent.hasExtra("clase")) {
            // Getting Data from Intent
            id = intent.getStringExtra("id")
            nombre = intent.getStringExtra("nombre")
            url = intent.getStringExtra("url")
            telefono = intent.getStringExtra("telefono")
            mail = intent.getStringExtra("mail")
            producto = intent.getStringExtra("producto")
            clase = intent.getStringExtra("clase")

            // Setting Intent Data
            nombreInput.setText(nombre)
            urlInput.setText(url)
            telefonoInput.setText(telefono)
            mailInput.setText(mail)
            productoInput.setText(producto)

            val items = listOf("Opción 1", "Opción 2", "Opción 3")
            // Crea un ArrayAdapter para poblar el Spinner
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            spinner.adapter = adapter
            val position = items.indexOf(clase)
            spinner.setSelection(position)

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDialog(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar")
        builder.setMessage("¿Estas seguro de eliminar?")
        builder.setPositiveButton("Si"){dialog, which ->
            val  myDB =  MyDatabaseHelper(this);
            myDB.deleteOneRow(id.toString())
            finish()
        }
        builder.setNegativeButton("No"){dialog, which ->{}


        }
        builder.create().show()



    }

}