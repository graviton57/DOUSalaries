package com.havrylyuk.dou.data.remote;

import com.havrylyuk.dou.data.remote.helper.error.ErrorHandlerHelper;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Igor Havrylyuk on 19.05.2017
 */

public class DouApiHelper implements IApiHelper {

    private SalaryApiService salaryApiInterface;
    private ErrorHandlerHelper errorHandlerHelper;


    @Inject
    public DouApiHelper(SalaryApiService salaryApiInterface,
                        ErrorHandlerHelper errorHandlerHelper) {
        this.errorHandlerHelper = errorHandlerHelper;
        this.salaryApiInterface = salaryApiInterface;
    }

    @Override
    public ErrorHandlerHelper getErrorHandlerHelper() {
        return errorHandlerHelper;
    }

    @Override
    public void setErrorHandler(ErrorHandlerHelper errorHandler) {
        this.errorHandlerHelper = errorHandler;
    }

    @Override
    public Call<ResponseBody> downloadDouFile(String fileUrl) {
        return salaryApiInterface.downloadDouFile(fileUrl);
    }

}
