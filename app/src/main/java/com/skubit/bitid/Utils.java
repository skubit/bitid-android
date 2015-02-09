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

package com.skubit.bitid;

import android.os.Bundle;
import android.text.TextUtils;

import java.net.URISyntaxException;

public class Utils {

    public static Bundle createBundleWithBitID(String bitID) {
        if (TextUtils.isEmpty(bitID)) {
            throw new IllegalArgumentException("bitID is null");
        }
        Bundle bundle = new Bundle();
        bundle.putString("bitID", bitID);
        return bundle;
    }

    public static BitID getID(String bitID) {
        if(bitID.startsWith("tidbit://")) {
            return getTidBit(bitID);
        } else {
            return getBitID(bitID);
        }
    }

    public static BitID getBitID(String bitID) {
        BitID bitId = null;
        try {
            bitId = BitID.parse(bitID);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return bitId;
    }

    public static BitID getTidBit(String tidbit) {
        TidBit tb = null;
        try {
            tb = TidBit.parse(tidbit);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return tb;
    }

    public static int getCodeFromMessage(String message) {
        //Try some common ones [these should be standardized]
        if (TextUtils.isEmpty(message)) {
            return ResultCode.UNKNOWN_ERROR;
        }

        if ("BitID URI is invalid or not legal".equals(message)) {
            return ResultCode.INVALID_BITID;
        } else if ("NONCE is illegal".equals(message)) {
            return ResultCode.INVALID_NONCE;
        } else if ("NONCE has expired".equals(message)) {
            return ResultCode.NONCE_EXPIRED;
        } else if ("Signature is incorrect".equals(message)) {
            return ResultCode.INVALID_SIGNATURE;
        }

        return ResultCode.UNKNOWN_ERROR;

    }
}
