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

import com.skubit.bitid.AuthenticationCallback;
import com.skubit.bitid.BitID;
import com.skubit.bitid.R;
import com.skubit.bitid.ResultCode;
import com.skubit.bitid.fragments.SignInRequestFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.net.URISyntaxException;

public class AuthenticationActivity extends Activity implements AuthenticationCallback {

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
        if (!isValidBitId(bitId)) {
            Toast.makeText(getApplicationContext(),
                    "Invalid bitID: " + bitId, Toast.LENGTH_SHORT).show();
            setResult(ResultCode.INVALID_BITID);
            finish();
        }

        if (savedInstanceState == null) {//make sure this refreshes if bitId changes
            getFragmentManager().beginTransaction().add(R.id.main_container,
                    SignInRequestFragment.newInstance(bitId), "accept").commit();
        }
    }

    @Override
    public void callback(int resultCode, String message) {
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
    public void onBackPressed() {
        super.onBackPressed();
        // getFragmentManager().popBackStack();
    }
}
