package com.skubit.bitid;

public class ExportKeysResponse {

    private final String mMessage;

    public ExportKeysResponse(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
