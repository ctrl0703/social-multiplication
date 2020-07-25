package microservices.book.multiplication.repository;

import org.springframework.data.repository.CrudRepository;

import microservices.book.multiplication.domain.Multiplication;

public interface MultiplicationRespository extends CrudRepository<Multiplication, Long> {
}
