package com.example.demo.specifications;

import com.example.demo.entities.Employee;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;

public class EmployeeSpecifications {

    public static Specification<Employee> withFirstName(String firstName) {
        if (firstName == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("firstName"), firstName);
        }
    }

    public static Specification<Employee> withLastName(String lastName) {
        if (lastName == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("lastName"), lastName);
        }
    }

    public static Specification<Employee> withDepartment(String department) {
        if (department == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("department"), department);
        }
    }

    public static Specification<Employee> withRole(String role) {
        if (role == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("role"), role);
        }
    }

    public static Specification<Employee> withLocation(String location) {
        if (location == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("location"), location);
        }
    }

    public static Specification<Employee> withSkills(String skillString) {
        if (skillString == null) {
            return null;
        } else {
            return (root, query, cb) -> {
                String[] skills = skillString.split(" ");
                Predicate predicate = cb.isMember(skills[0], root.get("skills"));
                for (String skill : skills) {
                    predicate = cb.or(predicate, cb.isMember(skill, root.get("skills")));
                }
                return predicate;
            };
        }
    }


}
