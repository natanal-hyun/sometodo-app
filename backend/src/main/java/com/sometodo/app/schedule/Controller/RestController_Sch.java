package com.sometodo.app.schedule.Controller;

import com.sometodo.app.auth.security.UserPrincipal;
import com.sometodo.app.schedule.DTO.MySchedule_D;
import com.sometodo.app.schedule.ENTITY.MySchedule_E;
import com.sometodo.app.schedule.REPO.ScheduleRepo;
import com.sometodo.app.schedule.Service.Schedule_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RestController_Sch {

    @Autowired
    Schedule_service schedule_service;
    @Autowired
    ScheduleRepo repo;


    //GET - By YearMonthDay
//    @GetMapping("/rest/scheduleApp/schedule/getByYMD/{year}/{month}/{day}")
//    ArrayList<MySchedule_D> getByYearMonthDay(@PathVariable int year, @PathVariable int month, @PathVariable int day){
//        ArrayList<MySchedule_D> dataList = schedule_service.getByYearMonthDay(year, month, day);
//        return dataList;
//    }

    @GetMapping("/rest/scheduleApp/schedule/getByYMD/{year}/{month}/{day}")
    public ResponseEntity<?> getByYearMonthDay(@PathVariable int year, @PathVariable int month, @PathVariable int day,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            ArrayList<MySchedule_D> dataList = schedule_service.getByYearMonthDay(email, year, month, day);
            return ResponseEntity.ok(dataList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //GET - By YearMonth
//    @GetMapping("/rest/scheduleApp/schedule/getByYM/{year}/{month}")
//    ArrayList<MySchedule_D> getByYearMonth(@PathVariable int year, @PathVariable int month){
//        ArrayList<MySchedule_D> dataList = schedule_service.getByYearMonth(year,month);
//        return dataList;
//    }

    @GetMapping("/rest/scheduleApp/schedule/getByYM/{year}/{month}")
    public ResponseEntity<?> getByYearMonth(@PathVariable int year, @PathVariable int month,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            ArrayList<MySchedule_D> dataList = schedule_service.getByYearMonth(email, year, month);
            return ResponseEntity.ok(dataList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET - By YearMonthCategory
//    @GetMapping("rest/scheduleApp/schedule/getByYMC/{year}/{month}/{category}")
//    ArrayList<MySchedule_D> getByYearMonthCategory(@PathVariable int year, @PathVariable int month, @PathVariable String category){
//        ArrayList<MySchedule_D> dataList = schedule_service.getByYearMonthCategory(year, month, category);
//        return dataList;
//    }

    @GetMapping("rest/scheduleApp/schedule/getByYMC/{year}/{month}/{category}")
    public ResponseEntity<?> getByYearMonthCategory(@PathVariable int year, @PathVariable int month, @PathVariable String category,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            ArrayList<MySchedule_D> dataList = schedule_service.getByYearMonthCategory(email, year, month, category);
            return ResponseEntity.ok(dataList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //POST
//    @PostMapping("/rest/scheduleApp/schedule/create")
//    MySchedule_E createData(@RequestBody MySchedule_D dto){
//        MySchedule_E saved = schedule_service.createData(dto);
//        return saved;
//    }

    @PostMapping("/rest/scheduleApp/schedule/create")
    public ResponseEntity<?> createData(@RequestBody MySchedule_D dto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            MySchedule_E saved = schedule_service.createData(email, dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //PATCH
//    @PatchMapping("/rest/scheduleApp/schedule/edit/{id}")
//    int editData(@PathVariable Long id,
//                  @RequestBody MySchedule_D schedule){
//        return schedule_service.editData(id,schedule);
//    }

    @PatchMapping("/rest/scheduleApp/schedule/edit/{id}")
    public ResponseEntity<?> editData(@PathVariable Long id, @RequestBody MySchedule_D schedule,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            schedule_service.editData(email, id, schedule);
            return ResponseEntity.ok(200);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PatchMapping("/rest/scheduleApp/schedule/edit/isAchievement/{id}")
//    int editIsAchievement(@PathVariable Long id){
//        MySchedule_E entity = repo.findById(id).orElse(null);
//        entity.setSchedule_isAchievement(true);
//        repo.save(entity);
//        return 200;
//    }

    @PatchMapping("/rest/scheduleApp/schedule/edit/isAchievement/{id}")
    public ResponseEntity<?> editIsAchievement(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            schedule_service.editIsAchievement(email, id);
            return ResponseEntity.ok(200);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE
//    @DeleteMapping("/rest/scheduleApp/schedule/delete/{id}")
//    int deleteData(@PathVariable Long id){
//        return schedule_service.deleteData(id);
//
//    }

    @DeleteMapping("/rest/scheduleApp/schedule/delete/{id}")
    public ResponseEntity<?> deleteData(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            schedule_service.deleteData(email, id);
            return ResponseEntity.ok(200);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
