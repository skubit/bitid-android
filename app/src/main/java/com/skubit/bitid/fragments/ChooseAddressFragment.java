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
import com.skubit.bitid.SignInAsyncTask;
import com.skubit.bitid.Utils;
import com.skubit.bitid.provider.key.KeyColumns;
import com.skubit.bitid.provider.key.KeyCursor;

import org.bitcoinj.core.ECKey;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

public class ChooseAddressFragment extends Fragment {

    private Button mCreateAddress;

    private Button mLogin;

    private ListView mListView;

    public static ChooseAddressFragment newInstance(String bitID) {
        ChooseAddressFragment chooseAddressFragment = new ChooseAddressFragment();
        chooseAddressFragment.setArguments(Utils.createBundleWithBitID(bitID));

        return chooseAddressFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final String contents = getArguments().getString(BitID.EXTRA_NAME);
        View view = inflater.inflate(R.layout.fragment_choose_address, container, false);
        mCreateAddress = (Button) view.findViewById(R.id.createAddressBtn);
        mCreateAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .replace(R.id.main_container,
                                LoadingFragment.newInstance(), "loading").commit();
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        getFragmentManager().beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_NONE)
                                .replace(R.id.main_container,
                                        CreateAddressFragment
                                                .newInstance(contents, new ECKeyData(new ECKey())),
                                        "create_address").commit();
                    }
                });
                t.start();

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
        final KeyCursor kc = new KeyCursor(c);

        mLogin = (Button) view.findViewById(R.id.loginBtn);
        mLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ECKey ecKey = ECKey.fromPrivate(kc.getPriv());
                try {
                    login(BitID.parse(contents), ecKey);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
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
        return view;
    }

    public void login(final BitID bitID, final ECKey key) {
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.main_container,
                        LoadingFragment.newInstance(), "loading").commit();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    new SignInAsyncTask(bitID, key, getFragmentManager()).execute((Void) null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        t.start();
    }
}
