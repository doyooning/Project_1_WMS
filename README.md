# WMS 입출고 관리 시스템

## 📌 프로젝트 개요

WMS(Warehouse Management System)의 **입출고 관리 모듈**을 담당하여 설계 및 구현하였습니다. 본 모듈은 일반회원의 입출고 요청부터 총관리자의 승인, 그리고 재고 반영까지의 전체 프로세스를 관리하는 핵심 기능입니다.

### 담당 역할

- **입출고 관리 전체 설계 및 구현**
- ERD 설계 (Inbound, Outbound, InboundItem, OutboundItem 테이블)
- 역할 기반 권한 분리 (일반회원/총관리자)
- 입출고 요청 생애주기 관리 (등록 → 승인/취소 → 재고 반영)

### 기술 스택

- **Language**: Java 17
- **Database**: MySQL 8.0
- **Architecture**: MVC Pattern, Singleton Pattern
- **Library**: Lombok, mysql-connector-java

---

## 🗂️ ERD 설계

### 핵심 테이블 구조

#### 1. Inbound (입고 요청)

```sql
CREATE TABLE Inbound (
    inRequestIdx INT PRIMARY KEY AUTO_INCREMENT,
    uIdx INT NOT NULL,               -- 요청한 일반회원 FK
    wIdx INT NOT NULL,                    -- 입고 창고 FK
    inDueDate DATE,             -- 입고 기한
    requestStatus ENUM('PENDING', 'APPROVED', 'CANCELLED'),
    inRequestDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    inboundDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uIdx) REFERENCES User(uIdx),
    FOREIGN KEY (wIdx) REFERENCES Warehouse(wIdx)
);
```

#### 2. InboundItem (입고 물품 상세)

```sql
CREATE TABLE InboundItem (
    inItemIdx INT PRIMARY KEY AUTO_INCREMENT,
    inRequestIdx INT NOT NULL,             -- 입고 요청 FK
    pIdx INT NOT NULL,                    -- 상품 FK
    wsIdx INT NOT NULL,                    -- 창고 섹션 FK
    wIdx INT NOT NULL,                    -- 창고 FK
    pQuantity INT NOT NULL,             -- 입고 수량
    FOREIGN KEY (inRequestIdx) REFERENCES Inbound(inRequestIdx) ON DELETE CASCADE,
    FOREIGN KEY (pIdx) REFERENCES Product(pIdx),
    FOREIGN KEY (wIdx) REFERENCES Warehouse(wIdx),
    FOREIGN KEY (wsIdx) REFERENCES Warehouse(wsIdx)
);
```

#### 3. Outbound (출고 요청)

```sql
CREATE TABLE Outbound (
    outRequestIdx INT PRIMARY KEY AUTO_INCREMENT,
    uIdx INT NOT NULL,               -- 요청한 일반회원 FK
    wIdx INT NOT NULL,                    -- 출고 창고 FK
    outDueDate DATE NOT NULL,             -- 출고 기한
    requestStatus ENUM('PENDING', 'APPROVED', 'CANCELLED'),
    outRequestDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    outboundDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uIdx) REFERENCES User(uIdx),
    FOREIGN KEY (wIdx) REFERENCES Warehouse(wIdx)
);
```

#### 4. OutboundItem (출고 물품 상세)

```sql
CREATE TABLE OutboundItem (
    outItemIdx INT PRIMARY KEY AUTO_INCREMENT,
    outRequestIdx INT NOT NULL,            -- 출고 요청 FK
    pIdx INT NOT NULL,                    -- 상품 FK
    wsIdx INT NOT            -- 창고 섹션 FK
    NULL,
    wIdx INT NOT NULL,                    -- 창고 FK
    pQuantity INT NOT NULL,             -- 출고 수량
    FOREIGN KEY (outRequestIdx) REFERENCES Outbound(outRequestIdx) ON DELETE CASCADE,
    FOREIGN KEY (pIdx) REFERENCES Product(pIdx),
    FOREIGN KEY (wIdx) REFERENCES Warehouse(wIdx),
    FOREIGN KEY (wsIdx) REFERENCES Warehouse(wsIdx)
);
```

### 설계 포인트

1. **코드 재사용성 극대화**

   - 비즈니스 로직이 유사한 입고-출고를 하나의 인터페이스에서 기능 설계 후, 각각의 구현체를 통해 실행
   - InOutboundController -> InboundControllerImpl, OutboundControllerImpl
   - InOutboundService -> InboundServiceImpl, OutboundServiceImpl
   - InOutboundDAO -> InboundDAO, OutboundDAO

