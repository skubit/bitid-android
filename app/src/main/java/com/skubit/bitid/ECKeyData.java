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

import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;

import android.os.Parcel;
import android.os.Parcelable;

public class ECKeyData implements Parcelable {

    public static final String EXTRA_NAME = "ECKey";

    private byte[] privateKey;

    private byte[] publicKey;

    private String address;

    public static final Parcelable.Creator<ECKeyData> CREATOR
            = new Parcelable.Creator<ECKeyData>() {
        @Override
        public ECKeyData createFromParcel(Parcel parcel) {
            ECKeyData ecKey = new ECKeyData();
            ecKey.publicKey = new byte[parcel.readInt()];
            parcel.readByteArray(ecKey.publicKey);

            ecKey.privateKey = new byte[parcel.readInt()];
            parcel.readByteArray(ecKey.privateKey);

            ecKey.address = parcel.readString();

            return ecKey;
        }

        @Override
        public ECKeyData[] newArray(int size) {
            return new ECKeyData[size];
        }
    };

    public ECKeyData() {
    }

    public ECKeyData(byte[] publicKey, byte[] privateKey, String address) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.address = address;
    }

    public ECKeyData(ECKey key) {
        this(key.getPubKey(), key.getPrivKeyBytes(), key.toAddress(MainNetParams.get()).toString());
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(publicKey.length);
        parcel.writeByteArray(publicKey);
        parcel.writeInt(privateKey.length);
        parcel.writeByteArray(privateKey);
        parcel.writeString(address);
    }

}
