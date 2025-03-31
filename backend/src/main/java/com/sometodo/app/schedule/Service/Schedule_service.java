package com.sometodo.app.schedule.Service;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.repository.AccountRepository;
import com.sometodo.app.schedule.DTO.MySchedule_D;
import com.sometodo.app.schedule.ENTITY.MySchedule_E;
import com.sometodo.app.schedule.REPO.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class Schedule_service {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ScheduleRepo repo;

    int SUCCESS_CODE = 200;


    //GET - By YearMonthDay
//    public ArrayList<MySchedule_D> getByYearMonthDay(int year,  int month,  int day){
//        ArrayList<MySchedule_E> list = repo.findByYearMonthDay(year,month,day);
//        ArrayList<MySchedule_D> dataList = new ArrayList<>();
//
//        ArrayList<MySchedule_D> sendList = convertToDtoList(list,dataList);
//        return sendList;
//    }

    public ArrayList<MySchedule_D> getByYearMonthDay(String email, int year,  int month,  int day){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        ArrayList<MySchedule_E> list = repo.findByAccountAndYearMonthDay(account.getId(),year,month,day);
        ArrayList<MySchedule_D> dataList = new ArrayList<>();

        ArrayList<MySchedule_D> sendList = convertToDtoList(list,dataList);
        return sendList;
    }


    //GET - By YearMonth
//    public ArrayList<MySchedule_D> getByYearMonth(int year,  int month){
//        ArrayList<MySchedule_E> list = repo.findByYearMonth(year, month);
//        ArrayList<MySchedule_D> list2 = new ArrayList<>();
//
//        ArrayList<MySchedule_D> sendList = convertToDtoList(list,list2);
//        return sendList;
//    }

    public ArrayList<MySchedule_D> getByYearMonth(String email, int year,  int month){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        ArrayList<MySchedule_E> list = repo.findByAccountAndYearMonth(account.getId(),year,month);
        ArrayList<MySchedule_D> list2 = new ArrayList<>();

        ArrayList<MySchedule_D> sendList = convertToDtoList(list,list2);
        return sendList;
    }


    //GET - By YearMonthCategory
//    public ArrayList<MySchedule_D> getByYearMonthCategory(int year,  int month,  String category){
//        ArrayList<MySchedule_E> list = repo.findByYearMonthCategory(year, month,category);
//        ArrayList<MySchedule_D> list2 = new ArrayList<>();
//
//        ArrayList<MySchedule_D> sendList = convertToDtoList(list,list2);
//        return sendList;
//    }

    public ArrayList<MySchedule_D> getByYearMonthCategory(String email, int year,  int month,  String category){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        ArrayList<MySchedule_E> list = repo.findByAccountAndYearMonthCategory(account.getId(),year,month,category);
        ArrayList<MySchedule_D> list2 = new ArrayList<>();

        ArrayList<MySchedule_D> sendList = convertToDtoList(list,list2);
        return sendList;
    }

    public void editIsAchievement(String email, Long id) {
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MySchedule_E entity = repo.findById(id).orElse(null);
        entity.setSchedule_isAchievement(true);
        repo.save(entity);
    }

    //POST
//    public MySchedule_E createData(MySchedule_D dto){
//        MySchedule_E entity = MySchedule_E.dtoToEntity(dto);
//        MySchedule_E saved = repo.save(entity);
//        return saved;
//    }

    public MySchedule_E createData(String email, MySchedule_D dto){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MySchedule_E entity = MySchedule_E.dtoToEntity(dto, account);
        MySchedule_E saved = repo.save(entity);
        return saved;
    }


    //PATCH
//    public int editData(Long id, MySchedule_D schedule){
//        MySchedule_E target = repo.findById(id).orElse(null);
//        target.setCategory(schedule.getCategory());
//        target.setContent(schedule.getContent());
//        target.setSchedule_isAchievement(schedule.isSchedule_isAchievement());
//        repo.save(target);
//        return SUCCESS_CODE;
//    }

    public void editData(String email, Long id, MySchedule_D schedule){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MySchedule_E target = repo.findById(id).orElse(null);

        if (target == null || !Objects.equals(target.getAccount().getId(), account.getId()))
            throw new IllegalArgumentException("계정과 작성자가 다릅니다.");

        target.setCategory(schedule.getCategory());
        target.setContent(schedule.getContent());
        target.setSchedule_isAchievement(schedule.isSchedule_isAchievement());
        repo.save(target);
    }

    //DELETE
//    public int deleteData(Long id){
//        repo.deleteById(id);
//        return SUCCESS_CODE;
//    }

    public void deleteData(String email, Long id){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MySchedule_E target = repo.findById(id).orElse(null);

        if (target == null || !Objects.equals(target.getAccount().getId(), account.getId()))
            throw new IllegalArgumentException("계정과 작성자가 다릅니다.");

        repo.deleteByIdAndAccountId(id, account.getId());
    }


    //User Defined Function
    public ArrayList<MySchedule_D> convertToDtoList(ArrayList<MySchedule_E> list, ArrayList<MySchedule_D> list2){
        for (MySchedule_E myScheduleE : list) {
            MySchedule_D schedule = new MySchedule_D(myScheduleE.getSchedule_id(),
                    myScheduleE.getContent(),
                    myScheduleE.getCategory(),
                    myScheduleE.getYear(),
                    myScheduleE.getMonth(),
                    myScheduleE.getDay(),
                    myScheduleE.isSchedule_isAchievement());
            list2.add(schedule);
        }
        return list2;
    }

}
