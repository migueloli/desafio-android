package br.com.boxdelivery.github_javapop.model

import java.util.*

data class GithubPullRequestModel(
    val title : String,
    val body : String,
    val html_url : String,
    val created_at : Date,
    val user : GithubUserModel,
) {}