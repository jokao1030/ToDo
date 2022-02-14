package de.thb.fbi.msr.maus.einkaufsliste.modelLogin;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/users")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface UserCRUDAccessor {
	
	@GET
	public List<User> readAllItems();
	
	@POST
	public Response createItem(@Valid User user);

	@DELETE
	@Path("/{userId}")
	public boolean deleteItem(@PathParam("userId") long userId);

	@PUT
	public User updateItem(User user);
}
