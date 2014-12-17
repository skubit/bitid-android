package com.skubit.bitid.provider.key;

import com.skubit.bitid.provider.base.AbstractCursor;

import android.database.Cursor;

/**
 * Cursor wrapper for the {@code key} table.
 */
public class KeyCursor extends AbstractCursor {

    public KeyCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code nickname} value.
     * Can be {@code null}.
     */
    public String getNickname() {
        Integer index = getCachedColumnIndexOrThrow(KeyColumns.NICKNAME);
        return getString(index);
    }

    /**
     * Get the {@code pub} value.
     * Can be {@code null}.
     */
    public byte[] getPub() {
        Integer index = getCachedColumnIndexOrThrow(KeyColumns.PUB);
        return getBlob(index);
    }

    /**
     * Get the {@code priv} value.
     * Can be {@code null}.
     */
    public byte[] getPriv() {
        Integer index = getCachedColumnIndexOrThrow(KeyColumns.PRIV);
        return getBlob(index);
    }

    /**
     * Get the {@code address} value.
     * Can be {@code null}.
     */
    public String getAddress() {
        Integer index = getCachedColumnIndexOrThrow(KeyColumns.ADDRESS);
        return getString(index);
    }
}
