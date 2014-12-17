package com.skubit.bitid.provider.key;

import com.skubit.bitid.provider.base.AbstractSelection;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Selection for the {@code key} table.
 */
public class KeySelection extends AbstractSelection<KeySelection> {

    @Override
    public Uri uri() {
        return KeyColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection      A list of which columns to return. Passing null will return all
     *                        columns, which is inefficient.
     * @param sortOrder       How to order the rows, formatted as an SQL ORDER BY clause (excluding
     *                        the ORDER BY itself). Passing null will use the default sort
     *                        order, which may be unordered.
     * @return A {@code KeyCursor} object, which is positioned before the first entry, or null.
     */
    public KeyCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) {
            return null;
        }
        return new KeyCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public KeyCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public KeyCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public KeySelection id(long... value) {
        addEquals("key." + KeyColumns._ID, toObjectArray(value));
        return this;
    }


    public KeySelection nickname(String... value) {
        addEquals(KeyColumns.NICKNAME, value);
        return this;
    }

    public KeySelection nicknameNot(String... value) {
        addNotEquals(KeyColumns.NICKNAME, value);
        return this;
    }

    public KeySelection nicknameLike(String... value) {
        addLike(KeyColumns.NICKNAME, value);
        return this;
    }

    public KeySelection pub(byte[]... value) {
        addEquals(KeyColumns.PUB, value);
        return this;
    }

    public KeySelection pubNot(byte[]... value) {
        addNotEquals(KeyColumns.PUB, value);
        return this;
    }


    public KeySelection priv(byte[]... value) {
        addEquals(KeyColumns.PRIV, value);
        return this;
    }

    public KeySelection privNot(byte[]... value) {
        addNotEquals(KeyColumns.PRIV, value);
        return this;
    }


    public KeySelection address(String... value) {
        addEquals(KeyColumns.ADDRESS, value);
        return this;
    }

    public KeySelection addressNot(String... value) {
        addNotEquals(KeyColumns.ADDRESS, value);
        return this;
    }

    public KeySelection addressLike(String... value) {
        addLike(KeyColumns.ADDRESS, value);
        return this;
    }
}
