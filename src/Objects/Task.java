package Objects;

/**
 * Represents an item and the quantity needed.
 */
public class Task {

	private final Item item;
	private final int qty;

	/**
	 * Create a job item from an item and a quantity.
	 * @param item The item.
	 * @param qty The quantity needed.
	 */
	public Task(Item item, int qty) {

		this.item = item;
		this.qty = qty;

	}

	/**
	 * Get the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Get the quantity.
	 * @return The quantity.
	 */
	public int getQuantity() {
		return qty;
	}

	// toString method for debugging purposes
	@Override
	public String toString() {
		return "Task [item=" + item + ", qty=" + qty + "]";
	}
}