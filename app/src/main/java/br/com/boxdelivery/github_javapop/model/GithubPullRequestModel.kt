package br.com.boxdelivery.github_javapop.model

class GithubPullRequestModel(
    val title : String,
    val body : String,
    val html_url : String,
    val created_at : String,
    val user : GithubUserModel,
) {}