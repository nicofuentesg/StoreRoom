package com.example.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import androidx.annotation.UiThread
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() , OnClickListeners, MainAuxiliar{

    private lateinit var binding: ActivityMainBinding

    private lateinit var mAdapter: TiendaAdapter
    private lateinit var mGridLayout: GridLayoutManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iniciarRV()

        /*binding.botonSave.setOnClickListener {
           var tienda = Tienda(nombreDeLaTienda = binding.etNombre.text.toString().trim())

            //Hacemos correr el proceso en el segundo hilo para que no se complique y tarde el hilo principla
            Thread{
                TiendaAplicacion.database.tiendaDao().addStore(tienda)
            }.start()

            mAdapter.add(tienda)
        }*/

        binding.fab.setOnClickListener {
            iniciarFragmento()
        }

    }

    private fun iniciarFragmento(args: Bundle? = null) {
        val fragment = AddTiendaFragment()

        if(args != null)fragment.arguments = args

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.contenedorPrincipal,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        hidefab()
    }

    private fun iniciarRV() {
        mAdapter =  TiendaAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,2)
        getTiendas()

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }


    }
    //Hacemos consulta para obtener todas las tiendas ya conseguidas
     fun getTiendas(){
        doAsync {
            val tiendas = TiendaAplicacion.database.tiendaDao().getAllStores()
            uiThread {
                mAdapter.setStore(tiendas)
            }


        }


    }

    /**
    *Interfaz de el metodo click
    **/
    override fun onClick(tiendaId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id),tiendaId)
        iniciarFragmento(args)
    }

    override fun tiendaFavorita(tienda: Tienda) {
        tienda.favorita = !tienda.favorita
        doAsync {
            TiendaAplicacion.database.tiendaDao().updateStore(tienda)
            uiThread { 
                mAdapter.actualizar(tienda)
            }
        }
    }

    override fun eliminarTienda(tienda: Tienda) {
        doAsync {
            TiendaAplicacion.database.tiendaDao().deleteStore(tienda)
            uiThread {
            mAdapter.borrar(tienda)
            }
        }
    }

    override fun hidefab(visible: Boolean) {
        if(visible) binding.fab.show() else binding.fab.hide()
        super.hidefab(visible)
    }

    //Actualizamos la tienda a traves del fragmento
    override fun add(tienda: Tienda) {
        mAdapter.add(tienda)
        super.add(tienda)
    }

    override fun actualizarTienda(tienda: Tienda) {
        mAdapter.actualizar(tienda)
    }



}