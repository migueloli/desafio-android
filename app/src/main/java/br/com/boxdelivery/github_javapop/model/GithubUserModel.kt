package br.com.boxdelivery.github_javapop.model

import android.os.Parcel
import android.os.Parcelable

data class GithubUserModel(
    val login: String,
    val avatar_url: String,
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(login)
        writeString(avatar_url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GithubUserModel> =
            object : Parcelable.Creator<GithubUserModel> {
                override fun createFromParcel(source: Parcel): GithubUserModel =
                    GithubUserModel(source)

                override fun newArray(size: Int): Array<GithubUserModel?> = arrayOfNulls(size)
            }
    }
}