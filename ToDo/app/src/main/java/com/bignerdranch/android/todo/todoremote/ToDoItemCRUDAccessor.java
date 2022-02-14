package com.bignerdranch.android.todo.todoremote;
import javax.ws.rs.*;
import java.util.List;


@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ToDoItemCRUDAccessor {
	
	@GET
	public List<ToDoR> readAllTodos();
	
	@POST
	public ToDoR createToDo(ToDoR todo);

	@DELETE
	@Path("/{todoId}")
	public boolean deleteToDo(@PathParam("todoId") long todoId);

	@PUT
	public ToDoR updateToDo(ToDoR todo);
}
