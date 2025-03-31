package com.sometodo.app.schedule.Controller;

import com.sometodo.app.auth.security.UserPrincipal;
import com.sometodo.app.schedule.DTO.MyToDo_D;
import com.sometodo.app.schedule.REPO.ToDoRepo;
import com.sometodo.app.schedule.Service.ToDo_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RestController_ToDo {


    @Autowired
    ToDo_Service todo_service;
    @Autowired
    ToDoRepo repo;

    //GET
//    @GetMapping("/rest/scheduleApp/todo/get")
//    ArrayList<MyToDo_D> getData(){
//        ArrayList<MyToDo_D> list = todo_service.getData();
//        return list;
//    }

    @GetMapping("/rest/scheduleApp/todo/get")
    public ResponseEntity<?> getData(@AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            ArrayList<MyToDo_D> list = todo_service.getData(email);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //POST
//    @PostMapping("/rest/scheduleApp/todo/create")
//    ArrayList<MyToDo_D> createData(@RequestBody MyToDo_D dto){
//        ArrayList<MyToDo_D> list = todo_service.createData(dto);
//        return list;
//    }

    @PostMapping("/rest/scheduleApp/todo/create")
    public ResponseEntity<?> createData(@RequestBody MyToDo_D dto,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            ArrayList<MyToDo_D> list = todo_service.createData(email, dto);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //PATCH
//    @PatchMapping("/rest/scheduleApp/todo/edit/{id}")
//    int editData(@PathVariable Long id){
//        return todo_service.editData(id);
//    }

    @PatchMapping("/rest/scheduleApp/todo/edit/{id}")
    public ResponseEntity<?> editData(@PathVariable Long id,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            todo_service.editData(email, id);
            return ResponseEntity.ok(200);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE
//    @DeleteMapping("/rest/scheduleApp/todo/delete/{id}")
//    int deleteData(@PathVariable Long id){
//        return todo_service.deleteData(id);
//    }

    @DeleteMapping("/rest/scheduleApp/todo/delete/{id}")
    public ResponseEntity<?> deleteData(@PathVariable Long id,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            todo_service.deleteData(email, id);
            return ResponseEntity.ok(200);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
