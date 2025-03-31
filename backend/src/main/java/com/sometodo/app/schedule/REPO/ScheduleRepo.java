package com.sometodo.app.schedule.REPO;

import com.sometodo.app.schedule.ENTITY.MySchedule_E;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ScheduleRepo extends JpaRepository<MySchedule_E,Long> {

    @Query(value = "SELECT * FROM my_schedule_e WHERE " +
            "month = :month " +
            "AND year = :year " +
            "AND day= :day",
           nativeQuery = true)
    ArrayList<MySchedule_E> findByYearMonthDay(@Param("year") int year,
                                               @Param("month") int month,
                                               @Param("day") int day);


    @Query(value = "SELECT * FROM my_schedule_e WHERE " +
            "month = :month " +
            "AND year = :year",
            nativeQuery = true)
    ArrayList<MySchedule_E> findByYearMonth(@Param("year") int year,
                                            @Param("month") int month);

    @Query(value = "SELECT * FROM my_schedule_e WHERE " +
            "month = :month " +
            "AND year = :year "+
            "AND category = :category",
            nativeQuery = true)
    ArrayList<MySchedule_E> findByYearMonthCategory(@Param("year") int year,
                                                    @Param("month") int month,
                                                    @Param("category") String category);

    @Query(value = "SELECT * FROM my_schedule_e WHERE account_id = :accountId AND year = :year AND month = :month AND day = :day", nativeQuery = true)
    ArrayList<MySchedule_E> findByAccountAndYearMonthDay(@Param("accountId") Long accountId,
                                                                @Param("year") int year,
                                                                @Param("month") int month,
                                                                @Param("day") int day);

    @Query(value = "SELECT * FROM my_schedule_e WHERE account_id = :accountId AND " +
            "month = :month " +
            "AND year = :year",
            nativeQuery = true)
    ArrayList<MySchedule_E> findByAccountAndYearMonth(@Param("accountId") Long accountId,
                                                      @Param("year") int year,
                                                      @Param("month") int month);

    @Query(value = "SELECT * FROM my_schedule_e WHERE account_id = :accountId AND " +
            "month = :month " +
            "AND year = :year "+
            "AND category = :category",
            nativeQuery = true)
    ArrayList<MySchedule_E> findByAccountAndYearMonthCategory(@Param("accountId") Long accountId,
                                                              @Param("year") int year,
                                                              @Param("month") int month,
                                                              @Param("category") String category);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM my_schedule_e WHERE schedule_id = :id AND account_id = :accountId", nativeQuery = true)
    int deleteByIdAndAccountId(@Param("id") Long id, @Param("accountId") Long accountId);
}
