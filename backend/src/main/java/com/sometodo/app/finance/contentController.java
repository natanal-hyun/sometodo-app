package com.sometodo.app.finance;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.repository.AccountRepository;
import com.sometodo.app.auth.security.UserPrincipal;
import com.sometodo.app.finance.Categories.Categories;
import com.sometodo.app.finance.MoneyFlow.MoneyFlow;
import com.sometodo.app.finance.MoneyFlow.MoneyFlowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sometodo.app.finance.MoneyFlow.MoneyFlowDTO.createMoneyFlowDTO;

@RestController
class contentController {
    @Autowired
    MoneyFlowRepo moneyFlowRepo;
    @Autowired
    CategoriesRepo categoriesRepo;

    @Autowired
    AccountRepository accountRepository;

    //http://localhost:8080/money/2025-02-26/contents 되는것 확인
//    @GetMapping("/money/{date}/contents")
//    public List<MoneyFlow> getByDate(@PathVariable("date") String date) {
//        System.out.println(date);
//        LocalDate localDate = LocalDate.parse(date);
//        return moneyFlowRepo.findByNowDate(localDate);
//    }

    @GetMapping("/money/{date}/contents")
    public List<MoneyFlow> getByDate(@PathVariable("date") String date,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) return null;
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        String email = userPrincipal.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return null;

        System.out.println(date);
        LocalDate localDate = LocalDate.parse(date);
        return moneyFlowRepo.findByAccountAndNowDate(account.getId(), localDate);
    }

//    @PostMapping("/money/{id}/contents")
//    public MoneyFlowDTO addMoneyFlow(@PathVariable("id") Long categoriesId,
//                                     @RequestBody MoneyFlowDTO dto) {
//        Categories category = categoriesRepo.findById(categoriesId)
//                .orElseThrow(() -> new IllegalArgumentException("가계부 등록 실패! " +
//                        "대상 카테고리 없습니다.")); //카테고리 아이디 갖고 카테고리 참조자 갖고옴, 카테고리 입력 안됐을때 처리
//        System.out.println(dto.isSpend()+""); //왜 true를 넣었는데 false값이 나오는지 ????
//        MoneyFlow moneyFlow = MoneyFlow.createMoneyFlow(dto, category); // dto->Entity
//        MoneyFlow created = moneyFlowRepo.save(moneyFlow); //repo에 저장 하면서 동시에 Entity만듬
//        MoneyFlowDTO createdDto = createMoneyFlowDTO(created); // Entity->dto
//        return createdDto;
//    }

    @PostMapping("/money/{id}/contents")
    public MoneyFlowDTO addMoneyFlow(@PathVariable("id") Long categoriesId,
                                     @RequestBody MoneyFlowDTO dto,
                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) return null;
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        String email = userPrincipal.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return null;

        Categories category = categoriesRepo.findById(categoriesId)
                .orElseThrow(() -> new IllegalArgumentException("가계부 등록 실패! " +
                        "대상 카테고리 없습니다.")); //카테고리 아이디 갖고 카테고리 참조자 갖고옴, 카테고리 입력 안됐을때 처리
        System.out.println(dto.isSpend()+""); //왜 true를 넣었는데 false값이 나오는지 ????
        MoneyFlow moneyFlow = MoneyFlow.createMoneyFlow(dto, category, account); // dto->Entity
        MoneyFlow created = moneyFlowRepo.save(moneyFlow); //repo에 저장 하면서 동시에 Entity만듬
        MoneyFlowDTO createdDto = createMoneyFlowDTO(created); // Entity->dto
        return createdDto;
    }