2. **1:N 관계 설계**

   - 하나의 입출고 요청은 여러 물품을 포함할 수 있도록 설계
   - Inbound(1) : InboundItem(N), Outbound(1) : OutboundItem(N)

3. **반복 출력 데이터 ENUM 관리**

   - 결과 출력 및 상태값에 반복적으로 사용할 데이터를 ENUM 타입으로 관리
   - EntityStatus, Errors, Messages
   - 코드가 간결해지고, 오타에 의한 컴파일 에러 방지 가능

4. **CASCADE 삭제**

   - 입출고 요청 삭제 시 관련 물품 정보도 함께 삭제되도록 설정
   - 데이터 참조 무결성 보장

5. **저장 프로시저(Stored Procedure) 사용**

   - DB 내부에서 쿼리 로직 처리하도록 설정
   - 코드 간결화 및 재사용성 향상, 유지보수 용이

## 🎯 주요 기능 구현

### 1. 역할 기반 권한 분리

#### 일반회원 (User)

- ✅ 입출고 요청 등록
- ✅ 본인의 요청 조회 (승인된 요청만)
- ✅ 승인 대기 중인 요청 수정/취소
- ✅ 승인된 요청의 고지서 출력

#### 총관리자 (TotalAdmin)

- ✅ 모든 입출고 요청 승인/거절
- ✅ 미승인 요청 조회
- ✅ 기간별 입출고 내역 조회
- ✅ 입출고 고지서 출력

### 2. 출고 요청 등록 프로세스

사용자(User)로 로그인 후 출고 관리 메뉴에 접속합니다. <br>
첫 번째 실행에서는 요청 1건, 물품정보 1건을 만듭니다.

```java
============================================================
######################### 출고 요청  #########################
============================================================
요청 정보를 작성해주세요.
출고위치(창고번호) : 1
============================================================
출고기한(8자리 숫자로 입력) : 20251212
============================================================
물품 정보를 작성해주세요.
물품번호 : 12345678901
============================================================
물품개수 : 5
============================================================
물품 정보가 정상적으로 입력되었습니다.
============================================================
Q를 입력하면 메뉴 화면으로 이동하며,
물품 정보를 추가로 입력하려면 Q를 제외한 아무 키나 입력하십시오.
: a
```

Q 이외의 문자를 입력하면 같은 요청에 추가하여 물품정보 1건을 입력받습니다.

```java
============================================================
물품 정보를 작성해주세요.
물품번호 : 12312312312
============================================================
물품개수 : 2
============================================================
물품 정보가 정상적으로 입력되었습니다.
============================================================
Q를 입력하면 메뉴 화면으로 이동하며,
물품 정보를 추가로 입력하려면 Q를 제외한 아무 키나 입력하십시오.
: Q
============================================================
출고 요청이 등록되었습니다. 회원님의 요청 번호는 [1] 입니다.
============================================================
```

Q를 누르면 입력을 종료하고, 요청 번호를 알려줍니다.

### 3. 승인 프로세스 및 재고 반영

관리자(Admin)로 로그인 후 출고 관리 메뉴에 접속합니다. <br>

```java
============================================================
####################### 출고 요청 승인 #######################
============================================================
승인할 출고요청 번호를 입력해주세요.
요청번호 : 1
============================================================
해당 출고요청을 승인하시겠습니까?
(Y/N 입력) : Y
============================================================
요청번호 [1] 출고 요청 승인이 완료되었습니다.
============================================================
```

승인 완료시 저장 프로시저가 실행됩니다.

```sql
drop procedure if exists approveOutRequestStatus;
delimiter $$
create procedure approveOutRequestStatus(IN requestId int, OUT outRequestId int)
proc_block: begin
    declare insufficient int default 0;

    select count(*) into insufficient
    from OutboundItem oi
             join Outbound o on oi.outRequestIdx = o.outRequestIdx
             join Stock s on s.pIdx = oi.pIdx and s.wIdx = oi.wIdx and s.wsIdx = oi.wsIdx
    where o.outRequestIdx = requestId
      and o.requestStatus = 'PENDING'
      and s.sWhole < oi.pQuantity;

    if insufficient > 0 then
        set outRequestId = -2;
        leave proc_block;
    end if;

    update Outbound
    set requestStatus = 'APPROVED', outboundDate = now()
    where outRequestIdx = requestId and requestStatus = 'PENDING';

    set outRequestId = requestId;
end proc_block $$
delimiter ;
call approveOutRequestStatus(1, @outRequestId);
```

