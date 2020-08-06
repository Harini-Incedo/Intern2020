package com.example.demo;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.EngagementRepository;
import com.example.demo.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository empRepository, ProjectRepository projRepository,
								EngagementRepository engRepository) {
		return args -> {
			List<String> skillsList = Arrays.asList("Java","Python","C","C++","UI","SQL","Cloud Computing",
					"Ruby","R","Data Science","Machine Learning","Go","Finance",
					"Marketing","Human Resource","Management");
			Random random = new Random();
			// creates mock active employees for testing
			Stream.of("John Cena Analyst", "Sponge Robert Manager",
						"Jennifer Aniston Developer", "Harry Styles Intern",
							"Nancy Drew Consultant").forEach(name -> {
				String[] info = name.split(" ");
				HashSet<String> mockSkills = new HashSet<>();
				mockSkills.add(skillsList.get(random.nextInt(10)));
				mockSkills.add("Management");
				Employee e = new Employee(info[0], info[1],
										info[0].toLowerCase() + "@domain.com",
												LocalDate.of(2019, 1, 1),
													Employee.Timezone.EST, info[2], mockSkills);
				e.setDepartment("Telecom");
				e.setLocation("New Jersey");
				e.setManager("Chandler Bing");
				e.setEndDate(LocalDate.of(2020, 1, 1));
				e.setWorkingHours("Full Time");

				empRepository.save(e);
			});

			// creates mock inactive employees for testing
			Stream.of("Billy Joel HR", "Fred Heebie Intern",
					"Bill Jeebie Intern").forEach(name -> {
				String[] info = name.split(" ");
				Employee e = new Employee(info[0], info[1],
						info[0].toLowerCase() + "@domain.com",
						LocalDate.of(2019, 1, 1),
						Employee.Timezone.EST, info[2],new HashSet<>());
				e.setDepartment("Healthcare");
				e.setLocation("New York");
				e.setManager("Joey Tribbiani");
				e.setEndDate(LocalDate.of(2020, 1, 1));
				e.setWorkingHours("Part Time");
				e.setActive(false);
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
				p.setEndDate(LocalDate.of(2019, 3, 1));
				p.setWeeklyHours(300);
				projRepository.save(p);
			});
			projRepository.findAll().forEach(System.out::println);

//			// creates mock engagements for testing
//			Stream.of("3 9 40 Developer", "2 9 40 Manager", "5 10 40 Consultant",
//					"1 10 40 Analyst", "4 10 40 Intern").forEach(name -> {
//				String[] info = name.split(" ");
//				Engagement p = new Engagement(Long.parseLong(info[1]), 1,
//												LocalDate.of(2020, 6, 1),
//												LocalDate.of(2020, 7, 1));
//				engRepository.save(p);
//			});
//			engRepository.findAll().forEach(System.out::println);

		};
	}

}
