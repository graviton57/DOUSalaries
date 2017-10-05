package com.havrylyuk.dou.data.remote;

import com.havrylyuk.dou.data.remote.helper.error.ErrorHandlerHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Igor Havrylyuk on 10.09.2017
 */

public interface IApiHelper {

    Call<ResponseBody> downloadDouFile(String fileUrl);

    ErrorHandlerHelper getErrorHandlerHelper();

    void setErrorHandler(ErrorHandlerHelper errorHandler);

}
