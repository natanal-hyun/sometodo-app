package com.example.todolist.schedule.Service;

import com.example.todolist.schedule.Class.ScheduleClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScheduleService {

    //GET
    @GET("/rest/scheduleApp/schedule/getByYM/{year}/{month}")
    Call<ArrayList<ScheduleClass>> getDataListByYM(@Path("year")int year, @Path("month")int month);
    @GET("/rest/scheduleApp/schedule/getByYMC/{year}/{month}/{category}")
    Call<ArrayList<ScheduleClass>> getDataListByYMC(@Path("year")int year, @Path("month")int month, @Path("category")String category);
    @GET("/rest/scheduleApp/schedule/getByYMD/{year}/{month}/{day}")
    Call<ArrayList<ScheduleClass>> getDataListByYMD(@Path("year")int year, @Path("month")int month, @Path("day")int day);  //MainActivity - displayDate()


    //CREATE
    @POST("/rest/scheduleApp/schedule/create")
    Call<ScheduleClass> createSchedule(@Body ScheduleClass schedule);


    //EDIT
    @PATCH("/rest/scheduleApp/schedule/edit/{id}")
    Call<Integer> editSchedule(@Path("id")Long id,@Body ScheduleClass schedule);
    @PATCH("/rest/scheduleApp/schedule/edit/isAchievement/{id}")
    Call<Integer> editIsAchievement(@Path("id")Long id);


    //DELETE
    @DELETE("/rest/scheduleApp/schedule/delete/{id}")
    Call<Integer> deleteSchedule(@Path("id")Long id);

}
