package com.example.lamudanzadelosgatos

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.example.lamudanzadelosgatos.databinding.ActivityMainBinding
import java.lang.Math.abs

class PosicionBoton(var indice: Int,var idtabla: Int,val idboton: Int){

}
class MainActivity : AppCompatActivity() {

    private val boton1 = PosicionBoton(0,R.id.Tabla0,R.id.imageButton1)
    private val botonRef = PosicionBoton(1,R.id.Tabla0,R.id.imageButtonRef)
    private val botonA = PosicionBoton(2,R.id.Tabla0,R.id.imageButtonA)
    private val boton2 = PosicionBoton(0,R.id.Tabla1,R.id.imageButton2)
    private val boton3 = PosicionBoton(1,R.id.Tabla1,R.id.imageButton3)
    private val botonB = PosicionBoton(2,R.id.Tabla1,R.id.imageButtonB)
    private var botonAnterior : PosicionBoton? = null
    private var botonActual : PosicionBoton? = null
    var numeroDeMovimientos :  Int = 0

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }
    fun obtenerPosicionBoton(view: View):PosicionBoton{
        if (view.id==botonA.idboton){
            return botonA
        }
        else if(view.id==botonRef.idboton){
            return botonRef
        }
        else if(view.id==boton1.idboton){
            return boton1
        }
        else if(view.id==boton2.idboton){
            return boton2
        }
        else if(view.id==boton3.idboton){
            return boton3
        }
        else
            return botonB
    }
    fun condicionesDeInteraccion(view: View){

        if (botonAnterior == null){
            botonAnterior = obtenerPosicionBoton(view)
        }
        else{
            botonActual = obtenerPosicionBoton(view)
            if(botonAnterior == botonActual){
                botonAnterior = null
                return
            }
            if(botonAnterior != botonRef && botonActual != botonRef){
                Log.d("MainActivity",view.toString())
                botonAnterior = null
                return
            }
            val distancia: Int = abs(botonAnterior!!.indice - botonActual!!.indice)
            val cambioTabla = botonAnterior!!.idtabla != botonActual!!.idtabla
            if(distancia > 1){
                botonAnterior = null
                return
            }
            if (cambioTabla && (distancia != 0)){
                botonAnterior = null
                return
            }
            if (cambioTabla){
                movimientoVertical(botonAnterior!!,botonActual!!)
            }
            else
                movimientoHorizontal(botonAnterior!!,botonActual!!)

            numeroDeMovimientos += 1
            findViewById<TextView>(R.id.numeroDeMovimientos).setText("Número de Movimientos: " + numeroDeMovimientos)
            revisarJuego()
            botonAnterior = null
        }
    }
    fun movimientoHorizontal(posicion1: PosicionBoton, posicion2: PosicionBoton){
        val tabla = findViewById<TableRow>(posicion1.idtabla)
        val boton = findViewById<ImageButton>(posicion1.idboton)
        val posicionAnterior = posicion1.indice
        tabla.removeView(boton)
        tabla.addView(boton,posicion2.indice)
        posicion1.indice = posicion2.indice
        posicion2.indice = posicionAnterior
    }
    fun movimientoVertical(posicion1: PosicionBoton,posicion2: PosicionBoton){
        val tabla1 = findViewById<TableRow>(posicion1.idtabla)
        val tabla2 = findViewById<TableRow>(posicion2.idtabla)
        val boton1 = findViewById<ImageButton>(posicion1.idboton)
        val boton2 = findViewById<ImageButton>(posicion2.idboton)
        val posicionTabla = posicion1.idtabla
        tabla1.removeView(boton1)
        tabla2.removeView(boton2)
        tabla1.addView(boton2,posicion1.indice)
        tabla2.addView(boton1,posicion2.indice)
        posicion1.idtabla = posicion2.idtabla
        posicion2.idtabla = posicionTabla
    }
    fun revisarJuego(){
        val condicionTabla = botonA.idtabla == R.id.Tabla1 && botonB.idtabla == R.id.Tabla0
        val condicionIndice = botonA.indice == 2 && botonB.indice == 2
        if (condicionIndice && condicionTabla){
            if(numeroDeMovimientos == 17){
                val toast =  Toast.makeText(applicationContext,"¡ Has Ganado en el menor número de Movimientos, QUE CRACK !",Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val toast =  Toast.makeText(applicationContext,"¡ Has Ganado !",Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}