package com.skubit.bitid.loaders;

import com.skubit.bitid.ExportKeysResponse;
import com.skubit.bitid.keystore.BitKeystoreExporter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportKeysTaskLoader extends AsyncTaskLoader<ExportKeysResponse> {

    private final String mPassword;

    private ExportKeysResponse mKeysResponse;

    public ExportKeysTaskLoader(Context context, String password) {
        super(context);
        mPassword = password;
    }

    @Override
    public ExportKeysResponse loadInBackground() {
        File bitDir = new File(Environment.getExternalStorageDirectory(), "bitid");
        if(!bitDir.exists()) {
            bitDir.mkdirs();
        }
        File ksFile = new File(bitDir, System.currentTimeMillis() + ".sbk");
        final BitKeystoreExporter exporter = new BitKeystoreExporter(getContext());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ksFile);
            exporter.store(fos, mPassword);
        } catch (Exception e) {
            return new ExportKeysResponse(e.getMessage());
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }
        }
        return new ExportKeysResponse("Exported keystore to " + ksFile.getAbsolutePath());
    }

    @Override
    protected void onStartLoading() {
        if (mKeysResponse != null) {
            deliverResult(mKeysResponse);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mKeysResponse != null) {
            mKeysResponse = null;
        }
    }

    @Override
    public void deliverResult(ExportKeysResponse data) {
        if (isReset() && mKeysResponse != null) {
            return;
        }
        mKeysResponse = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
