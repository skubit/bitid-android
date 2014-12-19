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
package com.skubit.bitid.fragments;

import com.skubit.bitid.BitID;
import com.skubit.bitid.ECKeyData;
import com.skubit.bitid.R;
import com.skubit.bitid.SignInResponse;
import com.skubit.bitid.Utils;
import com.skubit.bitid.loaders.SignInAsyncTaskLoader;
import com.skubit.bitid.provider.key.KeyColumns;
import com.skubit.bitid.provider.key.KeyCursor;

import org.bitcoinj.core.ECKey;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.net.URISyntaxException;

public class ChooseAddressFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<SignInResponse> {

    private Button mCreateAddress;

    private Button mLogin;

    private ListView mListView;

    private String mBitID;

    private ECKey mEcKey;

    private KeyCursor kc;

    public static ChooseAddressFragment newInstance(String bitID) {
        ChooseAddressFragment chooseAddressFragment = new ChooseAddressFragment();
        chooseAddressFragment.setArguments(Utils.createBundleWithBitID(bitID));

        return chooseAddressFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mEcKey != null) {
            getLoaderManager().initLoader(2, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mBitID = getArguments().getString(BitID.EXTRA_NAME);
        View view = inflater.inflate(R.layout.fragment_choose_address, container, false);
        mCreateAddress = (Button) view.findViewById(R.id.createAddressBtn);
        mCreateAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAuthCallbacks != null) {
                    mAuthCallbacks.showCreateAddress(mBitID, new ECKeyData(new ECKey()));
                }
            }
        });

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setSelected(true);
        mListView.setClickable(true);

        final Cursor c = getActivity().getContentResolver()
                .query(KeyColumns.CONTENT_URI, null, null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.list_item_single_choice, c,
                new String[]{KeyColumns.ADDRESS}, new int[]{android.R.id.text1},
                CursorAdapter.FLAG_AUTO_REQUERY);

        kc = new KeyCursor(c);

        mLogin = (Button) view.findViewById(R.id.loginBtn);
        mLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEcKey = ECKey.fromPrivate(kc.getPriv());
                getLoaderManager().initLoader(2, null, ChooseAddressFragment.this);
            }
        });

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                TextView radio = (TextView) view.findViewById(android.R.id.text1);

                StringBuilder label = new StringBuilder();
                String nickname = kc.getNickname();
                if (!TextUtils.isEmpty(nickname)) {
                    label.append("(").append(nickname).append(") ");
                }
                label.append(kc.getAddress());

                radio.setText(label.toString());
                return true;
            }
        });

        mListView.setAdapter(adapter);
        if(c.getCount() > 0) {
            mListView.requestFocusFromTouch();
            mListView.setItemChecked(c.getCount() - 1, true);
            mListView.smoothScrollToPosition(c.getCount());
        }
        return view;
    }

    @Override
    public Loader<SignInResponse> onCreateLoader(int id, Bundle args) {
        try {
            return new SignInAsyncTaskLoader(getActivity(), BitID.parse(mBitID), mEcKey);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;//Should never happen
    }

    @Override
    public void onLoadFinished(Loader<SignInResponse> loader, SignInResponse data) {
        if (mAuthCallbacks != null) {
            mAuthCallbacks.showSignInResponse(data.getResultCode(), data.getMessage());
            mEcKey = null;
        }
    }

    @Override
    public void onLoaderReset(Loader<SignInResponse> loader) {

    }

}
