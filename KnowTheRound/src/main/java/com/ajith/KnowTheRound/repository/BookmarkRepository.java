package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.Bookmark;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserAndInterviewExperience(User user,
                                                        InterviewExperience interviewExperience);

    List<Bookmark> findByUser(User user);

    boolean existsByUserAndInterviewExperience(User user,
                                               InterviewExperience interviewExperience);

    void deleteByUserAndInterviewExperience(User user,
                                            InterviewExperience interviewExperience);


}