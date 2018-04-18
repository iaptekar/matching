package igor.exercise;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;

public class MatchingEngineTest {

	private MatchingEngine matcher = new MatchingEngine();

	@Test
	public void testNoExecutions() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST2").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(10)).build();
		matcher.addOrder(order1);
		matcher.addOrder(order2);
		assertEquals(Long.valueOf(1), matcher.getOpenInterest("TEST1", Direction.BUY).get(BigDecimal.valueOf(10)));
		assertEquals(Long.valueOf(1), matcher.getOpenInterest("TEST2", Direction.SELL).get(BigDecimal.valueOf(10)));
		assertEquals(0.0, matcher.getAverageExecutionPrice("TEST1"), 0.01);
		assertEquals(0, matcher.getExecutedQuantity("TEST1", "TESTER"));
		assertEquals(0, matcher.getExecutedQuantity("TEST2", "TESTER"));
	}

	@Test
	public void testWithExecution() {
		Order order1 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(10)).build();
		Order order2 = new Order.Builder().withRIC("TEST1").withUser("TESTER").withQuantity(1)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(10)).build();
		matcher.addOrder(order1);
		matcher.addOrder(order2);
		assertEquals(new HashMap<>(), matcher.getOpenInterest("TEST1", Direction.BUY));
		assertEquals(new HashMap<>(), matcher.getOpenInterest("TEST1", Direction.SELL));
		assertEquals(10.0, matcher.getAverageExecutionPrice("TEST1"), 0.01);
		assertEquals(1, matcher.getExecutedQuantity("TEST1", "TESTER"));
		assertEquals(1, matcher.getExecutedQuantity("TEST1", "TESTER"));
	}

	@Test
	public void testExampleSequence() {
		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User1").withQuantity(1000)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(100.2)).build());

		assertEquals(Long.valueOf(1000),
				matcher.getOpenInterest("VOD.L", Direction.SELL).get(BigDecimal.valueOf(100.2)));
		assertEquals(0, matcher.getExecutedQuantity("VOD.L", "User1"));

		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User2").withQuantity(1000)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(100.2)).build());

		assertEquals(new HashMap<>(), matcher.getOpenInterest("VOD.L", Direction.BUY));
		assertEquals(new HashMap<>(), matcher.getOpenInterest("VOD.L", Direction.SELL));
		assertEquals(100.2, matcher.getAverageExecutionPrice("VOD.L"), 0.0001);
		assertEquals(-1000, matcher.getExecutedQuantity("VOD.L", "User1"));
		assertEquals(1000, matcher.getExecutedQuantity("VOD.L", "User2"));

		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User1").withQuantity(1000)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(99)).build());

		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(99)));
		assertEquals(100.2, matcher.getAverageExecutionPrice("VOD.L"), 0.0001);
		assertEquals(-1000, matcher.getExecutedQuantity("VOD.L", "User1"));
		assertEquals(1000, matcher.getExecutedQuantity("VOD.L", "User2"));

		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User1").withQuantity(1000)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(101)).build());

		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(99)));
		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(101)));
		assertEquals(100.2, matcher.getAverageExecutionPrice("VOD.L"), 0.0001);
		assertEquals(-1000, matcher.getExecutedQuantity("VOD.L", "User1"));
		assertEquals(1000, matcher.getExecutedQuantity("VOD.L", "User2"));

		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User2").withQuantity(500)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(102)).build());

		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(99)));
		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(101)));
		assertEquals(Long.valueOf(500), matcher.getOpenInterest("VOD.L", Direction.SELL).get(BigDecimal.valueOf(102)));
		assertEquals(100.2, matcher.getAverageExecutionPrice("VOD.L"), 0.0001);
		assertEquals(-1000, matcher.getExecutedQuantity("VOD.L", "User1"));
		assertEquals(1000, matcher.getExecutedQuantity("VOD.L", "User2"));

		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User1").withQuantity(500)
				.withDirection(Direction.BUY).withPrice(BigDecimal.valueOf(103)).build());

		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(99)));
		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(101)));
		assertEquals(101.1333, matcher.getAverageExecutionPrice("VOD.L"), 0.0001);
		assertEquals(-500, matcher.getExecutedQuantity("VOD.L", "User1"));
		assertEquals(500, matcher.getExecutedQuantity("VOD.L", "User2"));

		matcher.addOrder(new Order.Builder().withRIC("VOD.L").withUser("User2").withQuantity(1000)
				.withDirection(Direction.SELL).withPrice(BigDecimal.valueOf(98)).build());

		assertEquals(Long.valueOf(1000), matcher.getOpenInterest("VOD.L", Direction.BUY).get(BigDecimal.valueOf(99)));
		assertEquals(99.8800, matcher.getAverageExecutionPrice("VOD.L"), 0.0001);
		assertEquals(500, matcher.getExecutedQuantity("VOD.L", "User1"));
		assertEquals(-500, matcher.getExecutedQuantity("VOD.L", "User2"));

	}

}
