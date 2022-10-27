package com.example.cookroom.models

import android.os.Parcel
import android.os.Parcelable

class ProdItem() : Parcelable{
    var id : Int? = 0
    var title : String? = null
    var category :String? = null
    var amount :Double? = null
    var measure: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        title = parcel.readString()
        category = parcel.readString()
        amount = parcel.readValue(Int::class.java.classLoader) as? Double
        measure = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeValue(amount)
        parcel.writeString(measure)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProdItem> {
        override fun createFromParcel(parcel: Parcel): ProdItem {
            return ProdItem(parcel)
        }

        override fun newArray(size: Int): Array<ProdItem?> {
            return arrayOfNulls(size)
        }
    }
}