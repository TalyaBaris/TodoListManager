package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDAL extends SQLiteOpenHelper  {

	private static final String DB_NAME = "todo_db";
	public static final String TABLE_NAME = "todo";
	private static final String ID_KEY = "_id";
	public static final String TITLE_KEY = "title";
	public static final String DUE_KEY = "due";
	private static final int DB_VER = 1; 

	private SQLiteDatabase _db;

	public TodoDAL(Context context) {
		super(context, DB_NAME, null,DB_VER);
		_db=this.getWritableDatabase();
		Parse.initialize(context,
				context.getResources().getString(R.string.parseApplication),
				context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();
	}

	public Cursor getCursor() {
		String [] columns = new String[] { ID_KEY, TITLE_KEY, DUE_KEY };
		return this.getReadableDatabase().query(TABLE_NAME, columns, null, null, null, null, null);
	}

	public boolean insert(ITodoItem todoItem) { 
		try{
			_db=this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(TITLE_KEY, todoItem.getTitle());
			if (todoItem.getDueDate() != null) {
				values.put(DUE_KEY, todoItem.getDueDate().getTime());
			} else {
				values.putNull(DUE_KEY);
			}
			long success = _db.insert(TABLE_NAME, null, values);			
			_db.close();

			if (success == -1) {
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return parseInsert(todoItem);
	}

	private boolean parseUpdate(final ITodoItem todoItem){
		try{
			ParseQuery pq= new ParseQuery(TABLE_NAME);
			pq.whereEqualTo(TITLE_KEY, todoItem.getTitle());
			pq.findInBackground(new FindCallback() {
				public void done(List<ParseObject> scoreList, ParseException e) {
					if (e == null) {
						if (scoreList.size() == 1) {
							ParseObject updateObj = scoreList.get(0);
							final Date dueDate = todoItem.getDueDate();
							if (dueDate != null) {
								updateObj.put(DUE_KEY, dueDate.getTime());
							} else {
								updateObj.put(DUE_KEY, JSONObject.NULL);
							}
							updateObj.saveInBackground();
						}
					} else {//TODO
					}
				}
			});
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean update(ITodoItem todoItem) { 
		try {
			_db=this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(TITLE_KEY, todoItem.getTitle());

			if (todoItem.getDueDate() != null) {
				values.put(DUE_KEY, todoItem.getDueDate().getTime());
			} else {
				values.putNull(DUE_KEY);
			}
			String[] where= {todoItem.getTitle()};
			int success = _db.update(TABLE_NAME, values, TITLE_KEY+" = ?", where);
			_db.close();
			if (success <=0) {
				return false;
			}
		}catch(Exception e){
			return false;
		}

		return parseUpdate(todoItem);
	}

	private boolean parseInsert(ITodoItem todoItem){
		try{
			ParseObject obj = new ParseObject(TABLE_NAME);
			obj.put(TITLE_KEY, todoItem.getTitle());
			final Date dueDate = todoItem.getDueDate();
			if (dueDate != null) {
				obj.put(DUE_KEY, dueDate.getTime());
			} else {
				obj.put(DUE_KEY, JSONObject.NULL);
			}
			obj.saveInBackground();

		} catch (Exception e) {
			return false;
		}

		return true;

	}
	public boolean delete(ITodoItem todoItem) {
		_db=this.getWritableDatabase();
		String[] where = {todoItem.getTitle()};
		int success = _db.delete(TABLE_NAME, TITLE_KEY+" = ?", where);
		_db.close();

		if (success <=0) {
			return false;
		}
		return deleteParse(todoItem);
	}
	private boolean deleteParse(ITodoItem todoItem){	
		try{
		ParseQuery query = new ParseQuery(TABLE_NAME);
		query.whereEqualTo(TITLE_KEY, todoItem.getTitle());
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					if (scoreList.size() == 1) {
						ParseObject toDelete = scoreList.get(0);
						toDelete.deleteInBackground();
					}
				} else {
				}
			}
		});

	} catch (Exception e) {
		return false;
	}

	return true;
}
public List<ITodoItem> all() { 
	_db=this.getWritableDatabase();
	List<ITodoItem> todoItemList = new ArrayList<ITodoItem>();
    String queryStr = "SELECT * FROM "+TABLE_NAME;
    Cursor cursor = _db.rawQuery(queryStr, null);
    if (cursor.moveToFirst()) {
        do {
            Task task = new Task(cursor.getString(1), new Date(cursor.getLong(2)));
            todoItemList.add(task);
        } while (cursor.moveToNext());
    }
	return todoItemList;
}
@Override
public void onCreate(SQLiteDatabase db) {
	db.execSQL("create table " + TABLE_NAME +
			" ( " + ID_KEY + " integer primary key autoincrement, "
			+ TITLE_KEY + " text, "
			+ DUE_KEY + " long);");

}
@Override
public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub

}
}
