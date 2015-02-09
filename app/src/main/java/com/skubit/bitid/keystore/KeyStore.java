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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyStore {

    private ScryptHeader scrypt;

    private ArrayList<KeyEntry> keys = new ArrayList<KeyEntry>();

    public ScryptHeader getScrypt() {
        return scrypt;
    }

    public void setScrypt(ScryptHeader scrypt) {
        this.scrypt = scrypt;
    }

    public void addKey(KeyEntry key) {
        keys.add(key);
    }

    public ArrayList<KeyEntry> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<KeyEntry> keys) {
        this.keys = keys;
    }
}
