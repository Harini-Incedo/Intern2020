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
		return args -> {
			// creates mock employees for testing purposes
			Stream.of("John Cena Business Analyst", "Sponge Robert Project Manager",
						"Jennifer Aniston Web Developer", "Harry Styles College Recruiter",
							"Nancy Drew IT Consultant").forEach(name -> {
				String[] info = name.split(" ");
				Employee e = new Employee(info[0], info[1],
										info[0].toLowerCase() + "@domain.com",
												new Date(120, 1, 1),
													Employee.Timezone.PST,
													info[2] + " " + info[3]);
				e.setDepartment(Department.TELECOM);
				e.setLocation("New Jersey");
				e.setManager("Chandler Bing");
				repository.save(e);
			});
			repository.findAll().forEach(System.out::println);
		};
	}

}
