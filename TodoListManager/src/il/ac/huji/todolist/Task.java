package il.ac.huji.todolist;

import java.util.Date;

public class Task implements ITodoItem{
	
	public Date dueDate;
	public String name;
		
		public Task(String name, Date dueDate2) {
			this.name = name;
			this.dueDate = dueDate2;
		}

		@Override
		public String getTitle() {
			return this.name;
		}

		@Override
		public Date getDueDate() {
			return  this.dueDate;
		}

	

	

}
