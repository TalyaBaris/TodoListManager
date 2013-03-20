package il.ac.huji.todolist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TasksDisplayAdapter extends ArrayAdapter<Task>{


	public TasksDisplayAdapter(Context activity, List<Task> tasks){
		super(activity,android.R.layout.simple_list_item_1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		Task task = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
	
		TextView title = (TextView)view.findViewById(R.id.txtTodoTitle);
		TextView dueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		title.setText(task.name);
		if(task.dueDate==null){
			dueDate.setText("dueDate.setText");
		}else{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = df.format(task.dueDate);
			dueDate.setText(formattedDate);
		}
		if (task.dueDate.before(new Date())){
			title.setTextColor(Color.RED);
			dueDate.setTextColor(Color.RED);
		}

		return view;


	}

}
