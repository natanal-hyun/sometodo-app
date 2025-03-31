package com.sometodo.app.schedule.REPO;

import com.sometodo.app.schedule.ENTITY.MyToDo_E;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ToDoRepo extends JpaRepository<MyToDo_E,Long> {
    @Query(value = "SELECT * FROM my_to_do_e WHERE account_id = :accountId", nativeQuery = true)
    ArrayList<MyToDo_E> findByAccount(@Param("accountId") Long accountId);
}
