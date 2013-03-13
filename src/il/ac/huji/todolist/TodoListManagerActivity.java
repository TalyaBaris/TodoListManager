package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
//
public class TodoListManagerActivity extends Activity {
    
	 private ArrayAdapter<String> adapter;
	 private EditText addTask;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        List<String> tasks=new ArrayList<String>();
        ListView tasksList=(ListView)findViewById(R.id.lstTodoItems);
       adapter = new TasksDisplayAdapter(this, tasks);//<String>(this, android.R.layout.simple_list_item_1,tasks);
        tasksList.setAdapter(adapter);
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menuItemAdd:
    		addTask=(EditText)findViewById(R.id.edtNewItem);
    		adapter.add(addTask.getText().toString());
    		addTask.setText("");
    		break;
    	case R.id.menuItemDelete:
    		ListView ourList=(ListView)findViewById(R.id.lstTodoItems);
    		adapter.remove(ourList.getSelectedItem().toString());
    		break;
    	}
    	return true;
    }
}