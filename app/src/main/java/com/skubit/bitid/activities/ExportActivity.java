package com.skubit.bitid.activities;


import com.skubit.bitid.ExportKeysResponse;
import com.skubit.bitid.R;
import com.skubit.bitid.loaders.ExportKeysTaskLoader;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ExportActivity extends BaseLoadingActivity
        implements LoaderManager.LoaderCallbacks<ExportKeysResponse> {

    private Button mExportBtn;

    private TextView mPassword;

    private View mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.export_activity_frame);
        super.onCreate(savedInstanceState);

        mMain = (View) this.findViewById(R.id.export_form_activity);

        mExportBtn = (Button) findViewById(R.id.exportBtn);
        mExportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().initLoader(2, null, ExportActivity.this);
            }
        });

        mPassword = (TextView) findViewById(R.id.password);
    }


    @Override
    public Loader<ExportKeysResponse> onCreateLoader(int id, Bundle args) {
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {

        }
        return new ExportKeysTaskLoader(this, password);

    }

    @Override
    public void onLoadFinished(Loader<ExportKeysResponse> loader, ExportKeysResponse data) {
        mMain.setVisibility(View.INVISIBLE);
        this.showMessage(data.getMessage());
    }

    @Override
    public void onLoaderReset(Loader<ExportKeysResponse> loader) {

    }

}
