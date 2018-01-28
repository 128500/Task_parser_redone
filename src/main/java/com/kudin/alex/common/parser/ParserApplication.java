package com.kudin.alex.common.parser;

import com.kudin.alex.common.parser.entities.Tire;
import com.kudin.alex.common.parser.repositories.TireRepository;
import com.kudin.alex.common.parser.services.DataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.kudin.alex.common.parser.repositories"})
public class ParserApplication implements CommandLineRunner{

	@Autowired
	private TireRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(ParserApplication.class, args);
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		DataParser parser = new DataParser();
		List<Tire> tires = parser.parseFile("C:\\Users\\homeuser.1-HP\\Desktop\\Прайс шины.xls");

		repo.save(tires);
	}
}
