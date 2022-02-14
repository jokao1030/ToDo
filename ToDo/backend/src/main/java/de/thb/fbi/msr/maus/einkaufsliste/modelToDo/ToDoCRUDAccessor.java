package de.thb.fbi.msr.maus.einkaufsliste.modelToDo;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ToDoCRUDAccessor {

	//Reading
	@GET
	public List<ToDo> readAllItems();

	@GET
	public ToDo readItem(int itemId);

	//Creation
	@POST
	public ToDo createItem(ToDo item);

	//Deletion
	@DELETE
	@Path("/{id}")
	public boolean deleteItem(@PathParam("id") long itemId);

	//Modification
	@PUT
	public ToDo updateItem(ToDo item);
}
