package microservices.book.multiplication.service;

import java.util.stream.IntStream;

import org.junit.Test;

public class IntStreamTest {
	@Test
	public void IntStreamTest() {
		IntStream.range(0, 10).forEach(System.out::println);
		
		IntStream.range(0, 10).map(i -> i + 5).forEach(System.out::println);
	}
}
