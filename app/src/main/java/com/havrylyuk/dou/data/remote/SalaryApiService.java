package com.havrylyuk.dou.data.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Igor Havrylyuk on 24.09.2017.
 */

public interface SalaryApiService {

    @Streaming
    @GET
    Call<ResponseBody> downloadDouFile(@Url String fileUrl);
}
