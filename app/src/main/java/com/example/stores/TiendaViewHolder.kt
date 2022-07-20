package com.example.stores

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.databinding.ItemListBinding

class TiendaViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemListBinding.bind(view)

    fun probar(tienda: Tienda){
        binding.tvNombreDeTiendas.text = tienda.nombreDeLaTienda
        binding.cbFavorito.isChecked = tienda.favorita

        Glide.with(binding.ivTiendas.context)
            .load(tienda.fotoUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivTiendas)
    }

}