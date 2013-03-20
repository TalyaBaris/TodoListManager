package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class AddNewTodoItemActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_layout);
        setTitle("Add New Item");
        ((Button)findViewById(R.id.btnOK)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText newTask = (EditText)findViewById(R.id.edtNewItem);
				DatePicker datePicker = ((DatePicker)findViewById(R.id.datePicker));
				Date dueDate = new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth());
				Intent resIntent = new Intent();
				resIntent.putExtra("title", newTask.getText().toString());
				resIntent.putExtra("dueDate", dueDate);
				setResult(RESULT_OK, resIntent);
				finish();
				
			}
		});
        
        
  ((Button)findViewById(R.id.btnCancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				finish();
				
			}
		});
        
        
        
        
    }
}
