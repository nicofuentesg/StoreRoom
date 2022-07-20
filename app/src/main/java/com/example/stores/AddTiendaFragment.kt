package com.example.stores

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.room.Database
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.databinding.FragmentAddTiendaBinding
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class AddTiendaFragment : Fragment() {

    private var _binding: FragmentAddTiendaBinding? = null
    private val binding get() = _binding!!

    private var misEditMode: Boolean = false
    private var mTiendaEntity: Tienda? = null

    private var mActivity: MainActivity?  = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTiendaBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    //Utilizamos el onViewCreated cuando todos los elementos ya fueron creados, aqui nos aseguramos que ningun elemento sera nulo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = activity as? MainActivity

        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.add_tienda)

        val id = arguments?.getLong(getString(R.string.arg_id),0)
        if( id != null && id !=  0L){
           misEditMode = true
            getStore(id)
        }else {
            misEditMode = false
            mTiendaEntity = Tienda(nombreDeLaTienda = "", telefono = "", fotoUrl = "")
        }

        //Esta linea quiere decir que el framento tome el control del menu
        setHasOptionsMenu(true)

        binding.etFotoUrl.addTextChangedListener {
            Glide.with(this).load(binding.etFotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivFoto)
        }


    }

    private fun getStore(id: Long) {

        doAsync {
            mTiendaEntity = TiendaAplicacion.database.tiendaDao().getStorebyId(id)
            uiThread { if (mTiendaEntity != null ) setUiTienda(mTiendaEntity!! ) }

        }
    }

    private fun setUiTienda(tiendaEntity: Tienda) {
        binding.apply {
            etNombreTienda.setText(tiendaEntity.nombreDeLaTienda)
            etNumeroTelefono.setText(tiendaEntity.telefono)
            etUrlSitioWeb.setText(tiendaEntity.sitioWeb)
            etFotoUrl.setText(tiendaEntity.fotoUrl)
            Glide.with(mActivity!!)
                .load(tiendaEntity.fotoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(ivFoto)
        }
    }

    //Al sobreescribir este metodo decimos que se muestre el menu en el fragmetp
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //Esto es cuando seleccionamos el menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save->{
            if (mTiendaEntity != null){
                /*var tienda = Tienda(
            nombreDeLaTienda = binding.etNombreTienda.text.toString().trim(),
            telefono = binding.etNumeroTelefono.text.toString().trim(),
        fotoUrl = binding.etFotoUrl.text.toString().trim())*/
                with(mTiendaEntity!!){
                    binding.etNombreTienda.text.toString().trim()
                    telefono = binding.etNumeroTelefono.text.toString().trim()
                    fotoUrl = binding.etFotoUrl.text.toString().trim()

                }
                doAsync {
                    if (misEditMode)  TiendaAplicacion.database.tiendaDao().updateStore(mTiendaEntity!!)
                    else mTiendaEntity!!.id = TiendaAplicacion.database.tiendaDao().addStore(mTiendaEntity!!)

                    uiThread {
                        hideKeyboard()
                        if (misEditMode){
                            mActivity?.actualizarTienda(mTiendaEntity!!)

                            Toast.makeText(mActivity,R.string.actualizar_tienda,Toast.LENGTH_LONG)
                                .show()
                        }else{

                            mActivity?.add(mTiendaEntity!!)
                            Toast.makeText(mActivity,"Guardamos Exitosamente la tienda",Toast.LENGTH_LONG).show()
                            mActivity?.onBackPressed()
                        }

                    }
                }

            }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    //return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mActivity?.hidefab(true)
        super.onDestroy()
    }


    fun hideKeyboard(){
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken,0)

    }

}