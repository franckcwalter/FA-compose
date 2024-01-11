package com.devid_academy.tutocomposeoct23.network

import com.squareup.moshi.Json

data class RegisterDto(
    @Json(name = "login")
    val login: String,
    @Json(name = "mdp")
    val mdp: String
)

data class ResponseRegisterOrLoginDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "token")
    val token: String
)

/** ????  **/
class GetArticlesDto : ArrayList<ArticleDto>()

data class ArticleDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "titre")
    val titre: String,
    @Json(name = "descriptif")
    val descriptif: String,
    @Json(name = "url_image")
    val urlImage: String,
    @Json(name = "categorie")
    val categorie: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "id_u")
    val idU: Long
)


data class UpdateArticleDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "cat")
    val cat: Int
)

data class NewArticleDto(
    @Json(name = "id_u")
    val idU: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "cat")
    val cat: Int
)
