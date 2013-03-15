package il.ac.huji.todolist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TasksDisplayAdapter extends ArrayAdapter<String>{
	
	
	public TasksDisplayAdapter(Context activity, List<String> tasks){
		super(activity,android.R.layout.simple_list_item_1);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		String task = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView current = (TextView)view.findViewById(R.id.newRow);
		current.setText(task);
		if (position %2==0){
			current.setTextColor(Color.RED);
		}
		else{
			current.setTextColor(Color.BLUE);
		}
		return view;
		
		
	}

}
