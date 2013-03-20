package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
//
public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<Task> adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);



		List<Task> tasks=new ArrayList<Task>();
		ListView tasksList=(ListView)findViewById(R.id.lstTodoItems);
		adapter = new TasksDisplayAdapter(this, tasks);//<String>(this, android.R.layout.simple_list_item_1,tasks);
		tasksList.setAdapter(adapter);
		registerForContextMenu(tasksList);
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
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, 1);//req code??????????????????????
			break;
			/**	
    		addTask=(EditText)findViewById(R.id.edtNewItem);
    		adapter.add(addTask.getText().toString());
    		addTask.setText("");*/

			/**case R.id.menuItemDelete:
    		ListView ourList=(ListView)findViewById(R.id.lstTodoItems);
    		adapter.remove(ourList.getSelectedItem().toString());
    		break;*/
		}
		return true;
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			String title = data.getStringExtra("title");
			Date dueDate = (Date) data.getSerializableExtra("dueDate");
			adapter.add(new Task(title, dueDate));
		}
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		
		getMenuInflater().inflate(R.menu.contextmenu, menu);
		AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) menuInfo;
		String title  = ((TextView) (info.targetView).findViewById(R.id.txtTodoTitle)).getText().toString();
		menu.setHeaderTitle(title); 
		if (title.startsWith("Call ")){
			MenuItem mi = menu.getItem(1);
			mi.setVisible(true);
			mi.setTitle(title);
		}
		else{
			menu.removeItem(1);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)
				item.getMenuInfo();
		int selectedItemIndex = info.position;
		switch (item.getItemId()){
		case R.id.menuItemDelete:
			adapter.remove(adapter.getItem(selectedItemIndex));
			break;
		case R.id.menuItemCall:
			String phone=item.getTitle().toString().split(" ")[1];
			Intent dial = new Intent( Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
			startActivity(dial);
			break;

			
		}
		return true;
	}
}