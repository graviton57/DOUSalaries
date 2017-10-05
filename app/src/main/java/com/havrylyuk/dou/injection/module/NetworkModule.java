package com.havrylyuk.dou.injection.module;

import android.content.Context;

import com.havrylyuk.dou.BuildConfig;
import com.havrylyuk.dou.data.remote.DouApiHelper;
import com.havrylyuk.dou.data.remote.IApiHelper;
import com.havrylyuk.dou.data.remote.SalaryApiService;
import com.havrylyuk.dou.data.remote.helper.HeaderHelper;
import com.havrylyuk.dou.data.remote.helper.error.ErrorHandlerHelper;
import com.havrylyuk.dou.injection.qualifier.ApplicationContext;
import com.havrylyuk.dou.injection.scope.DouAppScope;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Igor Havrylyuk on 01.09.2017.
 */

@Module(includes = { ContextModule.class, RxModule.class })
public class NetworkModule {

    @Provides
    @DouAppScope
    public Interceptor httpInterceptor() {
        return new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                // Request customization: add request headers
                Request.Builder requestBuilder = chain.request()
                        .newBuilder()
                        .method(chain.request().method(), chain.request().body())
                        .headers(HeaderHelper.getAppHeaders());
                return chain.proceed(requestBuilder.build());
            }
        };
    }

    @Provides
    @DouAppScope
    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override public void log(String message) {
                        Timber.i(message);
                    }
                });
        interceptor.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

  /*  @Provides
    @DouAppScope
    public SigningInterceptor signingInterceptor(){
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(
                BuildConfig.NOUN_DEV_API_KEY,
                BuildConfig.NOUN_DEV_SECRET_API_KEY);
        return new SigningInterceptor(consumer);
    }*/

    @Provides
    @DouAppScope
    public OkHttpClient okHttpClient(Interceptor interceptor) {

        return new OkHttpClient.Builder().addInterceptor(interceptor)
                //.addInterceptor(signingInterceptor())
                .addInterceptor(loggingInterceptor())
                .build();
    }

    @Provides
    @DouAppScope
    public Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @DouAppScope
    public SalaryApiService apiSalaryInterface(Retrofit retrofit) {
        return retrofit.create(SalaryApiService.class);
    }

    @Provides
    @DouAppScope
    public ErrorHandlerHelper errorHandlerHelper(@ApplicationContext Context context) {
        return new ErrorHandlerHelper(context);
    }

    @Provides
    @DouAppScope
    public IApiHelper apiHelper( SalaryApiService salaryApiInterface,
                                ErrorHandlerHelper errorHandlerHelper) {
        return new DouApiHelper( salaryApiInterface, errorHandlerHelper);
    }

}
