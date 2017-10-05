package com.havrylyuk.dou.data.remote;

import com.havrylyuk.dou.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Igor Havrylyuk on 24.09.2017.
 */

public class SalaryApiClient {
    private static Retrofit sRetrofit = null;

    public SalaryApiClient() {
    }

    public static Retrofit getClient() {
        if (sRetrofit == null) {
            synchronized (Retrofit.class) {
                if (sRetrofit == null) {
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl(BuildConfig.BASE_SALARY_URL)
                            .client(new OkHttpClient())
                            .build();
                }
            }
        }
        return sRetrofit;
    }

}
