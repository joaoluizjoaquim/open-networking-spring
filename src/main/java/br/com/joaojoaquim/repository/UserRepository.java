package br.com.joaojoaquim.repository;

import org.springframework.data.neo4j.repository.CRUDRepository;

import br.com.joaojoaquim.bean.User;

public interface UserRepository extends CRUDRepository<User> {

	
	
}
