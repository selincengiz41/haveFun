package com.selincengiz.havefun.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.selincengiz.havefun.common.Constants.BASE_URL
import com.selincengiz.havefun.data.source.remote.EventService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.internal.addHeaderLenient
import okhttp3.internal.isSensitiveHeader
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT = 60L

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor.Builder(context).build()

    @Provides
    @Singleton
    fun provideOkHttpClient(chucker: ChuckerInterceptor) = OkHttpClient.Builder().apply {
        addInterceptor(chucker)
        addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("cookie", "__test=47cf31c57a4224f292ec27c24beee6a2; expires=2024-11-26T13:30:59.979Z; path=/") // İstediğiniz header'ı burada ekleyin
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideEventService(retrofit: Retrofit) = retrofit.create<EventService>()

}


