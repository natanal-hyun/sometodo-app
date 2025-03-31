package com.sometodo.app.finance.MoneyFlow;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.finance.Categories.Categories;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
public class MoneyFlow {
    @Column(name="now_date")
    private LocalDate nowDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categories_id") //카테고리 아이디를 객체생성할때 넣으면 카테고리에 참조자를 만든다.
    private Categories category;
    @Column
    private String content;
    @Column
    private int cost;
    @Column
    private boolean spend;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public static MoneyFlow createMoneyFlow(MoneyFlowDTO dto, Categories category, Account account) {
        if (dto.getId() != null)
            throw new IllegalArgumentException("지출/소득 생성 실패! 아이디가 없어야합니다."); //moneyflow의 아이디는 지정이 아닌 자동 할당이다.
        if (dto.getCategoriesId() != category.getId())
            throw new IllegalArgumentException("지출/소득 생성 실패! 카테고리의 id가 잘못됐습니다.");//클라이언트가 요청한 path의 ID값과 body의 카테고리Id가 일치하지 않을때

        return new MoneyFlow(
                dto.getNowDate(),
                dto.getId(),
                category,
                dto.getContent(),
                dto.getCost(),
                dto.isSpend(),
                account
        );
    }

    public void patch(MoneyFlowDTO dto) {
        if (this.id != dto.getId())  // 탈렌드 조회시 패쓰값과 동일하게 아이디 줘야함
            throw new IllegalArgumentException("가계부 수정 실패! 잘못된 id가 입력됐습니다.");

        if (dto.getContent() != null)
            this.content = dto.getContent();
        if(dto.getNowDate()!=null)
            this.nowDate=dto.getNowDate();
        this.cost = dto.getCost();
        this.spend = dto.isSpend();

    }


}


