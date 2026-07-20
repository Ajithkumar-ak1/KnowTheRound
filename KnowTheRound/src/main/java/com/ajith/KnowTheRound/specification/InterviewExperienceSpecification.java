package com.ajith.KnowTheRound.specification;

import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
import com.ajith.KnowTheRound.model.InterviewExperience;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import com.ajith.KnowTheRound.model.Technology;
import java.util.List;
public class InterviewExperienceSpecification {

    public static Specification<InterviewExperience> hasCompany(String company) {
        return (root, query, cb) ->
                company == null || company.isBlank()
                        ? null
                        : cb.equal(
                        cb.lower(root.get("company").get("name")),
                        company.toLowerCase()
                );
    }

    public static Specification<InterviewExperience> hasJobRole(String jobRole) {
        return (root, query, cb) ->
                jobRole == null || jobRole.isBlank()
                        ? null
                        : cb.equal(
                        cb.lower(root.get("jobRole").get("name")),
                        jobRole.toLowerCase()
                );
    }

    public static Specification<InterviewExperience> hasTechnologies(List<String> technologies) {

        return (root, query, cb) -> {

            if (technologies == null || technologies.isEmpty()) {
                return null;
            }

            query.distinct(true);

            Join<InterviewExperience, Technology> technologyJoin =
                    root.join("technologies");

            return cb.lower(technologyJoin.get("name"))
                    .in(
                            technologies.stream()
                                    .map(String::toLowerCase)
                                    .toList()
                    );
        };
    }

    public static Specification<InterviewExperience> hasDifficulty(Difficulty difficulty) {
        return (root, query, cb) ->
                difficulty == null
                        ? null
                        : cb.equal(root.get("difficulty"), difficulty);
    }

    public static Specification<InterviewExperience> hasResult(InterviewResult result) {
        return (root, query, cb) ->
                result == null
                        ? null
                        : cb.equal(root.get("result"), result);
    }

    public static Specification<InterviewExperience> hasLocation(String location) {
        return (root, query, cb) ->
                location == null || location.isBlank()
                        ? null
                        : cb.like(
                        cb.lower(root.get("location")),
                        "%" + location.toLowerCase() + "%"
                );
    }

    public static Specification<InterviewExperience> filterBy(
            String company,
            String jobRole,
            List<String> technologies,
            Difficulty difficulty
    ) {
        return Specification.where(hasCompany(company))
                .and(hasJobRole(jobRole))
                .and(hasTechnologies(technologies))
                .and(hasDifficulty(difficulty));
    }
}