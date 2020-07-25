package microservices.book.multiplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

public class MultiplicationServiceImplTest {
	
	private MultiplicationServiceImpl multiplicationServiceImpl;
	
	@Mock
	private RandomGeneratorService randomGeneratorService;
	
	@Mock
	private MultiplicationResultAttemptRepository attemptRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository);
	}
	
	@Test
	public void createRandomMultiplicationTest() {
		// given
		given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
	}

	@Test
	public void checkCorrectAttemptTest() {
		// given
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("John_doe");
		MultiplicationResultAttempt attempt = 
				new MultiplicationResultAttempt(user, multiplication, 3000, false);
		MultiplicationResultAttempt verifiedAttempt =
				new MultiplicationResultAttempt(user, multiplication, 3000, true);
		given(userRepository.findByAlias("John_doe")).willReturn(Optional.empty());
		
		// when
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		// assert
		assertThat(attemptResult).isTrue();
		verify(attemptRepository).save(verifiedAttempt);
	}
	
	@Test
	public void checkWrongAttemptTest() {
		// given
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("John_doe");
		MultiplicationResultAttempt attempt = 
				new MultiplicationResultAttempt(user, multiplication, 3010, false);
		given(userRepository.findByAlias("John_doe")).willReturn(Optional.empty());
		
		// when
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		// assert
		assertThat(attemptResult).isFalse();
		verify(attemptRepository).save(attempt);
	}
	
	@Test
	public void retrieveStatsTest() {
		// given
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("John_doe");
		MultiplicationResultAttempt attempt1 = 
				new MultiplicationResultAttempt(user, multiplication, 3010, false);
		MultiplicationResultAttempt attempt2 = 
				new MultiplicationResultAttempt(user, multiplication, 3051, false);
		List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
		given(userRepository.findByAlias("John_doe")).willReturn(Optional.empty());
		given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("John_doe")).willReturn(latestAttempts);
		
		// when
		List<MultiplicationResultAttempt> latestAttemptsResult = 
				multiplicationServiceImpl.getStatsForUser("John_doe");
		
		// then
		assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
	}
}
