package microservices.book.multiplication.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {
	
	private RandomGeneratorService randomGeneratorService;
	private MultiplicationResultAttemptRepository attemptRepository;
	private UserRepository userRepository;
	
	@Autowired
	public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService,
									MultiplicationResultAttemptRepository attemptRepository,
									UserRepository userRepository) {
		this.randomGeneratorService = randomGeneratorService;
		this.attemptRepository = attemptRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		return new Multiplication(factorA, factorB);
	}

	@Transactional
	@Override
	public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
		Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());
		
		Assert.isTrue(!attempt.isCorrect(), "채점한 상태로 보낼 수 없습니다!!");

		boolean isCorrect = attempt.getResultAttempt() ==
				attempt.getMultiplication().getFactorA() *
				attempt.getMultiplication().getFactorB();
		
		MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
															user.orElse(attempt.getUser()), 
															attempt.getMultiplication(), 
															attempt.getResultAttempt(), 
															isCorrect);
		
		attemptRepository.save(checkedAttempt);
		return isCorrect;
	}

}
