/* Copyright 2015 Skubit
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

import com.skubit.bitid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseLoadingActivity extends Activity {

    protected LinearLayout mfinishedMessage;

    protected TextView mFinishedText;

    protected View mLoading;

    public void hideLoading() {
        mLoading.setVisibility(View.INVISIBLE);
    }

    public void hideMessage() {
        mfinishedMessage.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLoading = this.findViewById(R.id.progress_bar);
        this.mfinishedMessage = (LinearLayout) this.findViewById(R.id.finished_message);

        mfinishedMessage.setVisibility(View.INVISIBLE);

        this.mFinishedText = (TextView) findViewById(R.id.finished_text);

        Button cancelBtn = (Button) this.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showLoading() {
        mfinishedMessage.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    public void showMessage(String message) {
        mLoading.setVisibility(View.INVISIBLE);

        mfinishedMessage.setVisibility(View.VISIBLE);
        mFinishedText.setText(message);
    }
}
