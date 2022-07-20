package com.example.stores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView

class TiendaAdapter(var tiendas: MutableList<Tienda>, private var listener:OnClickListeners): RecyclerView.Adapter<TiendaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiendaViewHolder {
        return TiendaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false))
    }

    override fun onBindViewHolder(holder: TiendaViewHolder, position: Int) {
       val item = tiendas[position]
        holder.probar(item)
        with(holder.binding.root){
            setOnClickListener { listener.onClick(item.id) }


            setOnLongClickListener {
                listener.eliminarTienda(item)
                true  }

        }
        holder.binding.cbFavorito.setOnClickListener { listener.tiendaFavorita(item) }
    }

    override fun getItemCount(): Int = tiendas.size



    fun add(tienda: Tienda) {

        //Nos refresca toda la vista
        if (!tiendas.contains(tienda)){
            tiendas.add(tienda)
            notifyItemChanged(tiendas.size - 1)
        }


    }

    fun setStore(tiendas: MutableList<Tienda>) {
        this.tiendas = tiendas
        notifyDataSetChanged()

    }

    fun actualizar(tienda: Tienda) {
        val index = tiendas.indexOf(tienda)

        //Significa que lo encontro
        if (index != -1){
            tiendas.set(index,tienda)
            notifyItemChanged(index)

    }

    }

    fun borrar(tienda: Tienda) {
        val index = tiendas.indexOf(tienda)

        //Significa que lo encontro
        if (index != -1){
            tiendas.removeAt(index)
            notifyItemRemoved(index)

        }

    }

    interface OnClickListener


}