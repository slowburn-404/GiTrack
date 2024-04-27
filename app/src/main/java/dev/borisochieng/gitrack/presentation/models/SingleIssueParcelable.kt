package dev.borisochieng.gitrack.presentation.models

import android.os.Parcel
import android.os.Parcelable

data class SingleIssueParcelable(
    val repoName: String,
    val repoOwner: String,
    val issueNumber: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(repoName)
        dest.writeString(repoOwner)
        dest.writeInt(issueNumber)
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