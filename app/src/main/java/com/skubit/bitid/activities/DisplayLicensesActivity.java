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
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class DisplayLicensesActivity extends Activity {

    private static String toString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.licenses_activity);
        this.setTitle("Open Source Licenses");
        TextView licensesText = (TextView) this.findViewById(R.id.licenses_test);
        
        AssetManager assetManager = getAssets();
        String text;
        try {
            text = toString(assetManager.open("NOTICE.txt"));
        } catch (IOException e) {
           return;
        }
        licensesText.setText(text);
    }
}
