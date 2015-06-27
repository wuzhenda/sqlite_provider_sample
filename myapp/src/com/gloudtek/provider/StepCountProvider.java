package com.gloudtek.provider;

import android.net.Uri;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

/**
 * Created by wu on 6/27/15.
 */
//
//public String id;// “记录ID”，
//public long date;//“日期”，
//public int stepCount;//“计步数”
//public String address;//gps address
public class StepCountProvider extends SQLiteContentProviderImpl {

    private static final String AUTHORITY = "content://com.gloudtek.provider/";

    private static final String TABLE_NAME_STEPCOUNT = "stepcounttable";


//    public static final String COL_STEPCOUNT_ID = "id";
    //时间是唯一主键
    public static final String COL_STEPCOUNT_DATE = "date";
    public static final String COL_STEPCOUNT_NUM = "stepCount";
    public static final String COL_STEPCOUNT_ADDRESS = "address";
    public static final String COL_STEPCOUNT_ISUPLOADED = "isuploaded";

    public static final Uri STEPCOUNT = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_NAME_STEPCOUNT).build();





}
