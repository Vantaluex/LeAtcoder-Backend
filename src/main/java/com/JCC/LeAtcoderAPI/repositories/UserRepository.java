package com.JCC.LeAtcoderAPI.repositories;

import com.JCC.LeAtcoderAPI.Model.Completed;
import com.JCC.LeAtcoderAPI.Model.Note;
import com.JCC.LeAtcoderAPI.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{'_id': ?0}")
    @Update("{'push'}: {'completedList': ?1}")
    void addCompletedToUser(String userId, Completed completed);

    @Query(value = "{'_id': ?0}", fields = "{'completedList': 1, '_id': 0}")
    Page<Completed> getAllCompleted(String userId);

    // This method will correctly map the result to an Optional<Note>
    @Aggregation(pipeline = {
            "{'$match': {'_id': ?0}}",             // Stage 1: Filter by user ID
            "{'$unwind': '$noteList'}",            // Stage 2: Deconstruct the 'noteList' array
            "{'$match': {'noteList.taskId': ?1}}", // Stage 3: Filter the unwound documents by taskId
            "{'$replaceRoot': {'newRoot': '$noteList'}}" // Stage 4: Promote the matching 'Note' object to the root level
    })
    Optional<Note> getNoteByIds(String userId, String taskId);

    @Query("{'_id': ?0, 'noteList.taskId': {'$ne': ?1}}")
    @Update("{'$push': {'noteList': ?2}}")
    void addNewNote(String userId, String taskId, Note note);

    @Query("{'_id': ?0, 'noteList.taskId': ?1}")
    @Update("{'$set': {'noteList.$.note': ?2, 'noteList.$.code': ?3}}")
    void updateNoteFields(String userId, String taskId, String noteText, String code);

    @Query("{'_id': ?0}")
    @Update("{'$pull': {'noteList': {'taskId': ?1}}}")
    void removeNote(String userId, String taskId);

}
