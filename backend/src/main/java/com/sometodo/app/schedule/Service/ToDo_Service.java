package com.sometodo.app.schedule.Service;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.repository.AccountRepository;
import com.sometodo.app.schedule.DTO.MyToDo_D;
import com.sometodo.app.schedule.ENTITY.MyToDo_E;
import com.sometodo.app.schedule.REPO.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ToDo_Service {

    int SUCCESS_CODE = 200;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ToDoRepo repo;

//    public ArrayList<MyToDo_D> getData(){
//        ArrayList<MyToDo_D> sendList = EntityToDtoList();
//        return sendList;
//    }

    public ArrayList<MyToDo_D> getData(String email){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        ArrayList<MyToDo_E> sendList = repo.findByAccount(account.getId());
        return EntityToDtoList2(sendList);
    }

//    public ArrayList<MyToDo_D> createData(MyToDo_D dto){
//        MyToDo_E data = new MyToDo_E(dto.getTodo_id(), dto.getTodo_content(), dto.isTodo_isAchievement(), dto.getImportance());
//        repo.save(data);
//        ArrayList<MyToDo_D> sendList = EntityToDtoList();
//        return sendList;
//    }

    public ArrayList<MyToDo_D> createData(String email, MyToDo_D dto){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MyToDo_E data = new MyToDo_E(dto.getTodo_id(), dto.getTodo_content(), dto.isTodo_isAchievement(), dto.getImportance(), account);
        repo.save(data);

        ArrayList<MyToDo_E> sendList = repo.findByAccount(account.getId());
        return EntityToDtoList2(sendList);
    }

//    public int editData(Long todo_id){
//        MyToDo_E target = repo.findById(todo_id).orElse(null);
//        target.setTodo_isAchievement(true);
//        repo.save(target);
//        return SUCCESS_CODE;
//    }

    public void editData(String email, Long todo_id){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MyToDo_E target = repo.findById(todo_id).orElse(null);

        if (target == null || !Objects.equals(target.getAccount().getId(), account.getId()))
            throw new IllegalArgumentException("계정과 작성자가 다릅니다.");

        target.setTodo_isAchievement(true);
        repo.save(target);
    }


//    public int deleteData(Long id){
//        repo.deleteById(id);
//        return SUCCESS_CODE;
//    }

    public void deleteData(String email, Long id){
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        MyToDo_E target = repo.findById(id).orElse(null);

        if (target == null || !Objects.equals(target.getAccount().getId(), account.getId()))
            throw new IllegalArgumentException("계정과 작성자가 다릅니다.");

        repo.deleteById(id);
    }


    //User-defined function
    public ArrayList<MyToDo_D> EntityToDtoList(){
        ArrayList<MyToDo_E> list = (ArrayList<MyToDo_E>) repo.findAll();
        ArrayList<MyToDo_D> send_list = new ArrayList<>();
        for (MyToDo_E myToDoE : list) {
            MyToDo_D data = new MyToDo_D(myToDoE.getTodo_content(),
                    myToDoE.isTodo_isAchievement(),
                    myToDoE.getImportance(),
                    myToDoE.getTodo_id());
            send_list.add(data);

        }
        return send_list;
    }

    public ArrayList<MyToDo_D> EntityToDtoList2(ArrayList<MyToDo_E> list){
        ArrayList<MyToDo_D> send_list = new ArrayList<>();
        for (MyToDo_E myToDoE : list) {
            MyToDo_D data = new MyToDo_D(myToDoE.getTodo_content(),
                    myToDoE.isTodo_isAchievement(),
                    myToDoE.getImportance(),
                    myToDoE.getTodo_id());
            send_list.add(data);
        }
        return send_list;
    }


}
