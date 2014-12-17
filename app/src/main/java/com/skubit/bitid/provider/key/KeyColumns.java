package com.skubit.bitid.provider.key;

import com.skubit.bitid.provider.KeyProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Columns for the {@code key} table.
 */
public class KeyColumns implements BaseColumns {

    public static final String TABLE_NAME = "key";

    public static final Uri CONTENT_URI = Uri
            .parse(KeyProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = new String(BaseColumns._ID);

    public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;

    public static final String NICKNAME = "nickname";

    public static final String PUB = "pub";

    public static final String PRIV = "priv";

    public static final String ADDRESS = "address";

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            NICKNAME,
            PUB,
            PRIV,
            ADDRESS
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) {
            return true;
        }
        for (String c : projection) {
            if (c == NICKNAME || c.contains("." + NICKNAME)) {
                return true;
            }
            if (c == PUB || c.contains("." + PUB)) {
                return true;
            }
            if (c == PRIV || c.contains("." + PRIV)) {
                return true;
            }
            if (c == ADDRESS || c.contains("." + ADDRESS)) {
                return true;
            }
        }
        return false;
    }

}
