package dev.borisochieng.gitrack.presentation.models

import android.os.Parcel
import android.os.Parcelable

data class RepositoryIssuesParcelable(
    val databaseID: Int,
    val name: String,
    val owner: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(databaseID)
        parcel.writeString(name)
        parcel.writeString(owner)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RepositoryIssuesParcelable> {
        override fun createFromParcel(parcel: Parcel): RepositoryIssuesParcelable {
            return RepositoryIssuesParcelable(parcel)
        }

        override fun newArray(size: Int): Array<RepositoryIssuesParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
