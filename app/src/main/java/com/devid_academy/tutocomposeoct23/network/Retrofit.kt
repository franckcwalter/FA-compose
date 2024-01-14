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

interface ApiHandler {
    suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): NetworkResult<T> {
        return try {
            val response = execute()

            if (response.isSuccessful) {
                NetworkResult.Success(response.code(), response.body()!!)
            } else {
                NetworkResult.Error(response.code(), response.errorBody()?.string())
            }
        } catch (e: HttpException) {
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }
}


class Repository(private val apiInterface: ApiInterface) : ApiHandler {

    suspend fun register(login: String, mdp: String): NetworkResult<ResponseRegisterOrLoginDto> {
        return handleApi { apiInterface.register(RegisterDto(login, mdp)) }
    }
    suspend fun login(login: String, mdp : String) : NetworkResult<ResponseRegisterOrLoginDto>{
        return handleApi { apiInterface.login(login,mdp) }
    }

    suspend fun getArticles(token : String) : NetworkResult<List<ArticleDto>>{
        return handleApi { apiInterface.getArticles(token) }
    }
    suspend fun getArticle(articleId : Long, token : String) : NetworkResult<ArticleDto>{
        return handleApi { apiInterface.getArticle(token, articleId) }
    }

    suspend fun createArticle(token : String, idU: Long, title: String, desc: String, image: String, cat: Int) : NetworkResult<Unit>{
        return handleApi { apiInterface.createArticle(token, NewArticleDto(idU, title, desc, image, cat)) }
    }
    suspend fun updateArticle(articleId : Long, token : String, title : String, desc : String, image : String, cat : Int) : NetworkResult<Unit>{
        return handleApi { apiInterface.updateArticle(articleId, token, UpdateArticleDto(articleId,title,desc, image, cat)) }
    }
    suspend fun deleteArticle(idArticle : Long, token : String) : NetworkResult<Unit>{
        return handleApi { apiInterface.deleteArticle(idArticle, token) }
    }
}

