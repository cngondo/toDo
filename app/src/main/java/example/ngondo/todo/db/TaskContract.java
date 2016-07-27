package example.ngondo.todo.db;

import android.provider.BaseColumns;

/**
 * Created by ngondo on 2/15/16.
 */
public class TaskContract {
    public static final String DB_NAME = "example.ngondo.todo.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    //Inner class that defines the table contents
    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }
}
