package es.ccrr.aloloco.engine.api.controllers

import es.ccrr.aloloco.engine.api.services.AlolocoService
import es.ccrr.aloloco.engine.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceController {
    private val TAG = ServiceController::class.java.name

    lateinit var retrofit: Retrofit
    val alolocoService: AlolocoService by lazy {
        retrofit.create(AlolocoService::class.java)
    }

    init {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)


        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClientBuilder.build())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}