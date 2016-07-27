package example.ngondo.todo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ngondo on 7/27/16.
 */

public class AddTaskFragment extends DialogFragment {
    //binding all views using butterknife
    @BindView(R.id.task) EditText mAddTask;
    @BindView(R.id.addbutton) EditText mAddButton;
    @BindView(R.id.cancelButton) EditText mCancelButton;

    public AddTaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task, container,false);
        ButterKnife.bind(this, v);
        return v;
    }
}
