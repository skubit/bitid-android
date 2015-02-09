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

import com.google.protobuf.ByteString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skubit.bitid.provider.key.KeyColumns;
import com.skubit.bitid.provider.key.KeyContentValues;
import com.skubit.bitid.provider.key.KeySelection;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.EncryptedData;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Protos;
import org.spongycastle.crypto.params.KeyParameter;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

import static com.google.common.io.BaseEncoding.base64Url;

public class BitKeystoreImporter {

    private final Context mContext;

    public BitKeystoreImporter(Context context) {
        mContext = context;
    }

    public void load(File file, String password) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        KeyStore keystore = mapper.readValue(new FileInputStream(file), KeyStore.class);

        Protos.ScryptParameters.Builder scryptParametersBuilder
                = Protos.ScryptParameters.newBuilder()
                .setSalt(ByteString.copyFrom(base64Url().decode(keystore.getScrypt().getSalt())))
                .setN(keystore.getScrypt().getN())
                .setR(keystore.getScrypt().getR())
                .setP(keystore.getScrypt().getP());

        KeyCrypterScrypt crypterScrypt = new KeyCrypterScrypt(scryptParametersBuilder.build());
        KeyParameter aesKey = crypterScrypt.deriveKey(password);

        for (KeyEntry entry : keystore.getKeys()) {
            byte[] key = base64Url().decode(entry.getPk());
            byte[] iv = base64Url().decode(entry.getIv());

            EncryptedData data = new EncryptedData(iv, key);
            ECKey ecKey = ECKey.fromPrivate(crypterScrypt.decrypt(data, aesKey));

            KeyContentValues kcv = new KeyContentValues();
            kcv.putNickname(entry.getAlias());
            kcv.putAddress(ecKey.toAddress(MainNetParams.get()).toString());
            kcv.putPriv(ecKey.getPrivKeyBytes());
            kcv.putPub(ecKey.getPubKey());

            KeySelection ks = new KeySelection();
            ks.nickname(entry.getAlias());

            if(kcv.update(mContext.getContentResolver(), ks) != 1) {
                kcv.insert(mContext.getContentResolver());
            };

        }
    }
}
