package igor.exercise;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class OrderMatcherTest {

	@Test
	public void differentRICShouldNotMatch() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST2").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(10)).build();
		assertEquals(false, new OrderMatcher(order1, order2).isMatch());
	}

	@Test
	public void differentQuantityShouldNotMatch() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(2)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(10)).build();
		assertEquals(false, new OrderMatcher(order1, order2).isMatch());
	}

	@Test
	public void sameDirectionShouldNotMatch() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		assertEquals(false, new OrderMatcher(order1, order2).isMatch());
	}

	@Test
	public void sellPriceEqualToBuyPriceShouldMatch() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(10)).build();
		assertEquals(true, new OrderMatcher(order1, order2).isMatch());
	}

	@Test
	public void sellPriceGreaterThanBuyPriceShouldNotMatch() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(100)).build();
		assertEquals(false, new OrderMatcher(order1, order2).isMatch());
	}

	@Test
	public void sellPriceLowerThanBuyPriceShouldMatch() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(1)).build();
		assertEquals(true, new OrderMatcher(order1, order2).isMatch());
	}

}
