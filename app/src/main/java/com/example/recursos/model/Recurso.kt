package com.example.recursos.model

import android.os.Parcel
import android.os.Parcelable

data class Recurso(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val enlace: String,
    val imagen: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeString(enlace)
        parcel.writeString(imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recurso> {
        override fun createFromParcel(parcel: Parcel): Recurso {
            return Recurso(parcel)
        }

        override fun newArray(size: Int): Array<Recurso?> {
            return arrayOfNulls(size)
        }
    }
}
