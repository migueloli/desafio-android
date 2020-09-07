package br.com.boxdelivery.github_javapop.model

class GithubRepositoryModel(
    val name : String,
    val description : String,
    val stargazers_count : Int,
    val forks : Int,
    val owner : GithubUserModel,
) {}