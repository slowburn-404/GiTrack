package dev.borisochieng.gitrack.ui.models

import android.os.Parcel
import android.os.Parcelable

data class RepositoryParcelable(
    val id: String,
    val title: String,
    val username: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RepositoryParcelable> {
        override fun createFromParcel(parcel: Parcel): RepositoryParcelable {
            return RepositoryParcelable(parcel)
        }

        override fun newArray(size: Int): Array<RepositoryParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
