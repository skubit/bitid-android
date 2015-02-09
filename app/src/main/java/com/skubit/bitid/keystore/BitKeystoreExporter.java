/**
 * Copyright 2015 Skubit
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
package com.skubit.bitid.keystore;

import com.google.common.io.BaseEncoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skubit.bitid.provider.key.KeyColumns;
import com.skubit.bitid.provider.key.KeyCursor;

import org.bitcoinj.crypto.EncryptedData;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.wallet.Protos;
import org.spongycastle.crypto.params.KeyParameter;

import android.content.Context;
import android.database.Cursor;

import java.io.OutputStream;

public class BitKeystoreExporter {

    private final Context mContext;

    public BitKeystoreExporter(Context context) {
        mContext = context;
    }

    public void store(OutputStream os, String password)
            throws Exception {
        final Cursor c = mContext.getContentResolver()
                .query(KeyColumns.CONTENT_URI, null, null, null, null);

        KeyCursor kc = new KeyCursor(c);
        kc.moveToFirst();

        KeyCrypterScrypt crypterScrypt = new KeyCrypterScrypt(512);
        KeyParameter aesKey = crypterScrypt.deriveKey(password);

        Protos.ScryptParameters params = crypterScrypt.getScryptParameters();
        ScryptHeader scryptHeader = new ScryptHeader();
        scryptHeader.setN(params.getN());
        scryptHeader.setR(params.getR());
        scryptHeader.setP(params.getP());
        scryptHeader.setSalt(BaseEncoding.base64Url().encode(params.getSalt().toByteArray()));

        KeyStore keyStore = new KeyStore();
        keyStore.setScrypt(scryptHeader);

        for (int i = 0; i < kc.getCount(); i++) {
            kc.moveToPosition(i);

            EncryptedData data = crypterScrypt.encrypt(kc.getPriv(), aesKey);
            String ecKey64 = BaseEncoding.base64Url().encode(data.encryptedBytes);

            KeyEntry entry = new KeyEntry();
            entry.setAlias(kc.getNickname());
            entry.setKty("EC");
            entry.setCrv("P-256");
            entry.setPk(ecKey64);
            entry.setIv(BaseEncoding.base64Url().encode(data.initialisationVector));
            keyStore.addKey(entry);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(os, keyStore);
        os.close();

    }
}