insufficient는 해당 물품의 총 재고(Stock.sWhole)보다 요청 물품 수량(OutboundItem.pQuantity)이 많은 경우를 세어, 하나의 경우라도 존재할 경우 프로시저는 -2를 반환합니다. <br>
정상적인 요청인 경우 요청 상태를 PENDING에서 APPROVED로 변경합니다.
상태가 변경되면, 재고량을 변경하는 트리거가 실행됩니다.

```sql
drop trigger if exists trg_outbound_stock_update;
delimiter $$
create trigger trg_outbound_stock_update
    after update on Outbound
    for each row
begin
    declare done int default 0;
    declare in_pIdx varchar(63);
    declare in_wsIdx int;
    declare in_wIdx int;
    declare in_quantity int;

    declare cur cursor for
        select pIdx, wsIdx, wIdx, pQuantity
        from OutboundItem
        where outRequestIdx = new.outRequestIdx;

    declare continue handler for not found set done = 1;

    if old.requestStatus = 'PENDING' and new.requestStatus = 'APPROVED' then

        open cur;
        read_loop: loop
            fetch cur into in_pIdx, in_wsIdx, in_wIdx, in_quantity;
            if done = 1 then
                leave read_loop;
            end if;

            update Stock s
                join OutboundItem oi
                on s.pIdx = oi.pIdx
                    and s.wIdx = oi.wIdx
                    and s.wsIdx = oi.wsIdx
            set s.sWhole = s.sWhole - oi.pQuantity
            where oi.outRequestIdx = new.outRequestIdx;

            select sSafe, sWhole into @safecount, @wholecount
            from Stock where pIdx = in_pIdx and wsIdx = in_wsIdx and wIdx = in_wIdx;

            if @wholecount = 0 then

                if @safecount = 0 then
                    set done = 1;
                    leave read_loop;
                else
                    update Stock
                    set sWhole = @wholecount + 1, sSafe = @safecount - 1
                    where pIdx = in_pIdx and wsIdx = in_wsIdx and wIdx = in_wIdx;
                end if;
            end if;
        end loop;
        close cur;
    end if;
end $$
delimiter ;
```

요청 상태가 PENDING에서 APPROVED로 변경될 경우, cursor를 사용하여 물품의 재고량을 수량만큼 차감합니다. <br>
만약 재고량이 0이 되면, 안전 재고(Stock.sSafe)에서 1을 차감하고 그만큼을 총 재고량에 더해줍니다. 더할 수 없는 경우에는 진행하지 않습니다.

**구현 특징:**

- 승인과 동시에 재고 자동 반영
- 안전 재고를 활용한 총 재고량 유지 구현

### 4. 요청 수정 기능

출고 요청 수정은 요청 정보(창고번호, 출고기한) 수정, 물품 정보 수정(물품번호, 물품개수)를 수정할 수 있습니다.

```java
============================================================
####################### 출고 요청 수정 #######################
============================================================
1. 요청 정보 수정 	    2. 물품 정보 수정           3. 뒤로가기
============================================================
메뉴를 고르세요 : 1
============================================================
수정할 출고요청 번호를 입력해주세요.
요청번호 : 1
============================================================
수정할 내용을 입력해주세요.
창고번호 : 7
============================================================
출고기한(8자리 숫자로 입력) : 20251220
============================================================
수정이 완료되었습니다.
============================================================
```

```java
============================================================
####################### 출고 요청 수정 #######################
============================================================
1. 요청 정보 수정 	    2. 물품 정보 수정           3. 뒤로가기
============================================================
메뉴를 고르세요 : 2
============================================================
수정할 출고요청 번호를 입력해주세요.
요청번호 : 1
============================================================
수정할 물품 순번을 입력해주세요.
순번 : 2
============================================================
물품 정보를 작성해주세요.
물품번호 : 12345678901
============================================================
물품개수 : 7
============================================================
수정이 완료되었습니다.
============================================================
```

**구현 특징:**

- PENDING 상태의 요청만 수정 가능하도록 제한(프로시저에서 검증)
- 요청 정보 수정 시 관련 물품의 창고번호 자동 업데이트
- 물품별 개별 수정 지원

### 5. 출고 고지서 출력

```java
============================================================
####################### 출고고지서 출력 #######################
============================================================
출력할 출고요청 번호를 입력해주세요.
요청번호 : 1
============================================================
해당 출고요청의 출고고지서를 출력하시겠습니까?
(Y/N 입력) : Y
============================================================
요청번호 |  출고일자  | 창고위치 |  요청자  |
   1      2025-12-20      7       홍길동

순번 |       물품이름       |  수량  |    단가    |
  1   이거4조 수분 핸드크림      5       35000
순번 |       물품이름       |  수량  |    단가    |
  2   이거4조 스킨토너          7        70000
                                    |   총 금액  |
                                       105000
============================================================
아무 키나 누르면 메뉴 화면으로 이동합니다.
: d
```

