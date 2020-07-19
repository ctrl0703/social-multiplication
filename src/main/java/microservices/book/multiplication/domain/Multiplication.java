package microservices.book.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class Multiplication {
	private final int factorA;
	private final int factorB;
	
	// JSON (역)직렬화를 위한 빈 생성
	Multiplication() {
		this(0, 0);
	}
}
