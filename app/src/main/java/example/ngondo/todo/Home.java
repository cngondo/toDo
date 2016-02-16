package example.ngondo.todo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

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
        listview.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add_task:
                //Launch a dialog to add new task
                AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                dialog.setTitle("Add a new Task");
                dialog.setMessage("What do you want to do?");

                final EditText input = new EditText(Home.this);
                dialog.setView(input);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = input.getText().toString();
                        Log.d("Home", task);

                        taskDBHelper = new TaskDBHelper(Home.this);
                        SQLiteDatabase db = taskDBHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);
                        db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create().show();

                return true;

            default:
                return false;
        }
    }

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
