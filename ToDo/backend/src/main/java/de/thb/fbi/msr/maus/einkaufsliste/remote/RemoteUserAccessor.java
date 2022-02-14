package de.thb.fbi.msr.maus.einkaufsliste.remote;

import de.thb.fbi.msr.maus.einkaufsliste.modelLogin.User;
import de.thb.fbi.msr.maus.einkaufsliste.modelLogin.UserCRUDAccessor;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RemoteUserAccessor implements UserCRUDAccessor {

	private static String myEmail = "kao@gmail.com";
	private static String myPassword = "123456";
	protected static Logger logger = Logger
			.getLogger(RemoteUserAccessor.class);

	/**
	 * the list of data items, note that the list is *static* as for each client
	 * request a new instance of this class will be created!
	 */
	private static List<User> userlist = new ArrayList<User>();

	/**
	 * we assign the ids here
	 */
	private static long idCount = 0;

	@Override
	public List<User> readAllItems() {
		logger.info("readAllItems(): " + userlist);

		return userlist;
	}

	@Override
	public Response createItem(User user) {
		logger.info("createUser(): " + user);
		user.setId(idCount++);
		userlist.add(user);
		if(user != null) {
			if (user.getEmail().equals(myEmail) && user.getPassword().equals(myPassword)) {
				user.setRegistered(true);
				String result = "The User is registered!";
				return Response.status(200).entity(result).build();
			} else {
				user.setRegistered(false);
				String result = "The User is not registered!";
				return Response.status(401).entity(result).build();
			}
		}

		return Response.status(400).build();
	}

	@Override
	public boolean deleteItem(final long userId) {
		logger.info("deleteItem(): " + userId);

		boolean removed = userlist.remove(new User() {
			/**
			 *
			 */
			private static final long serialVersionUID = 71193783355593985L;

			@Override
			public long getId() {
				return userId;
			}
		});

		return removed;
	}

	@Override
	public User updateItem(User user) {
		logger.info("updateItem(): " + user);

		return userlist.get(userlist.indexOf(user)).updateFrom(user);
	}
}


