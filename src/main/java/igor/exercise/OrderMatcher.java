package igor.exercise;

public class OrderMatcher {

	private final Order order1;
	private final Order order2;

	public OrderMatcher(Order order1, Order order2) {
		this.order1 = order1;
		this.order2 = order2;
	}

	public boolean isMatch() {
		boolean matches = order1.isDirectionOppositeTo(order2) && order1.getRic().equals(order2.getRic())
				&& order1.getQuantity() == order2.getQuantity();
		if (matches) {
			if (order1.isSellSide()) {
				matches = matches && order1.getPrice().compareTo(order2.getPrice()) <= 0;
			} else {
				matches = matches && order1.getPrice().compareTo(order2.getPrice()) >= 0;
			}
		}
		return matches;
	}
}
