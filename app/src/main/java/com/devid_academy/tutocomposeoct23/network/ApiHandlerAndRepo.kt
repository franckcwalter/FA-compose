package com.devid_academy.tutocomposeoct23.network

import com.devid_academy.tutocomposeoct23.NetworkResult
import retrofit2.HttpException
import retrofit2.Response


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

    suspend fun register(
        login: String,
        mdp: String
    ): NetworkResult<ResponseRegisterOrLoginDto>
    {
        return handleApi { apiInterface.register(RegisterDto(login, mdp)) }
    }

    suspend fun login(
        login: String,
        mdp : String
    ): NetworkResult<ResponseRegisterOrLoginDto>
    {
        return handleApi { apiInterface.login(login,mdp) }
    }

    suspend fun getArticles(token : String)
    : NetworkResult<List<ArticleDto>>
    {
        return handleApi { apiInterface.getArticles(token) }
    }

    suspend fun getArticle(
        articleId : Long,
        token : String
    ) : NetworkResult<ArticleDto>
    {
        return handleApi { apiInterface.getArticle(token, articleId) }
    }

    suspend fun createArticle(
        token : String,
        idU: Long,
        title: String,
        desc: String,
        image: String,
        cat: Int
    ): NetworkResult<Unit>
    {
        return handleApi { apiInterface.createArticle(token,
                            NewArticleDto(idU, title, desc, image, cat)) }
    }

    suspend fun updateArticle(
        articleId : Long,
        token : String,
        title : String,
        desc : String,
        image : String,
        cat : Int
    ): NetworkResult<Unit>
    {
        return handleApi { apiInterface.updateArticle(articleId, token,
                            UpdateArticleDto(articleId,title,desc, image, cat)) }
    }
    suspend fun deleteArticle(
        idArticle : Long,
        token : String
    ) : NetworkResult<Unit>
    {
        return handleApi { apiInterface.deleteArticle(idArticle, token) }
    }
}

