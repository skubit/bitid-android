package com.skubit.bitid.provider.key;

import com.skubit.bitid.provider.base.AbstractContentValues;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Content values wrapper for the {@code key} table.
 */
public class KeyContentValues extends AbstractContentValues {

    @Override
    public Uri uri() {
        return KeyColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, KeySelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(),
                where == null ? null : where.args());
    }

    public KeyContentValues putNickname(String value) {
        mContentValues.put(KeyColumns.NICKNAME, value);
        return this;
    }

    public KeyContentValues putNicknameNull() {
        mContentValues.putNull(KeyColumns.NICKNAME);
        return this;
    }


    public KeyContentValues putPub(byte[] value) {
        mContentValues.put(KeyColumns.PUB, value);
        return this;
    }

    public KeyContentValues putPubNull() {
        mContentValues.putNull(KeyColumns.PUB);
        return this;
    }


    public KeyContentValues putPriv(byte[] value) {
        mContentValues.put(KeyColumns.PRIV, value);
        return this;
    }

    public KeyContentValues putPrivNull() {
        mContentValues.putNull(KeyColumns.PRIV);
        return this;
    }


    public KeyContentValues putAddress(String value) {
        mContentValues.put(KeyColumns.ADDRESS, value);
        return this;
    }

    public KeyContentValues putAddressNull() {
        mContentValues.putNull(KeyColumns.ADDRESS);
        return this;
    }

}
