/**
 * Copyright 2014 Skubit
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.skubit.bitid.loaders;

import com.skubit.bitid.BitID;
import com.skubit.bitid.ResultCode;
import com.skubit.bitid.SignInResponse;
import com.skubit.bitid.Utils;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Scanner;

public class SignInAsyncTaskLoader extends AsyncTaskLoader<SignInResponse> {

    private final BitID mBitID;

    private final ECKey mKey;

    public SignInResponse mSignInResponse;

    private HttpURLConnection mConnection;

    public SignInAsyncTaskLoader(Context context, final BitID bitID, final ECKey key) {
        super(context);
        mBitID = bitID;
        mKey = key;
    }

    public static String asString(InputStream inputStream) throws IOException {
        try {
            return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        } finally {
            inputStream.close();
        }
    }

    private SignInResponse performRequest() throws IOException, URISyntaxException, JSONException {
        openConnection();
        writeRequest(buildRequest());
        return readResponse();
    }

    private void openConnection() throws IOException, URISyntaxException {
        mConnection = (HttpURLConnection) mBitID.toCallbackURI()
                .toURL().openConnection();
        mConnection.setRequestMethod("POST");
        mConnection.setRequestProperty("Content-Type", "application/json");
        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);
        mConnection.setUseCaches(false);

        mConnection.connect();
    }

    private void writeRequest(String message) throws IOException {
        DataOutputStream dos = new DataOutputStream(mConnection.getOutputStream());
        dos.write(message.getBytes());
        dos.close();
    }

    private SignInResponse readResponse() throws IOException {
        int rc = mConnection.getResponseCode();
        if (rc == -1) {
            return new SignInResponse(ResultCode.NO_CONNECTION, null);
        }
        if (rc < 300 && rc >= 200) {
            return new SignInResponse(ResultCode.OK, asString(mConnection.getInputStream()));
        } else if (rc >= 400) {
            String message = asString(mConnection.getErrorStream());
            return new SignInResponse(Utils.getCodeFromMessage(message), message);
        } else {
            return new SignInResponse(ResultCode.UNKNOWN_ERROR, null);
        }
    }

    private String signMessage() {
        return mKey.signMessage(mBitID.getRawUri());
    }

    private String buildRequest() throws JSONException, UnsupportedEncodingException {
        JSONObject json = new JSONObject();
        json.put("address", URLDecoder
                .decode(mKey.toAddress(MainNetParams.get()).toString(), "UTF-8"));
        json.put("signature", signMessage());
        json.put("uri", mBitID.getRawUri());
        return json.toString();
    }

    @Override
    public SignInResponse loadInBackground() {
        try {
            return performRequest();
        } catch (Exception e) {

        }
        return new SignInResponse(ResultCode.UNKNOWN_ERROR, null);
    }

    @Override
    protected void onStartLoading() {
        if (mSignInResponse != null) {
            deliverResult(mSignInResponse);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mSignInResponse != null) {
            mSignInResponse = null;
        }
    }

    @Override
    public void deliverResult(SignInResponse data) {
        if (isReset() && mSignInResponse != null) {
            return;
        }

        mSignInResponse = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
