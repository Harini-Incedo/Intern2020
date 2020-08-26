package com.example.demo.specifications;

import com.example.demo.entities.Project;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class ProjectSpecifications {

    public static Specification<Project> withProjectName(String projectName) {
        if (projectName == null) {
            return null;
        } else {
            return (root, query, cb) ->
                    cb.greaterThan(cb.locate(cb.lower(root.get("projectName")),
                                        projectName.toLowerCase()), 0);
        }
    }

    public static Specification<Project> withClientName(String clientName) {
        if (clientName == null) {
            return null;
        } else {
            return (root, query, cb) ->
                    cb.greaterThan(cb.locate(cb.lower(root.get("clientName")),
                                        clientName.toLowerCase()), 0);
        }
    }

    public static Specification<Project> withStartDate(LocalDate startDate) {
        if (startDate == null) {
            return null;
        } else {
            return (root, query, cb) ->
                    cb.or(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
    }

    public static Specification<Project> withEndDate(LocalDate endDate) {
        if (endDate == null) {
            return null;
        } else {
            return (root, query, cb) ->
                    cb.or(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
        }
    }

    public static Specification<Project> withStatus(String status) {
        if (status == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("status"), status);
        }
    }

}
