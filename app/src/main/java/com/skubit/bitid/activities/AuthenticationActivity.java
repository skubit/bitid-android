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
package com.skubit.bitid.activities;

import com.skubit.bitid.BitID;
import com.skubit.bitid.ECKeyData;
import com.skubit.bitid.R;
import com.skubit.bitid.ResultCode;
import com.skubit.bitid.SignInCallback;
import com.skubit.bitid.UIState;
import com.skubit.bitid.fragments.ChooseAddressFragment;
import com.skubit.bitid.fragments.CreateAddressFragment;
import com.skubit.bitid.fragments.SignInRequestFragment;
import com.skubit.bitid.fragments.SignInResponseFragment;

import org.bitcoinj.core.ECKey;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.net.URISyntaxException;

public class AuthenticationActivity extends Activity implements SignInCallback {

    private ECKeyData mEcKeyData;

    private String mUIState = UIState.SIGNIN_REQUEST;

    private String mBitId;

    public static Intent newInstance(String bitId) {
        Intent i = new Intent();
        i.setClassName("com.skubit.bitid", AuthenticationActivity.class.getName());
        i.putExtra(BitID.EXTRA_NAME, bitId);
        return i;
    }

    private String getBitIdFromScanner() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return null;
        }
        return extras.getString(BitID.EXTRA_NAME);
    }

    private String getBitIdFromBrowser() {
        Uri bitId = getIntent().getData();

        if (bitId == null) {
            return null;
        }
        return bitId.toString();
    }

    private String getBitId() {
        String bitId = getBitIdFromScanner();
        if (TextUtils.isEmpty(bitId)) {
            bitId = getBitIdFromBrowser();
        }
        return bitId;
    }

    private boolean isValidBitId(String bitId) {
        if (TextUtils.isEmpty(bitId)) {
            return false;
        }
        try {
            BitID.parse(bitId);
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        String bitId = getBitId();
        if (bitId == null) {
            bitId = mBitId;
        }
        if (!isValidBitId(bitId)) {
            Toast.makeText(getApplicationContext(),
                    "Invalid bitID: " + bitId, Toast.LENGTH_SHORT).show();
            setResult(ResultCode.INVALID_BITID);
            finish();
            return;
        }
        if (savedInstanceState != null) {
            mUIState = savedInstanceState.getString("UI_STATE");
        }

        preloadHackForKey();

        Fragment frag = getFragmentManager().findFragmentByTag(mUIState);
        if (frag != null) {
            getFragmentManager().beginTransaction().show(frag);
            return;
        } else {
            showSignInRequest(bitId);
        }
    }

    /**
     * Forces expensive param generation upfront
     */
    private void preloadHackForKey() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean a = ECKey.FAKE_SIGNATURES;
            }
        });
        t.start();
    }

    @Override
    public void sendResultsBackToCaller(int resultCode, String message) {
        if (TextUtils.isEmpty(message)) {
            setResult(resultCode);
        } else {
            setResult(resultCode, new Intent().putExtra("response", message));
        }
        finish();
    }

    @Override
    public void cancel() {
        setResult(ResultCode.USER_CANCELED);
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mUIState = savedInstanceState.getString("UI_STATE");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("UI_STATE", mUIState);
    }

    public void showLoading() {

    }

    public void showCreateAddress(String bitID, ECKeyData keyData) {
        mBitId = bitID;
        mUIState = UIState.CREATE_ADDRESS;
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.main_container,
                        CreateAddressFragment
                                .newInstance(bitID, keyData),
                        UIState.CREATE_ADDRESS).commitAllowingStateLoss();
    }

    @Override
    public void showSignInResponse(int responseCode, String message) {
        mUIState = UIState.SIGNIN_RESPONSE;
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.main_container,
                        SignInResponseFragment.newInstance(responseCode,
                                message), UIState.SIGNIN_RESPONSE).commitAllowingStateLoss();
    }

    @Override
    public void showChooseAddress(String bitID) {
        mBitId = bitID;
        mUIState = UIState.CHOOSE_ADDRESS;
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.main_container, ChooseAddressFragment.newInstance(bitID),
                        UIState.CHOOSE_ADDRESS)
                .commit();
    }

    @Override
    public void showSignInRequest(String bitID) {
        mBitId = bitID;
        mUIState = UIState.SIGNIN_REQUEST;
        getFragmentManager().beginTransaction().add(R.id.main_container,
                SignInRequestFragment.newInstance(bitID), UIState.SIGNIN_REQUEST).commit();
    }
}
