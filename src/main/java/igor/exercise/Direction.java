package igor.exercise;

public enum Direction {

	BUY, SELL;

	public boolean isOppositeTo(Direction direction) {
		return (this == BUY && direction == SELL) || (this == SELL && direction == BUY);
	}

}
