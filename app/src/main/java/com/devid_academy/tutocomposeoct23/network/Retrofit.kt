package com.devid_academy.tutocomposeoct23.network

import com.devid_academy.tutocomposeoct23.NetworkResult
import com.squareup.moshi.Json
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


object ApiRoutes {

    const val BASE_URL = "https://dev.dev-id.fr/formation/api_pro/"
    const val USER  = "articles/user/"
    const val ARTICLES = "articles/"
    const val ARTICLES_WITH_ID = "articles/{id}"

}

interface ApiInterface {

    @PUT(ApiRoutes.USER)
    suspend fun register(
        @Body registerDto : RegisterDto
    ) : Response<ResponseRegisterOrLoginDto>

    @FormUrlEncoded
    @POST(ApiRoutes.USER)
    suspend fun login(
        @Field("login") login : String,
        @Field("mdp") mdp : String
    ) : Response<ResponseRegisterOrLoginDto>

    @GET(ApiRoutes.ARTICLES)
    suspend fun getArticles(
        @Header("token") token : String
    ) : Response<List<ArticleDto>>

    @GET(ApiRoutes.ARTICLES_WITH_ID)
    suspend fun getArticle(
        @Header("token") token : String,
        @Path("id") articleId : Long
    ) : Response<ArticleDto>

    @POST(ApiRoutes.ARTICLES_WITH_ID)
    suspend fun updateArticle(
        @Path("id") articleId : Long,
        @Header("token") token : String,
        @Body updateArticleDto: UpdateArticleDto
    ) : Response<Unit>

    @DELETE(ApiRoutes.ARTICLES_WITH_ID)
    suspend fun deleteArticle(
        @Path("id") articleId : Long,
        @Header("token") token : String
    ) : Response<Unit>

    @PUT(ApiRoutes.ARTICLES)
    suspend fun createArticle(
        @Header("token") token : String,
        @Body newArticleDto: NewArticleDto
    ) : Response<Unit>
}






