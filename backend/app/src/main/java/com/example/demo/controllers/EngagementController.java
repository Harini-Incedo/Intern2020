package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import com.example.demo.entities.Skill;
import com.example.demo.repositories.SkillRepository;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.EngagementRepository;
import com.example.demo.validation.EntityNotFoundException;
import com.example.demo.validation.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EngagementController {

    @Autowired
    private EngagementRepository repository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ProjectRepository projectRepository;

    // To get all engagements associated with the given project.
    // Returns a list in the form: { (eng-1, emp-1, null), (eng-2, emp-2, null) }
    @GetMapping("projects/{id}/engagements")
    public List<EngagementsBySkill> getEngagementsByProjectID(@PathVariable("id") Long id)
            throws EntityNotFoundException {
        // checks to see if given project ID is valid
        Project p = repository.findProjectByID(id);
        if (p == null) {
            throw new EntityNotFoundException("No project exists with this ID: " + id,
                    "Please use a valid project ID.");
        }

        // retrieves a list of the skills on the given project
        List<Skill> allSkills = skillRepository.getSkillByProjectID(id);
        if (allSkills == null) {
            throw new EntityNotFoundException("No Skills exists with this ProjectID: " + id,
                    "Please use a valid project ID.");
        }

        List<EngagementsBySkill> toReturn = new ArrayList<>();
        Collections.sort(allSkills, Comparator.comparing(Skill::getSkillName));

        // for each skill, wraps the skill and it's associated
        // engagements together in an container object.
        for (Skill s: allSkills) {
            List<Engagement> temp = projectRepository.getEngagementBySkill(id,s.getId());
            List<EngagementContainer> engagementsBySkill = new ArrayList<>();
            for (Engagement e: temp) {
                // find the employee associated with engagement e
                long employeeID = e.getEmployeeID();
                Employee emp = repository.findEmployeeByID(employeeID);
                // creates a new triple with engagement e and it's associated
                // employee grouped together
                engagementsBySkill.add(new EngagementContainer(e, emp));
            }
            EngagementsBySkill skillGrouping = new EngagementsBySkill(s.getSkillName(),
                                                        s.getTotalWeeklyHours(), engagementsBySkill);
            toReturn.add(skillGrouping);
        }
        
        return toReturn;
    }


    // To get all engagements associated with the given employee.
    // Returns a list in the form: { (eng-1, null, proj-1), (eng-2, null, proj-2) }
    @GetMapping("employees/{id}/engagements")
    public List<EngagementContainer> getEngagementsByEmployeeID(@PathVariable("id") Long id)
            throws EntityNotFoundException {
        // checks to see if given employee ID is valid
        Employee emp = repository.findEmployeeByID(id);
        if (emp == null) {
            throw new EntityNotFoundException("No Employee exists with this ID: " + id,
                    "Please use a valid employee ID.");
        }

        List<EngagementContainer> toReturn = new ArrayList<>();

        List<Engagement> engagements = repository.findEngagementsByEmployeeID(id);
        for (Engagement e : engagements) {
            // find the employee associated with engagement e
            long projectID = e.getProjectID();
            Project temp = repository.findProjectByID(projectID);
            // creates a new triple with engagement e and it's associated
            // project grouped together
            toReturn.add(new EngagementContainer(e, temp));
        }

        return toReturn;
    }

    // Creates a new engagement in the database with the given information
    @PostMapping("/engagements")
    void createEngagement(@RequestBody Engagement e) throws InvalidInputException {
        // INPUT VALIDATION //
        validateEngagementDetails(e);
        repository.save(e);
    }

    // Deletes the engagement with the given ID
    @DeleteMapping("engagements/{id}")
    void deleteEngagementByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        EngagementContainer toDelete = getEngagementByID(id);
        repository.delete(toDelete.engagement);
    }

    // Returns the engagement with the given ID, if it exists
    @GetMapping("engagements/{id}")
    private EngagementContainer getEngagementByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Optional<Engagement> engagement = repository.findById(id);
        if (engagement.isPresent()){
            Engagement e = engagement.get();
            // find the employee associated with engagement e
            long employeeID = e.getEmployeeID();
            Employee temp = repository.findEmployeeByID(employeeID);
            // returns a new triple with engagement e and it's associated
            // employee grouped together
            return new EngagementContainer(e, temp);
        }
        throw new EntityNotFoundException("No engagement exists with this ID: " + id,
                                                        "Please use a valid engagement ID.");
    }

    // Updates the engagement with the given ID if it exists
    @PutMapping("engagements/{id}")
    void updateEngagementByID(@PathVariable("id")Long id, @RequestBody Engagement e)
                    throws EntityNotFoundException, InvalidInputException {
        EngagementContainer data = getEngagementByID(id);
        Engagement toUpdate = data.engagement;

        // INPUT VALIDATION //
        validateEngagementDetails(e);

        toUpdate.setEmployeeID(e.getEmployeeID());
        toUpdate.setProjectID(e.getProjectID());
        toUpdate.setStartDate(e.getStartDate());
        toUpdate.setEndDate(e.getEndDate());
        toUpdate.setSkillID(e.getSkillID());

        // updates only the hourly mappings which have changed from the UI
        Map<LocalDate, Integer> existingHourlyMapping = toUpdate.getAssignedWeeklyHours();

        Map<LocalDate, Integer> weeksToUpdate = e.getAssignedWeeklyHours();
        Set<LocalDate> dates = weeksToUpdate.keySet();

        for (LocalDate d: dates) {
            existingHourlyMapping.replace(d, weeksToUpdate.get(d));
        }

        repository.save(toUpdate);
    }

    // Updates the given engagement's hourly mappings for the weeks,
    // starting with startDate and going forward weekCount # of weeks
    // after startDate, with the given newHours.
    @PutMapping("engagements/{id}/hours")
    void massUpdateHoursByEngagementID(@PathVariable("id")Long id,
                                       @RequestBody HashMap<String, String> values)
                                            throws EntityNotFoundException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(values.get("startDate"), dtf);
        int weekCount = Integer.parseInt(values.get("weekCount"));
        int newHours = Integer.parseInt(values.get("newHours"));

        EngagementContainer data = getEngagementByID(id);
        Engagement toUpdate = data.engagement;

        toUpdate.massUpdateHours(startDate, weekCount, newHours);
        repository.save(toUpdate);

    }

    // Validates engagement details input by the user
    private void validateEngagementDetails(Engagement e) throws InvalidInputException {
        /* Start Date */
        if (e.getStartDate() != null && !e.getStartDate().isAfter(LocalDate.of(2011, 12, 31))) {
            throw new InvalidInputException("Start Date is invalid: " + e.getStartDate(),
                    "Start Date should be on or after January 1st, 2012.");
        }
        /* End Date */
        if (e.getEndDate() != null && !e.getEndDate().isAfter(e.getStartDate())) {
            throw new InvalidInputException("End Date is invalid: " + e.getEndDate(),
                    "End Date should be equal to or later than Start Date.");
        }
    }

    // A helper/container class which allows grouping of engagement objects
    // with their associated employee and project objects.
    public class EngagementContainer {

        public Engagement engagement;
        public Employee employee;
        public Project project;

        // constructs a triple with the engagement and employee
        // field populated, and the project field nullified
        public EngagementContainer(Engagement eng, Employee emp) {
            engagement = eng;
            employee = emp;
            project = null;
        }

        // constructs a triple with the engagement and project
        // field populated, and the employee field nullified
        public EngagementContainer(Engagement eng, Project proj) {
            engagement = eng;
            employee = null;
            project = proj;
        }

    }

    // A helper/container class which allows grouping of EngagementContainer
    // objects and the skill associated with them.
    public class EngagementsBySkill {
        public String skillName;
        public int totalWeeklyHours;
        public List<EngagementContainer> engagements;

        public EngagementsBySkill(String skillName, int totalWeeklyHours,
                                            List<EngagementContainer> engagements) {
            this.skillName = skillName;
            this.totalWeeklyHours = totalWeeklyHours;
            this.engagements = engagements;
        }
    }


}
