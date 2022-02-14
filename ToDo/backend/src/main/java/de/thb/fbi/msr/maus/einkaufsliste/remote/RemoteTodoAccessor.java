package de.thb.fbi.msr.maus.einkaufsliste.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import de.thb.fbi.msr.maus.einkaufsliste.modelToDo.ToDo;
import de.thb.fbi.msr.maus.einkaufsliste.modelToDo.ToDoCRUDAccessor;

public class RemoteTodoAccessor implements ToDoCRUDAccessor {

	protected static Logger logger = Logger
			.getLogger(RemoteTodoAccessor.class);

	/**
	 * the list of data items, note that the list is *static* as for each client
	 * request a new instance of this class will be created!
	 */
	private static List<ToDo> itemlist = new ArrayList<ToDo>();

	/**
	 * we assign the ids here
	 */
	private static int idCount = 0;
	
	@Override
	public List<ToDo> readAllItems() {
		logger.info("readAllItems(): " + itemlist);

		return itemlist;
	}

	@Override
	public ToDo readItem(int itemId){
		logger.info("readItem(): " + itemlist.get(itemId));
		return itemlist.get(itemId);
	}

	@Override
	public ToDo createItem(ToDo item) {
		logger.info("createItem(): " + item);
		item.setId(idCount++);

		itemlist.add(item);
		return item;
	}

	@Override
	public boolean deleteItem(final long itemId) {
		logger.info("deleteItem(): " + itemId);

		boolean removed = itemlist.remove(new ToDo() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 71193783355593985L;

			@Override
			public long getId() {
				return itemId;
			}
		});

		return removed;
	}

	@Override
	public ToDo updateItem(ToDo item) {
		logger.info("updateItem(): " + item);

		return itemlist.get(itemlist.indexOf(item)).updateFrom(item);
	}
}
