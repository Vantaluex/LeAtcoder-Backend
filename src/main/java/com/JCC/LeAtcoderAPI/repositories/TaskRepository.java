package com.JCC.LeAtcoderAPI.repositories;

import com.JCC.LeAtcoderAPI.Model.Task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    Task findByTaskId(String Id);

    @Query(value = "{ score: { $gte: ?0, $lte: ?1 } }", count = true)
    int countByScoreBetween(int min, int max);

    Page<Task> findByScoreBetween(int min, int max, Pageable pageable);
}
