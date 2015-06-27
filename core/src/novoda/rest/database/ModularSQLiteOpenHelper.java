
package novoda.rest.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import novoda.lib.sqliteprovider.util.DatabaseUtils;

public class ModularSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = ModularSQLiteOpenHelper.class.getSimpleName();
    private static final String SELECT_TABLES_NAME = "SELECT name FROM sqlite_master WHERE type='table';";
    private static final int ALWAYS_UPGRADE = 99;

    private static int dbVersion = 0;

    private final List<String> createdTable;
    private final Map<String, SQLiteTableCreator> createStatements;

    public ModularSQLiteOpenHelper(Context context) {
        this(context, null);
    }

    public ModularSQLiteOpenHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, new StringBuilder(context.getApplicationInfo().packageName).append(".db").toString(), factory, dbVersion);
        createdTable = new ArrayList<String>();
        createStatements = new HashMap<String, SQLiteTableCreator>();
        init();
    }

    private void init() {
        final Cursor cur = getReadableDatabase().rawQuery(SELECT_TABLES_NAME, null);
        while (cur.moveToNext()) {
            createdTable.add(cur.getString(0));
        }
        cur.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "upgrading database from version " + oldVersion + " to " + newVersion);
        for (Entry<String, SQLiteTableCreator> entry : createStatements.entrySet()) {
            if (createdTable.contains(entry.getKey())) {
                Log.v(TAG, "Table " + entry.getKey() + " already in DB.");
            } else {
                Log.v(TAG, "Creating table: " + entry.getKey());
                SQLiteTableCreator creator = entry.getValue();
                db.execSQL(DatabaseUtils.getCreateStatement(creator));
                if (creator.isOneToMany()) {
                    for (String trigger : creator.getTriggers()) {
                        db.execSQL(trigger);
                    }
                }
                createdTable.add(entry.getKey());
            }
        }
    }

    public synchronized void createTable(SQLiteTableCreator creator) {
        if (createdTable.contains(creator.getTableName())) {
            Log.v(TAG, "Table " + creator.getTableName() + " already in DB.");
        } else {
            Log.v(TAG, "Will create table " + creator.getTableName());
            createStatements.put(creator.getTableName(), creator);
            getWritableDatabase().needUpgrade(++dbVersion);
            onUpgrade(getWritableDatabase(), 0, ALWAYS_UPGRADE);
        }
    }

    /**
     * Method to return the columns and type for a specific table. This only
     * supports value defined in SQLType
     * @param table , the table name against which we want the columns
     * @return a map containing all columns and their type
     */
    public synchronized Map<String, SQLiteType> getColumnsForTable(final String table) {
        final Cursor cur = getReadableDatabase().rawQuery(
                new StringBuilder("PRAGMA table_info('").append(table).append("')").toString(),
                null);
        final Map<String, SQLiteType> ret = new HashMap<String, SQLiteType>(cur.getCount());
        while (cur.moveToNext()) {
            ret.put(cur.getString(cur.getColumnIndexOrThrow("name")),
                    SQLiteType.valueOf(cur.getString(cur.getColumnIndexOrThrow("type"))));
        }
        cur.close();
        return ret;
    }

    /**
     * Utility method to check if a table has been created in the database or
     * not.
     * @param tableName , the table to check if created or not
     * @return true if the table has been created. false otherwise
     */
    public synchronized boolean isTableCreated(final String tableName) {
        return createdTable.contains(tableName);
    }

}
