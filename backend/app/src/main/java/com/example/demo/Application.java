package com.example.demo;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository repository) {
		// creates mock employees for testing purposes
		return args -> {
			Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
				Employee e =
						new Employee(name, name, name.toLowerCase() + "@domain.com",
											new Date(1, 1, 1),
												Employee.Timezone.PST, "developer");
				repository.save(e);
			});
			repository.findAll().forEach(System.out::println);
		};
	}

}
