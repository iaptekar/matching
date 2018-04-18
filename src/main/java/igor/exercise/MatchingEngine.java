package igor.exercise;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A simple matching engine that matches new orders and create an Execution if a match is found
 * 
 */
public class MatchingEngine {

	private final Collection<Order> openOrders = new HashSet<>();

	private final ReadWriteLock orderLock = new ReentrantReadWriteLock();

	private final Collection<Execution> executions = new HashSet<>();

	private final ReadWriteLock executionLock = new ReentrantReadWriteLock();

	public void addOrder(Order order) {
		orderLock.writeLock().lock();
		Order match = null;
		try {
			Collection<Order> matches = openOrders.stream().filter(o -> o.matches(order)).sorted().collect(toList());
			if (matches.isEmpty()) {
				if (!openOrders.add(order)) {
					System.err.println("New order " + order + " could not be added to open orders");
				}
				return;
			}
			for (Order o : matches) {
				if (match == null) {
					match = o;
				}
				if (order.hasSamePrice(match)) {
					match = o;
					break;
				} else {
					if (order.isSellSide()) {
						match = o.hasGreaterPrice(match) ? o : match;
					} else {
						match = o.hasLowerPrice(match) ? o : match;
					}
				}
			}
			if (!openOrders.remove(match)) {
				System.err.println("Match " + match + " could not be removed from open orders");
			}
		} finally {
			orderLock.writeLock().unlock();
		}
		Execution execution = new Execution(match, order, order.getPrice());
		executionLock.writeLock().lock();
		try {
			if (!executions.add(execution)) {
				System.err.println("New execution " + execution + " could not be added to executions");
			}
		} finally {
			executionLock.writeLock().unlock();
		}
	}

	public Map<BigDecimal, Long> getOpenInterest(String ric, Direction direction) {
		orderLock.readLock().lock();
		try {
			Map<BigDecimal, Long> openInterest = new HashMap<>();
			for (Order o : openOrders) {
				if (o.getRic().equals(ric) && o.getDirection() == direction) {
					openInterest.putIfAbsent(o.getPrice(), 0L);
					openInterest.computeIfPresent(o.getPrice(), (k, v) -> v + o.getQuantity());
				}
			}
			return openInterest;
		} finally {
			orderLock.readLock().unlock();
		}
	}

	public double getAverageExecutionPrice(String ric) {
		executionLock.readLock().lock();
		try {
			long units = 0;
			double total = 0.0;
			for (Execution e : executions) {
				if (e.getRic().equals(ric)) {
					units += e.getQuantity();
					total += e.getQuantity() * e.getPrice().doubleValue();
				}
			}
			return units == 0 ? 0.0 : total / units;
		} finally {
			executionLock.readLock().unlock();
		}
	}

	public long getExecutedQuantity(String ric, String user) {
		executionLock.readLock().lock();
		try {
			return executions.stream().filter(e -> e.getRic().equals(ric) && e.hasUser(user))
					.mapToLong(e -> e.getQuantityForUser(user)).sum();
		} finally {
			executionLock.readLock().unlock();
		}
	}

}
