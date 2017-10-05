package com.havrylyuk.dou.data.remote.helper.error;

import timber.log.Timber;

/**
 * Created by Igor Havrylyuk on 11.09.2017
 */
public class NounResponseException extends Throwable {

    public NounResponseException(String detailMessage) {
        super(detailMessage);
        Timber.d("NounResponseException=%s",detailMessage);
    }

}
