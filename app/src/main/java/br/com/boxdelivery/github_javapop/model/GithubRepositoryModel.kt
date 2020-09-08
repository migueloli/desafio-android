package br.com.boxdelivery.github_javapop.model

import android.os.Parcel
import android.os.Parcelable

data class GithubRepositoryModel(
    val name : String,
    val description : String,
    val stargazers_count : Int,
    val forks : Int,
    val owner : GithubUserModel,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable<GithubUserModel>(
            GithubUserModel::class.java.classLoader
        ) ?: GithubUserModel("", "")
    ) {}

    fun getTitle() = "${owner.login}/$name"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(stargazers_count)
        parcel.writeInt(forks)
        parcel.writeParcelable(owner, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<GithubRepositoryModel> {
        override fun createFromParcel(parcel: Parcel): GithubRepositoryModel {
            return GithubRepositoryModel(parcel)
        }

        override fun newArray(size: Int): Array<GithubRepositoryModel?> {
            return arrayOfNulls(size)
        }
    }
}