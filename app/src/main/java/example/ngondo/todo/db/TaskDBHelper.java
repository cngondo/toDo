package example.ngondo.todo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ngondo on 2/15/16.
 */
public class TaskDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqlDB;

    public TaskDBHelper(Context context){
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = String.format("CREATE TABLE %s (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT)", TaskContract.TABLE,
                TaskContract.Columns.TASK);

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqlDB.execSQL("DROP TABLE IF EXISTS" + TaskContract.TABLE);
        onCreate(sqlDB);
    }
}