//    @PatchMapping("/money/{id}/contents")
//    public ResponseEntity<MoneyFlowDTO> update(@PathVariable Long id,
//                                               @RequestBody MoneyFlowDTO dto){
//        MoneyFlow target=moneyFlowRepo.findById(id) //타겟은 레포에서 꺼낸 참조자
//                .orElseThrow(()->new IllegalArgumentException("가계부 수정 실패! "+
//                        "대상 가계부가 없습니다."));
//        Categories updateCategory= categoriesRepo.findById(dto.getCategoriesId()) //카테고리 아이디로 참조자를 찾아옴
//                        .orElseThrow(()->new IllegalArgumentException("Category ID: "+dto.getCategoriesId()+"를 찾을 수 없습니다."));
//                                target.setCategory(updateCategory); //참조자를 MoneyFlow의 속성에 할당
//
//        target.patch(dto);
//        MoneyFlow updated= moneyFlowRepo.save(target);
//        MoneyFlowDTO updatedDto= createMoneyFlowDTO(updated);
//
//        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
//    }

    @PatchMapping("/money/{id}/contents")
    public ResponseEntity<MoneyFlowDTO> update(@PathVariable Long id,
                                               @RequestBody MoneyFlowDTO dto,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) return null;
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        String email = userPrincipal.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return null;

        MoneyFlow target=moneyFlowRepo.findById(id) //타겟은 레포에서 꺼낸 참조자
                .orElseThrow(()->new IllegalArgumentException("가계부 수정 실패! "+
                        "대상 가계부가 없습니다."));

        if (target == null || !Objects.equals(target.getAccount().getId(), account.getId())) return null;

        Categories updateCategory= categoriesRepo.findById(dto.getCategoriesId()) //카테고리 아이디로 참조자를 찾아옴
                .orElseThrow(()->new IllegalArgumentException("Category ID: "+dto.getCategoriesId()+"를 찾을 수 없습니다."));
        target.setCategory(updateCategory); //참조자를 MoneyFlow의 속성에 할당

        target.patch(dto);
        MoneyFlow updated= moneyFlowRepo.save(target);
        MoneyFlowDTO updatedDto= createMoneyFlowDTO(updated);

        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

//    @DeleteMapping("/money/{id}/contents")
//    public ResponseEntity<MoneyFlowDTO> delete(@PathVariable Long id){
//        MoneyFlow target=moneyFlowRepo.findById(id)
//                .orElseThrow(()-> new IllegalArgumentException("가계부 삭제 실패! "+
//                "대상이 없습니다."));
//        moneyFlowRepo.delete(target);
//        MoneyFlowDTO deletedDto= createMoneyFlowDTO(target);
//        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
//    }

    @DeleteMapping("/money/{id}/contents")
    public ResponseEntity<MoneyFlowDTO> delete(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) return null;
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        String email = userPrincipal.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return null;

        MoneyFlow target=moneyFlowRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("가계부 삭제 실패! "+
                        "대상이 없습니다."));

        if (target == null || !Objects.equals(target.getAccount().getId(), account.getId())) return null;

        moneyFlowRepo.delete(target);
        MoneyFlowDTO deletedDto= createMoneyFlowDTO(target);
        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
    }

//    @GetMapping("/getMonthPay/{categoryId}/{year}/{month}")
//    public List<MoneyFlowDTO> getByMonth(@PathVariable("year") int year,
//                                      @PathVariable("month")int month,
//                                      @PathVariable("categoryId") Long categoryId) {
//        int i=categoryId.intValue();
//        System.out.println(year+"    "+month+"    "+i);
//        List<MoneyFlow> getMonthEntity=moneyFlowRepo.findByCategoryAndMonth(categoryId,year,month);
//        List<MoneyFlowDTO> getMonthDTO=new ArrayList<>();
//        for(MoneyFlow m:getMonthEntity){
//            MoneyFlowDTO dto=createMoneyFlowDTO(m);
//            getMonthDTO.add(dto);
//        }
//        return getMonthDTO;
//    }

    @GetMapping("/getMonthPay/{categoryId}/{year}/{month}")
    public List<MoneyFlowDTO> getByMonth(@PathVariable("year") int year,
                                         @PathVariable("month")int month,
                                         @PathVariable("categoryId") Long categoryId,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) return null;
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        String email = userPrincipal.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return null;

        int i=categoryId.intValue();
        System.out.println(year+"    "+month+"    "+i);
        List<MoneyFlow> getMonthEntity=moneyFlowRepo.findByCategoryAndMonthAndAccount(categoryId,year,month,account.getId());
        List<MoneyFlowDTO> getMonthDTO=new ArrayList<>();
        for(MoneyFlow m:getMonthEntity){
            MoneyFlowDTO dto=createMoneyFlowDTO(m);
            getMonthDTO.add(dto);
        }
        return getMonthDTO;
    }





}

