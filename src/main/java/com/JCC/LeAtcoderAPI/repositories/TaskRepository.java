package com.JCC.LeAtcoderAPI.repositories;

import com.JCC.LeAtcoderAPI.Model.Task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    Task findByid(String id);

    @Query(value = "{ 'score': { '$gte': ?0, '$lte': ?1 } }", count = true)
    int countByScoreBetween(int min, int max);

    Page<Task> findByScoreBetween(int min, int max, Pageable pageable);

    Page<Task> findByIdInAndScoreBetween(List<String> ids, int min, int max, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { 'score': { '$gte': ?0, '$lte': ?1 }, 'id': { '$nin': ?2 } } }",
            "{ $sort: { 'id': 1 } }",
            "{ $skip: ?3 }",
            "{ $limit: ?4 }"
    })
    List<Task> findUncompletedTasks(int min, int max, List<String> completedIds, long skip, int limit);

}
