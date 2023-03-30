package com.example.github_repository.network

import com.example.github_repository.model.Repo
import com.example.github_repository.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @Headers("Authorization: Bearer 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")
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<UserDto>

    @Headers("Authorization: Bearer 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")
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String): Call<Repo>
}