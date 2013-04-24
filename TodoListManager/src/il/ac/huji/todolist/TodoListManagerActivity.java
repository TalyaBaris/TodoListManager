package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
//import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;
public class TodoListManagerActivity extends Activity {
	private ListView _lstItems;

	private TodoDAL _tdDAL;
	//private ArrayAdapter<Task> _adapter;
	private Cursor _cursor;

	private SimpleCursorAdapter _cursorAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Parse.initialize(this, "yyD9I63F4zOecKEzAnivMLVdnyhW4Nt0IT0UTE03", "irluSxilcVAXFZrmraGPAjzNKnFX3PBhCKN6Z7oC"); 
		_lstItems = (ListView)findViewById(R.id.lstTodoItems);
		_tdDAL=new TodoDAL(this);
		_cursor = _tdDAL.getCursor();

		String [] from = {"title", "due"};
		int [] to = {R.id.txtTodoTitle, R.id.txtTodoDueDate};
		_cursorAdapter = new MyCursorAdapter(this, R.layout.row, _cursor, from, to);	                
		_lstItems.setAdapter(_cursorAdapter);	                
		registerForContextMenu(_lstItems);

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

		}
		return true;
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			String title = data.getStringExtra("title");
			Date dueDate = (Date) data.getSerializableExtra("dueDate");
			Task t=new Task(title, dueDate);
			_tdDAL.insert(t);
			_cursor = _tdDAL.getCursor();
			_cursorAdapter.changeCursor(_cursor);
			_cursorAdapter.notifyDataSetChanged();
		}
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.contextmenu, menu);
		AdapterContextMenuInfo info =(AdapterContextMenuInfo) menuInfo;
		_cursor = _cursorAdapter.getCursor();
		_cursor.moveToPosition(info.position);
		String title = _cursor.getString(1);
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
		_cursor =_cursorAdapter.getCursor();
		_cursor.moveToPosition(selectedItemIndex);
		Task task;
 		if (_cursor.getString(2) == null) {
			task = new Task(_cursor.getString(1), null);
		} else {
			task = new Task(_cursor.getString(1), new Date(_cursor.getLong(2)));
		}
		switch (item.getItemId()){
		case R.id.menuItemDelete:
			_tdDAL.delete(task);
			_cursor = _tdDAL.getCursor();
			_cursorAdapter.changeCursor(_cursor);
			_cursorAdapter.notifyDataSetChanged();
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