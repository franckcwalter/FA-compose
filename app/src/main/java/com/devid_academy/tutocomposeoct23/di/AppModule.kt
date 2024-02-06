package com.devid_academy.tutocomposeoct23.di

import android.content.Context
import android.content.SharedPreferences
import com.devid_academy.tutocomposeoct23.MyPrefs
import com.devid_academy.tutocomposeoct23.network.ApiHandler
import com.devid_academy.tutocomposeoct23.network.ApiInterface
import com.devid_academy.tutocomposeoct23.network.ApiRoutes
import com.devid_academy.tutocomposeoct23.network.RegisterDto
import com.devid_academy.tutocomposeoct23.network.ResponseRegisterOrLoginDto
import com.devid_academy.tutocomposeoct23.network.Repository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideMoshi() : Moshi {

        return Moshi.Builder().apply {
            add(KotlinJsonAdapterFactory())
        }.build()

    }

    @Singleton
    @Provides
    fun provideAppService(okHttpClient: OkHttpClient, moshi: Moshi) : Retrofit {

        return Retrofit.Builder()
            .baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(apiService : Retrofit) : ApiInterface {
        return apiService.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(apiInterface: ApiInterface): Repository {
        return Repository(apiInterface)
    }


    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext applicationContext: Context) : SharedPreferences {
        return applicationContext.getSharedPreferences("FeedArticlesCompose", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideMyPrefs(sharedPreferences: SharedPreferences): MyPrefs {

        return MyPrefs(sharedPreferences)

    }
}



