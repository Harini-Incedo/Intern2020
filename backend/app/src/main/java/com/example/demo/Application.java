package com.example.demo;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import com.example.demo.entities.Skill;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.EngagementRepository;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository empRepository, ProjectRepository projRepository,
						   EngagementRepository engRepository, SkillRepository skillRepository) {
		return args -> {

			// random integer generator
			Random rand = new Random();

			// predefined values needed for randomization
			String[] skillsList = {"Java","Python","UI","SQL", "Cloud Computing","Marketing","Management"};
			String[] employmentType = {"Part Time", "Full Time", "Temporary"};
			String[] roles = {"Analyst", "Developer", "Consultant", "Data Scientist",
									"Intern", "Manager", "HR", "Assistant"};
			String[] mockManagerNames = { "Peter Pan", "Tinker Bell", "Sponge Robert" };

			// maps timezones to practical locations
			HashMap<Employee.Timezone, String> locations = new HashMap<>();
			locations.put(Employee.Timezone.IST, "Bangalore");
			locations.put(Employee.Timezone.EST, "New Jersey");
			locations.put(Employee.Timezone.PST, "Santa Clara");

			// set of pre-defined manager names
			HashSet<String> mockManagersSet= new HashSet<>();
			for (String name : mockManagerNames) {
				mockManagersSet.add(name);
			}

			// set of pre-defined inactive employee names
			HashSet<String> mockInactiveEmployeeNames = new HashSet<>();
			mockInactiveEmployeeNames.add("Mickey Mouse");
			mockInactiveEmployeeNames.add("Minnie Mouse");
			mockInactiveEmployeeNames.add("Donald Duck");
			mockInactiveEmployeeNames.add("Daisy Duck");
			mockInactiveEmployeeNames.add("Buzz Lightyear");

			// full set of pre-defined employee names
			String[] mockEmployeeNames = {	"Mickey Mouse",
											"Minnie Mouse",
											"Donald Duck",
											"Daisy Duck",
											"Buzz Lightyear",
											"Harry Potter",
											"Albus Dumbledore",
											"Lord Voldemort",
											"Severus Snape",
											"Draco Malfoy",
											"Hermione Granger",
											"Ron Weasley",
											"Rachel Greene",
											"Phoebe Buffay",
											"Ross Geller",
											"Chandler Bing",
											"Monica Geller",
											"Joey Tribbiani",
											"Shrek Ogre",
											"Donkey Donkey",
											"Princess Fiona",
											"Lord Farquaad",
											"Peter Pan",
											"Tinker Bell",
											"Sponge Robert",
											"John Cena",
											"Harry Styles",
											"Niall Horan",
											"Zayn Malik",
											"Louis Tomlinson",
											"Liam Payne"	};

			//////////////////////////////////////////////
			///// LOADS MOCK EMPLOYEES INTO DATABASE /////
			//////////////////////////////////////////////
			for (String name : mockEmployeeNames) {
				// for extracting names
				String[] names = name.split(" ");

				// adds up to 5 random skills to each employee's skill set
				HashSet<String> mockSkills = new HashSet<>();
				for (int i = 0; i < 5; i++) {
					mockSkills.add(skillsList[rand.nextInt(skillsList.length)]);
				}

				// assigns manager-specific traits if the current name is a
				// mock manager name, otherwise assigns randomized traits
				boolean isManager = mockManagersSet.contains(name);
				String role = isManager ? "Manager" : roles[rand.nextInt(8)];
				String manager = isManager ? "Jack Sparrow" : mockManagerNames[rand.nextInt(mockManagerNames.length)];
				if (isManager) {
					mockSkills.add("Management");
				}

				// creates an employee object with current name and selected traits
				Employee e = new Employee(names[0], names[1],
						names[0].toLowerCase() + names[1].toLowerCase() + "@domain.com",
								LocalDate.of(2019, 1, 1),
									Employee.Timezone.getRandomTimezone(rand),
										role, mockSkills);

				// sets fixed department
				e.setDepartment("Telecom");
				// sets a location matching the random timezone
				e.setLocation(locations.get(e.getTimezone()));
				// sets random manager
				e.setManager(manager);
				// sets a random end date > start date
				e.setEndDate(LocalDate.of(2021, rand.nextInt(11) + 1 , 1));
				// sets random employment type
				e.setWorkingHours(employmentType[rand.nextInt(employmentType.length)]);

				// sets active status if current name is an mock inactive employee
				if (mockInactiveEmployeeNames.contains(name)) {
					e.setActive(false);
				}

				// saves mock employee to the employee database
				empRepository.save(e);
			}

			/////////////////////////////////////////////
			///// LOADS MOCK PROJECTS INTO DATABASE /////
			/////////////////////////////////////////////
			Stream.of("TestProject-1 TMobile", "TestProject-2 Microsoft", "TestProject-3 TicketMaster",
						"TestProject-4 Google", "TestProject-5 Sony","TestProject-6 Verizon",
							"TestProject-7 Spotify", "TestProject-8 Kleenex", "TestProject-9 Samsung",
								"TestProject-10 AT&T").forEach(name -> {

				// for extracting string info
				String[] info = name.split(" ");

				// creates a new project with the given details
				Project p = new Project(info[0], info[1], LocalDate.of(2019, 1, 1),
										"In Progress");

				// sets fixed department and project goal
				p.setDepartment("Telecom");
				p.setProjectGoal("To help the client leverage their digital and data " +
									"infrastructure to deliver world class customer experience " +
										"and optimize their network and business operations.");

				// sets randomized team size: between 5-20 and
				// reasonable weekly hours based on team size.
				p.setTeamSize(rand.nextInt(16) + 5);
				p.setWeeklyHours(p.getTeamSize() * 40);

				// sets project end date to be randomly between 1-5 months ahead of start date
				p.setEndDate(LocalDate.of(2019, rand.nextInt(2) + 3, 1));

				// saves mock employee to the employee database
				projRepository.save(p);

			});

			List<Project> projects = projRepository.findAllProjects();
			List<Employee> employees = empRepository.findAllActive();

			/////////////////////////////////////////////////////////
			///// LOADS MOCK SKILLS & ENGAGEMENTS INTO DATABASE /////
			/////////////////////////////////////////////////////////
			for (Project p : projects) {

				// indices to help with randomization
				int randomSkillIndex = rand.nextInt(skillsList.length);
				int randomEmployeeIndex = rand.nextInt(employees.size());

				// generates random number of skills on project: between 1-5
				int randomSkillsCount = rand.nextInt(5) + 1;
				for (int i = 0; i < randomSkillsCount; i++) {
					// gets a random skill from the skills list and adds it to project
					Skill currSkill = new Skill(skillsList[(randomSkillIndex++) % skillsList.length],
															80, p.getId(), p.getStartDate(), p.getEndDate());
					skillRepository.save(currSkill);

					// generates random number of engagements under current skill: between 1-3
					int randomEmployeeCount = rand.nextInt(3) + 1;
					for (int j = 0; j < randomEmployeeCount; j++) {
						// gets a random employee and ensures they have current skill
						Employee employee = employees.get((randomEmployeeIndex++) % employees.size());
						employee.getSkills().add(currSkill.getSkillName());
						empRepository.save(employee);
						// engages the employee on this project under current skill
						Engagement engagement = new Engagement(p.getId(), currSkill.getId(),
																	p.getStartDate(), p.getEndDate(),
																	40, false);
						engagement.setEmployeeID(employee.getId());
						engRepository.save(engagement);
					}
				}

			}

		};
	}

}
