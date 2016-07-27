package example.ngondo.todo;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import example.ngondo.todo.db.TaskContract;
import example.ngondo.todo.db.TaskDBHelper;

public class Home extends AppCompatActivity {
    private TaskDBHelper taskDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateUI();

        //Add a task to do
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popup the custom dialog with its style
                final Dialog addDialog = new Dialog(Home.this, R.style.myDialog);
                addDialog.setContentView(R.layout.add_task);
                /*
                * Note that the view for the dialog belongs to the dialog, therefore
                * The content cannot be declared explicitly but must be within the dialog
                * */
                final EditText input = (EditText) addDialog.findViewById(R.id.task);
                final Button addTask = (Button) addDialog.findViewById(R.id.addbutton);
                final Button cancel = (Button) addDialog.findViewById(R.id.cancelButton);

                addDialog.setTitle("What do you want to do?");

                //add a task to the DB
                addTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //convert the text to string
                        String task = input.getText().toString();
                        Log.d("Home", task);
                        //Check for null entries
                        if(task.trim().length() == 0){
                            Snackbar.make(view ,"Sorry!! Cannot enter empty task.",Snackbar.LENGTH_LONG).show();
                            updateUI();
                        }else{
                            //open the DB
                            taskDBHelper = new TaskDBHelper(Home.this);
                            SQLiteDatabase db = taskDBHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();

                            values.clear();
                            values.put(TaskContract.Columns.TASK, task);
                            db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                            updateUI();
                            addDialog.dismiss();
                        }
                    }
                });
                //dismiss the dialog
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addDialog.dismiss();
                    }
                });
                //show the dialog
                addDialog.show();
            }
        });
    }
    //Method called to update the UI whenever a change occurs in the db
    private void updateUI(){
        taskDBHelper = new TaskDBHelper(Home.this);
        SQLiteDatabase sqlDB = taskDBHelper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID,
                TaskContract.Columns.TASK}, null, null, null, null, null);

        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.tasks_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );
        ListView listview = (ListView) findViewById(R.id.list);
        listview.setMinimumHeight(100);
        listview.setAdapter(listAdapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//
//
//    }

    public void onDoneClick(View view){
        //Invoked method from getting the view
        View v = (View) view.getParent();
        TextView tv = (TextView) v.findViewById(R.id.taskTextView);
        String task = tv.getText().toString();

        String deletequery = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);

        taskDBHelper = new TaskDBHelper(Home.this);
        SQLiteDatabase sqlDB = taskDBHelper.getWritableDatabase();
        sqlDB.execSQL(deletequery);
        updateUI();
    }
}
