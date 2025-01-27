package com.example.reto8

import android.app.ComponentCaller
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var customAdapter: CustomAdapter
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var addButton: FloatingActionButton
    private lateinit var myDB: MyDatabaseHelper


    private val empresaID = mutableListOf<String>()
    private val empresaNombre = mutableListOf<String>()
    private val empresaUrl = mutableListOf<String>()
    private val empresaTelefono = mutableListOf<String>()
    private val empresaMail = mutableListOf<String>()
    private val empresaProducto = mutableListOf<String>()
    private val empresaClase = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchInput = findViewById(R.id.search_input)
        addButton = findViewById(R.id.add_button)
        myDB = MyDatabaseHelper(this)


        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivityForResult(intent, 1) // Use startActivityForResult for data from AddActivity
        }

        dbHelper = MyDatabaseHelper(this)

        // Cargar datos desde la base de datos
        loadDataFromDatabase()

        // Configurar el adaptador y RecyclerView
        customAdapter = CustomAdapter(
            this,
            this,
            empresaID,
            empresaNombre,
            empresaUrl,
            empresaTelefono,
            empresaMail,
            empresaProducto,
            empresaClase
        )
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar el filtro de búsqueda
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                customAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        // Limpia las listas actuales para evitar datos duplicados
        empresaID.clear()
        empresaNombre.clear()
        empresaUrl.clear()
        empresaTelefono.clear()
        empresaMail.clear()
        empresaProducto.clear()
        empresaClase.clear()

        // Lee los datos actualizados de la base de datos
        val cursor = myDB.readAllData()
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                empresaID.add(cursor.getString(0))
                empresaNombre.add(cursor.getString(1))
                empresaUrl.add(cursor.getString(2))
                empresaTelefono.add(cursor.getString(3))
                empresaMail.add(cursor.getString(4))
                empresaProducto.add(cursor.getString(5))
                empresaClase.add(cursor.getString(6))
            }
        }

        // Notifica al adaptador que los datos han cambiado
        customAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 ) {
            // Recargar los datos después de crear, eliminar o actualizar un elemento
            recreate()
        }
    }

    private fun loadDataFromDatabase() {
        val cursor: Cursor = dbHelper.readAllData()
        if (cursor.count == 0) {
            Toast.makeText(this, "No hay datos disponibles", Toast.LENGTH_SHORT).show()
            return
        }
        while (cursor.moveToNext()) {
            empresaID.add(cursor.getString(0)) // _id
            empresaNombre.add(cursor.getString(1)) // nombre
            empresaUrl.add(cursor.getString(2)) // url
            empresaTelefono.add(cursor.getString(3)) // telefono
            empresaMail.add(cursor.getString(4)) // mail
            empresaProducto.add(cursor.getString(5)) // producto
            empresaClase.add(cursor.getString(6)) // clase
        }
    }
}


