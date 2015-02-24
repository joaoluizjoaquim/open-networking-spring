package br.com.joaojoaquim.repository;

import org.springframework.data.neo4j.repository.CRUDRepository;

import br.com.joaojoaquim.bean.Person;

public interface PersonRepository extends CRUDRepository<Person> {

	
	
}
