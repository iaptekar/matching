package igor.exercise;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Class representing an order on stocks. Use Builder to construct.
 *
 */
public class Order implements Comparable<Order>, Matchable<Order> {

	private Direction direction;
	private String ric;
	private long quantity;
	private BigDecimal price;
	private String user;
	private final Instant timestamp = Instant.now();

	@Override
	public boolean matches(Order order) {
		return new OrderMatcher(order, this).isMatch();
	}

	public boolean isDirectionOppositeTo(Order order) {
		return direction.isOppositeTo(order.direction);
	}

	public Direction getDirection() {
		return direction;
	}

	public String getRic() {
		return ric;
	}

	public long getQuantity() {
		return quantity;
	}

	public String getUser() {
		return user;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public boolean isSellSide() {
		return direction == Direction.SELL;
	}

	public boolean isBuySide() {
		return direction == Direction.BUY;
	}

	@Override
	public int compareTo(Order o) {
		return timestamp.compareTo(o.timestamp);
	}

	public boolean hasSamePrice(Order order) {
		return order == null ? false : price.compareTo(order.getPrice()) == 0;
	}

	public boolean hasLowerPrice(Order order) {
		return order == null ? false : price.compareTo(order.getPrice()) < 0;
	}

	public boolean hasGreaterPrice(Order order) {
		return order == null ? false : price.compareTo(order.getPrice()) > 0;
	}

	@Override
	public String toString() {
		return "Order [direction=" + direction + ", ric=" + ric + ", quantity=" + quantity + ", price=" + price
				+ ", user=" + user + ", timestamp=" + timestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + (int) (quantity ^ (quantity >>> 32));
		result = prime * result + ((ric == null) ? 0 : ric.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Order other = (Order) obj;
		if (direction != other.direction)
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity != other.quantity)
			return false;
		if (ric == null) {
			if (other.ric != null)
				return false;
		} else if (!ric.equals(other.ric))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/**
	 * Builder for Orders
	 *
	 */
	public static class Builder {

		private final Order order = new Order();

		public Builder withRIC(String ric) {
			order.ric = ric;
			return this;
		}

		public Builder withUser(String user) {
			order.user = user;
			return this;
		}

		public Builder withDirection(Direction direction) {
			order.direction = direction;
			return this;
		}

		public Builder withPrice(BigDecimal price) {
			order.price = price;
			return this;
		}

		public Builder withQuantity(long quantity) {
			order.quantity = quantity;
			return this;
		}

		public Order build() {
			if (order.ric == null || order.ric.isEmpty()) {
				throw new IllegalArgumentException("Order must have a valid RIC");
			}
			if (order.user == null || order.user.isEmpty()) {
				throw new IllegalArgumentException("Order must have a valid User");
			}
			if (order.price == null || order.price.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Order price must be greater than zero");
			}
			if (order.quantity <= 0) {
				throw new IllegalArgumentException("Order quantity must be greater than zero");
			}
			if (order.direction == null) {
				throw new IllegalArgumentException("Order direction must be specified");
			}
			return order;
		}

	}

}
