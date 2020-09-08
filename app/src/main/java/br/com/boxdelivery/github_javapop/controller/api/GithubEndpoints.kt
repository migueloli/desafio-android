package br.com.boxdelivery.github_javapop.controller.api

import br.com.boxdelivery.github_javapop.model.GithubBody
import br.com.boxdelivery.github_javapop.model.GithubPullRequestModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubEndpoints {

    @GET("search/repositories?q=language:Java&sort=stars")
    fun getRepositories(
        @Query("page") page : Int,
    ) : Call<GithubBody>

    @GET("repos/{user}/{repo}/pulls")
    fun getPullRequests(
        @Path("user", encoded = true) user : String,
        @Path("repo", encoded = true) repo : String,
    ) : Call<MutableList<GithubPullRequestModel>>

}