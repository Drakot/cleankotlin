package es.demokt.kotlindemoproject.data.api.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Extension GsonBuilder
 * Created by aluengo on 12/2/18.
 */

fun Retrofit.Builder.addConverters(): Retrofit.Builder {
    val gson = GsonBuilder().setLenient().create()
    return this.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
}