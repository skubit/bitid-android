package com.skubit.bitid.activities;

import com.skubit.bitid.ImportKeysResponse;
import com.skubit.bitid.R;
import com.skubit.bitid.loaders.ImportKeysTaskLoader;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

public class ImportActivity extends BaseLoadingActivity implements LoaderManager.LoaderCallbacks<ImportKeysResponse> {

    private ListView mListView;

    private Button mImportButton;

    private ArrayAdapter<String> mAdapter;

    private String fileName;

    private TextView mPassword;

    private View mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.import_activity_frame);
        super.onCreate(savedInstanceState);
        mPassword = (TextView) findViewById(R.id.password);
        mMain = findViewById(R.id.import_form_activity);

        mImportButton = (Button) findViewById(R.id.importBtn);
        mImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mListView.getCheckedItemPosition();
                fileName = mAdapter.getItem(position);
                getLoaderManager().initLoader(1, null, ImportActivity.this);
            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setSelected(true);
        mListView.setClickable(true);

        File bitDir = new File(Environment.getExternalStorageDirectory(), "bitid");
        if(!bitDir.exists()) {
            bitDir.mkdirs();
        }
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, bitDir.list());
        mListView.setAdapter(mAdapter);

    }

    @Override
    public Loader<ImportKeysResponse> onCreateLoader(int id, Bundle args) {
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {

        }
        return new ImportKeysTaskLoader(this, new File(Environment.getExternalStorageDirectory(),
                "bitid/" + fileName), password);
    }

    @Override
    public void onLoadFinished(Loader<ImportKeysResponse> loader, ImportKeysResponse data) {
        mMain.setVisibility(View.INVISIBLE);
        showMessage(data.getMessage());
    }

    @Override
    public void onLoaderReset(Loader<ImportKeysResponse> loader) {

    }
}