**구현 특징:**

- 승인된 요청에 대해서만 고지서 출력 가능
- CLI 환경에 맞게 요청 정보와 물품 목록을 포맷팅

### 6. 출고 현황 조회

사용자(User)는 자신의 출고 요청에 대한 정보와, 출고 요청 상품 목록을 조회할 수 있고, 관리자(Admin)는 미승인 요청에 대한 조회와 기간별 출고 현황에 대한 조회를 할 수 있습니다.

```java
// User
============================================================
####################### 출고 현황 조회 #######################
============================================================
1. 출고 요청 조회		    2. 요청 상품 리스트	      3. 뒤로가기
============================================================
메뉴를 고르세요. : 1
============================================================
요청번호 |  출고기한  | 창고번호 |      요청일자      |
    1     2025-12-20      7     2025-11-01 09:40:52

                        |  요청상태  |    승인/취소일자     |
                           PENDING             -
============================================================
요청번호 |  출고기한  | 창고번호 |      요청일자      |
    2     2025-11-20      4     2025-10-29 13:14:26

                        |  요청상태  |    승인/취소일자     |
                           APPROVED   2025-11-01 11:05:34
============================================================
아무 키나 누르면 메뉴 화면으로 이동합니다.
: d
```

사용자 한 명이 작성한 여러 건의 출고 현황 조회가 가능합니다.

```java
============================================================
####################### 출고 현황 조회 #######################
============================================================
1. 출고 요청 조회		    2. 요청 상품 리스트	      3. 뒤로가기
============================================================
메뉴를 고르세요. : 2
============================================================
요청번호|순번|    물품번호   |      물품명        | 수량 |창고번호|
    1     1    12345678901  이거4조 수분 핸드크림   5       7

============================================================
요청번호|순번|    물품번호   |      물품명        | 수량 |창고번호|
    1     2    12312312312   이거4조 스킨토너       7       7

============================================================
아무 키나 누르면 메뉴 화면으로 이동합니다.
: d
```

```java
// Admin
============================================================
####################### 출고 현황 조회 #######################
============================================================
1. 미승인 요청 조회		2. 기간별 출고 현황	      3. 뒤로가기
============================================================
메뉴를 고르세요. : 1
============================================================
요청번호 1 , 홍길동 님의 출고 요청이 대기중입니다.
창고ID | 상품건수 |   출고기한   |        요청일자
   7       2       2025-12-20    2025-11-01 09:40:52

============================================================
요청번호 9 , 고길동 님의 출고 요청이 대기중입니다.
창고ID | 상품건수 |   출고기한   |        요청일자
   11       4      2025-12-01    2025-11-01 14:23:04

============================================================
아무 키나 누르면 메뉴 화면으로 이동합니다.
: d
```

여러 사용자들의 미승인된 요청을 조회할 수 있습니다.

```java
============================================================
####################### 출고 현황 조회 #######################
============================================================
1. 미승인 요청 조회		2. 기간별 출고 현황	      3. 뒤로가기
============================================================
메뉴를 고르세요. : 2
============================================================
                        [기간별 출고 현황]
============================================================
시작 날짜를 입력하세요(8자리 숫자로 입력) : 2025-09-01
============================================================
종료 날짜를 입력하세요(8자리 숫자로 입력) : 2025-09-30
============================================================
[1] 요청번호 6
    요청자 | 창고ID |     출고물품     |        출고일자
    신세계     3      리무버 등 2건      2025-09-04 17:23:05

[2] 요청번호 8
    요청자 | 창고ID |     출고물품     |        출고일자
    이세계     15     폼클렌징 등 5건    2025-09-30 13:45:13

============================================================
아무 키나 누르면 메뉴 화면으로 이동합니다.
: d
```

해당 기간동안 출고 승인된 요청들을 모아서 조회합니다. <br>

**구현 특징:**

- 권한별 사용 목적에 맞는 메뉴 분기 및 기능 분기
- DB 내부에서 출고 물품 포맷팅('[상품명] 등 [상품 건수]')

---

## 🔧 기술적 구현 사항

### 1. 계층형 아키텍처 적용

```
View (Main.java)
    ↓
Controller (InboundControllerImpl, OutboundControllerImpl)
    ↓
Service (InboundServiceImpl, OutboundServiceImpl)
    ↓
DAO (InboundDao, OutboundDao, InboundItemDao, OutboundItemDao)
    ↓
Database (MySQL)
```

