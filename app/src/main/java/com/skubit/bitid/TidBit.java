package com.skubit.bitid;

import android.net.Uri;
import android.text.TextUtils;

import java.net.URISyntaxException;

public class TidBit extends BitID {

    public static TidBit parse(String tidbit) throws URISyntaxException {
        if (TextUtils.isEmpty(tidbit)) {
            throw new IllegalArgumentException("tidbit is null");
        }

        TidBit tb = new TidBit();
        tb.mRawUri = tidbit;
        tb.mUri = Uri.parse(tidbit);
        checkIfValid(tb.mUri);

        tb.mNonce = tb.mUri.getQueryParameter("x");
        String uValue = tb.mUri.getQueryParameter("u");
        tb.mIsSecured = TextUtils.isEmpty(uValue) || uValue.equals("0");

        return tb;
    }

    private static void checkIfValid(Uri uri) throws URISyntaxException {
        if (!"tidbit".equals(uri.getScheme())) {
            throw new URISyntaxException(uri.toString(), "Invalid scheme");
        }
        if (TextUtils.isEmpty(uri.getQueryParameter("x"))) {
            throw new URISyntaxException(uri.toString(), "Missing x parameter");
        }

        String uValue = uri.getQueryParameter("u");
        if (!TextUtils.isEmpty(uValue) && (!uValue.equals("0")) && !uValue.equals("1")) {
            throw new URISyntaxException(uValue, "Illegal value for u param");
        }
    }
}
