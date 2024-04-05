package dev.borisochieng.gitrack.ui.models

import android.os.Parcel
import android.os.Parcelable

data class SingleIssueParcelable(
    val repoName: String,
    val repoOwner: String,
    val number: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(repoName)
        dest.writeString(repoOwner)
        dest.writeInt(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SingleIssueParcelable> {
        override fun createFromParcel(parcel: Parcel): SingleIssueParcelable {
            return SingleIssueParcelable(parcel)
        }

        override fun newArray(size: Int): Array<SingleIssueParcelable?> {
            return arrayOfNulls(size)
        }
    }

}