package com.example.demo;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Project;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository empRepository, ProjectRepository projRepository) {
		return args -> {

			// creates mock employees for testing
			Stream.of("John Cena Business Analyst", "Sponge Robert Project Manager",
						"Jennifer Aniston Web Developer", "Harry Styles College Recruiter",
							"Nancy Drew IT Consultant").forEach(name -> {
				String[] info = name.split(" ");
				Employee e = new Employee(info[0], info[1],
										info[0].toLowerCase() + "@domain.com",
												LocalDate.of(2019, 1, 1),
													Employee.Timezone.EST,
													info[2] + " " + info[3]);
				e.setDepartment("Telecom");
				e.setLocation("New Jersey");
				e.setManager("Chandler Bing");
				e.setEndDate(LocalDate.of(2020, 1, 1));
				e.setWorkingHours("Part Time");
				empRepository.save(e);
			});
			empRepository.findAll().forEach(System.out::println);

			// creates mock projects for testing
			Stream.of("TestProject-1 TMobile", "TestProject-2 Microsoft", "TestProject-3 TicketMaster",
						"TestProject-4 Google", "TestProject-5 Sony").forEach(name -> {
				String[] info = name.split(" ");
				Project p = new Project(info[0], info[1],
										LocalDate.of(2019, 1, 1),
									"In Progress");
				p.setDepartment("Telecom");
				p.setProjectGoal("To make money!");
				p.setTeamSize(10);
				p.setEndDate(LocalDate.of(2021, 1, 1));
				p.setWeeklyHours(300);
				projRepository.save(p);
			});
			projRepository.findAll().forEach(System.out::println);

		};
	}

}
