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

import com.skubit.bitid.AuthenticationCallback;
import com.skubit.bitid.R;
import com.skubit.bitid.ResultCode;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SignInResponseFragment extends Fragment {


    public static SignInResponseFragment newInstance(int resultCode, String message) {
        SignInResponseFragment signInResponseFragment = new SignInResponseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("resultCode", resultCode);
        if (!TextUtils.isEmpty(message)) {
            bundle.putString("message", message);
        }

        signInResponseFragment.setArguments(bundle);
        return signInResponseFragment;
    }

    private String getMessageValue(String message) {
        if (TextUtils.isEmpty(message)) {
            return null;
        }

        try {
            JSONObject joMessage = new JSONObject(message);
            if (joMessage.has("message")) {
                return joMessage.get("message").toString();
            }
        } catch (JSONException e) {
        }
        return "Unknown Error";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_response, container, false);
        TextView messageView = (TextView) view.findViewById(R.id.message);
        final AuthenticationCallback callback = (AuthenticationCallback) getActivity();

        final String message = getArguments().getString("message");
        final int resultCode = getArguments().getInt("resultCode");
        if (resultCode == ResultCode.OK) {
            messageView.setText("Login successful");
        } else {
            messageView.setText(getMessageValue(message));
        }

        Button done = (Button) view.findViewById(R.id.doneBtn);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.callback(resultCode, message);
            }
        });

        return view;
    }


}