interface MoneyFlowRepo extends JpaRepository<MoneyFlow, Long> {
    List<MoneyFlow> findByNowDate(LocalDate nowDate);

    @Query(value = "SELECT * FROM money_flow WHERE account_id = :accountId AND now_date = :nowDate", nativeQuery = true)
    List<MoneyFlow> findByAccountAndNowDate(@Param("accountId") Long accountId,
                                              @Param("nowDate") LocalDate nowDate);

//    @Query(value = "SELECT TO_CHAR(DATE_TRUNC('MONTH', m.now_date), 'YYYY-MM') AS month_start, m.categories_id, SUM(m.cost) AS total_cost " +
//            "FROM MONEY_FLOW m " +
//            "WHERE YEAR(m.now_date) = :year " +
//            "AND MONTH(m.now_date) = :month " +
//            "AND m.spend = true " +
//            "GROUP BY TO_CHAR(DATE_TRUNC('MONTH', m.now_date), 'YYYY-MM'), m.categories_id " +
//            "ORDER BY month_start DESC", nativeQuery = true)
//    List<Object[]>getMonthlyCostByCategory(int year,int month);

    @Query(value = "SELECT TO_CHAR(DATE_TRUNC('MONTH', m.now_date), 'YYYY-MM') AS month_start, m.categories_id, SUM(m.cost) AS total_cost " +
            "FROM MONEY_FLOW m " +
            "WHERE EXTRACT(YEAR FROM m.now_date) = :year " +
            "AND EXTRACT(MONTH FROM m.now_date) = :month " +
            "AND m.spend = true " +
            "GROUP BY TO_CHAR(DATE_TRUNC('MONTH', m.now_date), 'YYYY-MM'), m.categories_id " +
            "ORDER BY month_start DESC", nativeQuery = true)
    List<Object[]> getMonthlyCostByCategory(@Param("year") int year, @Param("month") int month);


//    @Query("SELECT m FROM MoneyFlow m " +
//            "WHERE m.category.id = :categoryId " +
//            "AND YEAR(m.nowDate) = :year " +
//            "AND MONTH(m.nowDate) = :month " +
//            "AND m.spend = true")
//    List<MoneyFlow> findByCategoryAndMonth(@Param("categoryId") Long categoryId,
//                                           @Param("year") int year,
//                                           @Param("month") int month);

    @Query(value = "SELECT * FROM money_flow m " +
            "WHERE m.categories_id = :categoryId " +
            "AND EXTRACT(YEAR FROM m.now_date) = :year " +
            "AND EXTRACT(MONTH FROM m.now_date) = :month " +
            "AND m.spend = true", nativeQuery = true)
    List<MoneyFlow> findByCategoryAndMonth(@Param("categoryId") Long categoryId,
                                           @Param("year") int year,
                                           @Param("month") int month);

    @Query(value = "SELECT * FROM money_flow m " +
            "WHERE m.categories_id = :categoryId " +
            "AND EXTRACT(YEAR FROM m.now_date) = :year " +
            "AND EXTRACT(MONTH FROM m.now_date) = :month " +
            "AND m.spend = true " +
            "AND m.account_id = :accountId", nativeQuery = true)
    List<MoneyFlow> findByCategoryAndMonthAndAccount(@Param("categoryId") Long categoryId,
                                                     @Param("year") int year,
                                                     @Param("month") int month,
                                                     @Param("accountId") Long accountId);
}

interface CategoriesRepo extends JpaRepository<Categories, Long> {


}