**각 계층의 역할:**

- **Controller**: 사용자 입력 처리 및 View와 Service 연결
- **Service**: 비즈니스 로직 처리
- **DAO**: 데이터베이스 CRUD 작업(프로시저 실행)

### 2. 싱글톤 패턴 적용

```java
// InboundControllerImpl.java
...
    private static InboundControllerImpl inboundControllerImpl;
    private InboundControllerImpl() {}

    public static InboundControllerImpl getInstance() {
        if (inboundControllerImpl == null) {
            inboundControllerImpl = new InboundControllerImpl();
        }
        return inboundControllerImpl;
    }

    private InboundService inboundService = InboundService.getInstance();
...
```

**적용 이유:**

- 애플리케이션 전체에서 하나의 인스턴스만 사용
- 메모리 효율성 및 일관된 상태 관리

## 📊 구현 결과

### 주요 성과

1. **비즈니스 로직 구현**

   - 입출고 요청의 전체 생애주기 관리 (등록 → 승인 → 재고 반영)
   - 역할 기반 접근 제어를 통한 보안성 강화
   - 프로시저와 트리거를 통한 로직 캡슐화

2. **유지보수성 향상**

   - 계층형 아키텍처로 관심사 분리
   - 자주 사용할 정보를 별도의 디렉토리로 관리하여 코드 재사용성 향상
   - 데이터 접근시 프로시저 사용으로 코드 단순화 및 수정 편의 제고

3. **사용자 경험 개선**
   - 직관적인 메뉴 구조
   - 동일 메뉴에 권한별로 기능을 다르게 구현하여 편의성 향상

---

## 💡 배운 점 및 개선 방향

### 배운 점

1. **데이터베이스 설계의 중요성**

   DB 설계에 가장 많은 시간을 쏟았던 것 같습니다. 설계부터 잘못된 프로젝트는 나중에 돌이키기 어려울 것이라고 생각해서 팀원들과 회의를 통해 많은 의견을 나눴습니다. <br>
   개념 -> 논리 -> 물리 순으로 설계 과정을 경험하면서 다양한 설계 도구들(다이어그램, ERD 협업 툴 등)을 사용해보며 표현 방법들을 익힐 수 있었고 설계가 그만큼 중요한 과정이라는 걸 느낄 수 있었습니다.

2. **논리적인 로직 구현의 중요성**

   담당했던 입출고관리 로직이 개인적으로 만족할만한 완성도를 가지지는 못했다고 생각합니다. 첫 프로젝트였던 만큼 사소한 실수도 많았고, 바람직하지 않은 방향으로 로직이 진행되어 중간중간 고뇌의 시간(?)을 많이 겪었습니다. <br> 한번에 완벽한 로직이 나올 수는 없다고 생각하기에 이 부분은 지속적으로 요구사항 분석과 기능 설계-구현을 실행해보는 과정을 경험해보며 숙달해야 할 것 같습니다.

3. **'방향'의 중요성**

   팀원들 모두 각자 맡은 역할을 너무나 성실히 수행해 주셨고 그 덕분에 성공적으로 첫 프로젝트를 마무리했습니다. 주어진 시간이 많지 않았지만 요구사항을 완전히 실행할 수 있는 완성도있는 결과물을 만들어냈습니다.

   팀에서 유일한 비전공자인 저는 다른 팀원들보다는 경험도 부족하고, 구현 능력도 부족했을 것입니다. 그럼에도 불구하고 팀의 목적을 달성할 수 있었던 배경에는 불필요하고 소모적인 논의나 내용 점검 등을 줄이고, 각자 기능 구현에 집중하되 코드 활용이나 기능 연결 등 필요할 때만 적극적으로 소통하는 방식으로 프로젝트를 진행했습니다.

   방향을 정확히 정해놓고 목적지로 갈 수 있어서 그 과정에서 겪었던 많은 고난과 시행착오들이 단순히 시간낭비가 아닌, 배움의 과정이 될 수 있었고 이전보다 더 성장할 수 있는 계기가 되었던 것 같습니다.

### 개선 방향

1. **클린 코드**

   - 가독성과 재사용성을 고려한 코드 리팩토링
   - 적절한 자료형의 사용과 객체 지향 프로그래밍

2. **설계 구조에 맞는 객체 역할 고려**

   - SRP(단일 역할 원칙) 지향

3. **웹 환경으로의 이식성**

   - 일회성 정보 제공 지양, 비즈니스 로직 점검

---
