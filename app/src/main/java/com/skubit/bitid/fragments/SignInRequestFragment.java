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
import com.skubit.bitid.R;
import com.skubit.bitid.Utils;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.net.URISyntaxException;

public class SignInRequestFragment extends Fragment {

    private Button mYesBtn;

    private TextView mSite;

    private Button mNoBtn;

    public static SignInRequestFragment newInstance(String bitID) {
        SignInRequestFragment signInRequestFragment = new SignInRequestFragment();
        signInRequestFragment.setArguments(Utils.createBundleWithBitID(bitID));
        return signInRequestFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final String bitIDText = getArguments().getString(BitID.EXTRA_NAME);

        View view = inflater.inflate(R.layout.fragment_signin_request, container, false);
        mSite = (TextView) view.findViewById(R.id.site);
        try {
            mSite.setText(Utils.getBitID(bitIDText).toCallbackURI().getHost());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mYesBtn = (Button) view.findViewById(R.id.yesBtn);
        mYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .replace(R.id.main_container, ChooseAddressFragment.newInstance(bitIDText),
                                "accept")
                        .commit();
            }
        });

        mNoBtn = (Button) view.findViewById(R.id.noBtn);
        mNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }
}
