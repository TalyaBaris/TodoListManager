package il.ac.huji.todolist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends SimpleCursorAdapter {

	private Context _context;

	//@SuppressWarnings("deprecation")
	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		_context = context;
	}

	//@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row, null);

		Cursor cur = (Cursor) getItem(position);
		cur.moveToPosition(position);

		Task task = null;

		if (cur.getString(2) == null) {
			task = new Task(cur.getString(1), null);
		} else {
			task = new Task(cur.getString(1), new Date(cur.getLong(2)));
		}

		TextView title = (TextView) view.findViewById(R.id.txtTodoTitle);
		TextView dueDate = (TextView) view.findViewById(R.id.txtTodoDueDate);

		title.setText(task.getTitle());

		if (task.getDueDate() == null) {
			dueDate.setText("No due date");
		} else {

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = df.format(task.dueDate);
			dueDate.setText(formattedDate);

			if (task.dueDate.before(new Date())){
				title.setTextColor(Color.RED);
				dueDate.setTextColor(Color.RED);
			}
		}
		return view;
	}
}