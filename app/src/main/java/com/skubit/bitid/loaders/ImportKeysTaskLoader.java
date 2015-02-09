package com.skubit.bitid.loaders;

import com.skubit.bitid.ImportKeysResponse;
import com.skubit.bitid.keystore.BitKeystoreImporter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.File;

public class ImportKeysTaskLoader extends AsyncTaskLoader<ImportKeysResponse> {

    private final File mKeystore;

    private final String mPassword;

    private ImportKeysResponse mKeysResponse;

    public ImportKeysTaskLoader(Context context, File keystore, String password) {
        super(context);
        mKeystore = keystore;
        mPassword = password;
    }

    @Override
    public ImportKeysResponse loadInBackground() {
        BitKeystoreImporter importer = new BitKeystoreImporter(getContext());
        try {
            importer.load(mKeystore, mPassword);
        } catch (Exception e) {
            return new ImportKeysResponse(e.getMessage());
        }
        return new ImportKeysResponse(
                "Successfully imported the keystore: " + mKeystore.getAbsolutePath());
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
    public void deliverResult(ImportKeysResponse data) {
        if (isReset() && mKeysResponse != null) {
            return;
        }
        mKeysResponse = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}
