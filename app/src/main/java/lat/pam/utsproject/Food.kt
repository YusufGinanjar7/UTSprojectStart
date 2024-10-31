package lat.pam.utsproject

import android.os.Parcel
import android.os.Parcelable

data class Food(
    val name: String,
    val description: String,
    val imageResourceId: Int,
    var quantity: Int = 0 // Menyimpan jumlah makanan
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt() // Mengambil quantity
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(imageResourceId)
        parcel.writeInt(quantity) // Menyimpan quantity
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}
