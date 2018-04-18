package igor.exercise;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Class representing an order execution
 *
 */
public class Execution {

	private final Instant timestamp = Instant.now();
	private final Order order1;
	private final Order order2;
	private final BigDecimal price;
	private final String ric;
	private final long quantity;

	public Execution(Order order1, Order order2, BigDecimal price) {
		this.ric = order1.getRic();
		this.order1 = order1;
		this.order2 = order2;
		this.quantity = order1.getQuantity();
		this.price = price;
	}

	public String getRic() {
		return ric;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public boolean hasUser(String user) {
		return order1.getUser().equals(user) || order2.getUser().equals(user);
	}

	public long getQuantityForUser(String user) {
		Order order = order1.getUser().equals(user) ? order1 : (order2.getUser().equals(user) ? order2 : null);
		if (order == null) {
			return 0L;
		}
		return order.isBuySide() ? order.getQuantity() : -order.getQuantity();
	}

	public long getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "Execution [order1=" + order1 + ", order2=" + order2 + ", price=" + price + ", ric=" + ric + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order1 == null) ? 0 : order1.hashCode());
		result = prime * result + ((order2 == null) ? 0 : order2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Execution other = (Execution) obj;
		if (order1 == null) {
			if (other.order1 != null)
				return false;
		} else if (!order1.equals(other.order1))
			return false;
		if (order2 == null) {
			if (other.order2 != null)
				return false;
		} else if (!order2.equals(other.order2))
			return false;
		return true;
	}

}
