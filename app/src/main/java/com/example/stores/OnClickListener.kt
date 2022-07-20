package com.example.stores

interface OnClickListeners {

    fun onClick(tiendaId: Long){

    }

    fun tiendaFavorita(tienda: Tienda){

    }

    fun actualizarTienda(tienda: Tienda){

    }

    fun eliminarTienda(tienda: Tienda){

    }


}