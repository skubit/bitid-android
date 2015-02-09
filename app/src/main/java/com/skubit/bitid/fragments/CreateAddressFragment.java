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
import com.skubit.bitid.Utils;
import com.skubit.bitid.provider.key.KeyColumns;
import com.skubit.bitid.provider.key.KeyContentValues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URISyntaxException;

public class CreateAddressFragment extends BaseFragment {

    private TextView mAddressText;

    private EditText mNickname;

    private Button mSaveAddress;

    private Button mBack;

    public static CreateAddressFragment newInstance(String bitID, ECKeyData ecKeyData) {
        CreateAddressFragment CreateAddressFragment = new CreateAddressFragment();
        Bundle data = Utils.createBundleWithBitID(bitID);
        data.putParcelable(ECKeyData.EXTRA_NAME, ecKeyData);

        CreateAddressFragment.setArguments(data);
        return CreateAddressFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final String bitID = getArguments().getString(BitID.EXTRA_NAME);
        View view = inflater.inflate(R.layout.fragment_create_address, container, false);
        mAddressText = (TextView) view.findViewById(R.id.addressText);
        mNickname = (EditText) view.findViewById(R.id.nicknameText);
        try {
            mNickname.setText(Utils.getID(bitID).toCallbackURI().getHost());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSaveAddress = (Button) view.findViewById(R.id.saveAddressBtn);

        final ECKeyData key = (ECKeyData) getArguments().get(ECKeyData.EXTRA_NAME);

        mAddressText.setText(key.getAddress());
        mSaveAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyContentValues kcv = new KeyContentValues();
                kcv.putPub(key.getPublicKey());
                kcv.putPriv(key.getPrivateKey());
                kcv.putAddress((key.getAddress()));
                kcv.putNickname(mNickname.getText().toString());
                if (mAuthCallbacks != null) {
                    getActivity().getContentResolver().insert(KeyColumns.CONTENT_URI, kcv.values());
                    mAuthCallbacks.showChooseAddress(bitID);
                }
            }
        });

        mBack = (Button) view.findViewById(R.id.backBtn);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAuthCallbacks != null) {
                    mAuthCallbacks.showChooseAddress(bitID);
                }
            }
        });
        return view;
    }


}
