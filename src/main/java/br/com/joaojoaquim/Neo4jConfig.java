package br.com.joaojoaquim;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@EnableNeo4jRepositories
@Configuration
public class Neo4jConfig extends Neo4jConfiguration{

	public Neo4jConfig() {
		setBasePackage("br.com.joaojoaquim");
	}
	
	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabase("target/open-networking.db");
	}
	
}
