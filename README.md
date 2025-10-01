# WMS (Warehouse Management System)

## 📖 프로젝트 개요

본 프로젝트는 효율적인 창고 관리를 위한 CLI 기반의 **WMS(Warehouse Management System)** 입니다. Java 기반의 콘솔 애플리케이션으로, 역할 기반 접근 제어를 통해 사용자별 맞춤 기능을 제공합니다.

---

## 🌟 주요 기능

- **회원 관리**: 총관리자, 창고관리자, 일반회원 3가지 역할(Role)에 따른 회원가입, 로그인, 정보 수정 및 승인 관리 기능을 제공합니다.
- **창고 및 재고 관리**: 창고 등록, 재고 조회, 재고 실사 등 창고 운영의 핵심 기능을 포함합니다.
- **입/출고 관리**: 상품의 입고 및 출고 요청과 승인 프로세스를 관리합니다.
- **재무 관리 (Finance)**: 창고별 매출 및 지출 내역을 체계적으로 관리하고, 사용자는 구독 서비스를 이용할 수 있습니다.
- **고객센터 (Board)**: 공지사항과 문의글을 통해 관리자와 사용자 간의 원활한 소통을 지원합니다.

---

## ⚙️ 아키텍처

### **설계 패턴**

- **Singleton Pattern**: DAO, Controller 클래스들
- **MVC Pattern**: Model(Domain), View(Main), Controller 분리
- **DAO Pattern**: 데이터 접근 로직 분리

본 프로젝트는 **계층형 아키텍처(Layered Architecture)**를 적용하여 각 기능의 독립성과 재사용성을 높였습니다.

- **`View (view/Main.java)`**: 사용자와의 상호작용을 담당하며, 사용자 입력을 받아 `Controller`에 전달합니다.
- **`Controller`**: 사용자의 요청을 받아 비즈니스 로직을 처리하는 `Service`를 호출하고, 그 결과를 `View`에 전달합니다.
- **`Service`**: 애플리케이션의 핵심 비즈니스 로직을 수행합니다. 여러 `DAO`를 조합하여 복잡한 작업을 처리합니다.
- **`DAO (Data Access Object)`**: 데이터베이스에 직접 접근하여 SQL 쿼리를 실행하고, CRUD(Create, Read, Update, Delete) 작업을 담당합니다.
- **`Domain`**: 애플리케이션에서 사용되는 데이터 객체를 정의합니다. (e.g., `User`, `Warehouse`, `Expense`)
- **`Common`**: 애플리케이션에서 공통으로 사용되는 예외 처리 문구 및 문자열을 enum 클래스로 정의합니다.
- **`Util`**: 데이터베이스 연결을 담당합니다.

## 🗄️ 데이터베이스 주요 테이블

- **`User`**: 일반회원 정보
- **`WarehouseAdmin`**: 창고관리자 정보
- **`TotalAdmin`**: 총관리자 정보
- **`Approval`**: 회원가입 승인 요청 관리
- **`Warehouse`**: 창고의 기본 정보 (위치, 종류, 용량 등)
- **`Product`**: 상품 정보
- **`Stock`**: 창고별 상품 재고 현황
- **`CheckLog`**: 재고 실사 기록
- **`Inbound` / `Outbound`**: 입고 및 출고 요청 정보
- **`InboundItem` / `OutboundItem`**: 입/출고 요청에 포함된 상품 목록
- **`Sales`**: 구독 서비스로 발생한 매출 정보
- **`Expense`**: 창고 운영 지출(임대료, 유지보수비) 정보
- **`SubModel`**: 구독 요금제 모델 정보
- **`SubApproval`**: 사용자의 구독 신청 및 승인 상태
- **`Announcement`**: 공지사항 게시글
- **`Inquiry`**: 사용자 문의글
- **`Response`**: 문의글에 대한 관리자 답변

---

## ✨ 상세 기능 설명

### 🖥️ **메인 애플리케이션 흐름 (Main Application Flow)**

애플리케이션의 전체적인 흐름은 `view/Main.java` 파일에 의해 제어됩니다.

1. **시작 및 초기 메뉴**:
    - `main` 메서드가 실행되면 `run()` 메서드가 호출되어 메인 루프가 시작됩니다.
    - 사용자에게는 로그인 전 이용할 수 있는 메뉴(회원가입, 로그인, 고객센터, 아이디/비밀번호 찾기)가 제공됩니다.
2. **역할별 대시보드 전환**:
    - 로그인이 성공하면, 사용자의 역할(`currentUserType`)에 따라 각각의 대시보드 메서드(`handleUserDashboard`, `handleWarehouseAdminDashboard`, `handleTotalAdminDashboard`)가 호출됩니다.
    - 각 대시보드는 해당 역할이 접근할 수 있는 기능 목록(예: 입/출고 관리, 재고 관리, 회원 관리 등)을 메뉴 형태로 보여줍니다.
3. **기능 실행 및 컨트롤러 호출**:
    - 사용자가 대시보드에서 특정 기능을 선택하면, `Main.java`는 해당 기능에 맞는 컨트롤러(예: `financeControl`, `boardControl`)의 메서드를 호출하여 작업을 위임합니다.
    - 예를 들어, 일반회원이 '재무관리'를 선택하면 `financeControl.showFinanceMenu()`가 호출됩니다.
4. **로그아웃**:
    - 사용자가 '로그아웃'을 선택하면, 모든 컨트롤러에 저장되어 있던 현재 사용자 정보가 `logoutUser()`를 통해 초기화됩니다.
    - 이후 애플리케이션은 다시 1번의 초기 메뉴 화면으로 돌아가 다음 사용자의 입력을 기다립니다.

### 👤 **회원 및 로그인 관리 (Member & Login Management)**

### **회원가입 기능**

- **일반회원/창고관리자 회원가입**
    1. 회원가입 메뉴에서 사용자 유형 선택 (1: 일반회원 / 2: 창고관리자)
    2. ID, PW, 이름, 전화번호, 이메일 정보 입력
    3. 비밀번호는 **SHA-256**으로 해싱하여 안전하게 저장
    4. `Approval` 테이블에 `PENDING` 상태로 승인 요청 생성
    5. "회원가입 신청이 접수되었습니다. (관리자 승인 대기)" 메시지 출력
- **총관리자 회원가입**
    - 별도의 승인 절차 없이 즉시 계정이 생성됩니다. (현재 코드에서는 주석 처리됨)

**로그인 기능**

- **로그인 프로세스**
    1. 로그인 유형 선택 (1: 일반회원 / 2: 창고관리자 / 3: 총관리자)
    2. ID와 비밀번호 입력
    3. 입력된 비밀번호를 해시하여 저장된 값과 비교 검증
    4. 일반회원과 창고관리자의 경우, **승인 상태**를 추가로 확인
    5. 로그인 성공 시 역할에 맞는 대시보드로 진입
- **승인 상태별 처리**
    - **`APPROVED`**: 정상적으로 로그인되어 대시보드로 진입합니다.
    - **`PENDING`**: "[회원가입 승인 대기중입니다]" 메시지를 출력하고 초기화면으로 복귀합니다.
    - **`REJECTED`**: "[회원가입 승인 거절되었습니다]" 메시지를 출력하고 초기화면으로 복귀합니다.
    - **승인 정보 없음**: "[승인 정보가 없습니다]" 메시지를 출력하고 초기화면으로 복귀합니다.

### **회원가입 승인 기능 (총관리자 전용)**

- **승인 화면 구성**

```
    ============================================================
    <회원가입 승인 화면>
    ============================================================
    [일반회원가입 신청자] - 총 N명
    ============================================================
    1. 회원번호: {uIdx}, 이름: {uName}, 전화번호: {uPhone}, 이메일: {uEmail}, 신청일: {User.createdAt}
    2. 회원번호: {uIdx}, 이름: {uName}, ...
    ============================================================
    [창고관리자 회원가입 신청자] - 총 M명
    ============================================================
    1. 창고관리자번호: {waIdx}, 이름: {waName}, 전화번호: {waPhone}, 이메일: {waEmail}, 신청일: {WarehouseAdmin.createdAt}
    ============================================================
    A : 회원가입 승인(Approved)
    R : 회원가입 거절(Rejected)
    Q : 종료
    ============================================================
    1(일반회원가입승인), 2(창고관리자가입승인) 중 선택하세요.
   ```

- **승인 처리 프로세스**
    1. 승인/거절할 대상 선택 (1: 일반회원 / 2: 창고관리자)
    2. 수행할 처리 선택 (A: 승인 / R: 거절)
    3. 처리할 신청자의 목록 순번 입력
    4. `Approval` 테이블의 상태를 **`APPROVED`** 또는 **`REJECTED`*로 업데이트하며, 승인 시 처리한 총관리자의 `taIdx`를 함께 기록합니다.

### **대시보드 및 마이페이지**

- **사용자별 대시보드**
    - **일반회원**: 입고관리, 출고관리, 재무관리, 고객센터, 창고관리, 재고관리
    - **창고관리자**: 창고관리, 재고관리, 재무관리, 고객센터
    - **총관리자**: 회원관리, 재무관리, 입고관리, 출고관리, 창고관리, 재고관리, 고객센터
  - **마이페이지 기능**

   ````
  ============================================================
    <마이페이지>
    ============================================================
    아이디: [사용자 ID]
    이메일: [사용자 이메일]
    이름: [사용자 이름]
    전화번호: [사용자 전화번호]
    회원가입 일자: [가입일시]
    ============================================================
      1. 내 정보 수정
      2. 비밀번호 수정
      3. 회원탈퇴
     ============================================================
  `````


### **보안 기능**

- **비밀번호 보안**
    - **SHA-256 해싱**: 모든 비밀번호는 해시 처리되어 데이터베이스에 저장됩니다.
    - **비밀번호 찾기**: 임시 비밀번호를 생성하여 사용자에게 제공하고, 이 또한 해시화하여 DB에 저장합니다.
    - **비밀번호 수정**: 변경 시 현재 비밀번호를 입력하여 본인 인증을 거치며, 새로운 비밀번호를 2회 입력받아 일치 여부를 확인합니다.
- **회원탈퇴 보안**
    - **비밀번호 확인**: 탈퇴 전 현재 비밀번호 입력을 통해 본인 인증을 수행합니다.
    - **Soft Delete**: 실제 데이터를 삭제하는 대신, `status`를 'DELETED'로 변경하여 데이터를 보존하고 비활성화 처리합니다.

### **추가 기능**

- **아이디/비밀번호 찾기**
    - **아이디 찾기**: 이메일 주소를 통해 가입된 모든 사용자 유형(일반, 창고, 총관리자)의 아이디를 조회할 수 있습니다.
    - **비밀번호 찾기**: 아이디를 입력하면 임시 비밀번호를 생성하여 반환합니다.
- **예외 처리**
    - **`ErrorHandler`**: 예외 발생 시 중앙에서 일관된 방식으로 처리합니다.
    - **사용자 친화적 메시지**: 기술적 오류 내용을 사용자가 이해하기 쉬운 메시지로 변환하여 안내합니다.
    - **개발자 로깅**: `System.err`를 통해 개발자가 오류를 추적할 수 있도록 상세 정보를 기록합니다.

### 📦 **창고 관리 (Warehouse)**

총관리자는 창고 등록과 조회가, 창고관리자는 창고 조회가, 일반 회원은 창고 전체 조회만 가능하도록 역할에 따라 권한이 차등 부여됩니다.

### ✅ **창고 등록 (총관리자 전용)**

- 총관리자는 `addWarehouse` 메서드를 통해 새로운 창고를 시스템에 등록할 수 있습니다.
- **입력 값 유효성 검사**:
    - 창고 고유 번호는 **'ware'**로 시작해야 하며, 중복될 수 없습니다.
    - 창고 종류는 **'보관형 창고'** 또는 **'마이크로 풀필먼트'**만 허용됩니다.
    - 주소 입력 시, 앞부분의 도시 정보가 DB의 지역 코드와 매핑되어 저장됩니다.
- **창고 용량 제한**:
    - **보관형 창고**: 800 ~ 1,000 박스
    - **마이크로풀필먼트**: 100 ~ 300 박스

### ✅ **창고 조회**

- 총관리자와 창고관리자는 `showWarehouseSearchMenu`를 통해 다양한 조건으로 창고를 조회할 수 있으며, 일반회원은 전체 창고 목록 조회만 가능합니다.
- 조회 결과가 많을 경우, `printList` 메서드를 통해 한 페이지당 최대 20개의 항목만 보이도록 페이징 처리됩니다.
1. **전체 창고 조회(`getWarehouseList`)**
- 모든 창고 목록을 조회 (보관형 창고 + 마이크로 풀필먼트)
1. **창고 고유번호 조회(`getWarehouse`)**
- 특정 창고 고유 번호(ware로 시작)를 입력하여 해당 창고의 상세 정보를 조회
- 해당 창고의 창고 번호, 창고 이름, 창고 임대료, 현재 재고, 최대수용용량, 창고 타입, 도주소 등 확인 가능
1. **소재지별 창고 조회(`getAddrWarehouse`)**
- 도(시) 단위로 창고 목록을 조회
- 창고 번호, 창고 이름, 창고 임대료, 창고별 재고, 상세주소 확인 가능
1. **창고 종류별 조회(`getTypeWarehouse`)**
- 창고 유형(보관형 창고, 마이크로 풀필먼트)에 따라 창고 목록을 조회
- 창고 번호, 창고 이름, 창고 임대료, 창고별 재고, 도주소 확인 가능

### 💄 **재고 관리 (Stock)**

총관리자와 창고관리자는 재고 조회와 재고 실사 메뉴 모두에 접근할 수 있지만, 일반회원은 재고 조회만 가능하도록 설계되었습니다.

### ✅ **재고 조회**

- 모든 사용자는 `showStockSearchMenu`를 통해 다양한 방식으로 재고를 조회할 수 있습니다.
- 조회 결과는 `printStockList`를 통해 품목(바코드 번호) 단위로 페이지를 구성하여 가독성을 높였습니다.
1. **전체 재고 조회(`getAllStockList`)**
- 모든 재고 목록을 조회
- 바코드번호와 물품 이름, 창고번호, 가용재고, 불량재고, 안전재고, 대분류, 수정일 확인 가능
1. **대/중/소분류별 재고 조회(`getPrimaryStockList`, `getSecondaryStockList`, `getTertiaryStockList`)**
- 사용자로부터 카테고리를 입력받아 유효성 검사를 거친 후 해당 카테고리에 있는 재고 목록을 조회
- 대분류별 조회에서는 중분류를 확인 가능하며, 중분류별 조회에서는 소분류를 확인 가능하도록 설계
1. **물품 번호(바코드번호)별 재고 조회(`getProductStockList`)**
- 사용자로부터 바코드번호를 입력받아 유효성 검사를 거친 후 해당 물품이 존재하는 모든 창고에서의 재고 목록을 조회
- 바코드번호와 물품 이름, 창고번호, 가용재고, 불량재고, 안전재고, 소분류, 수정일 확인 가능

### ✅ **재고 실사 (총관리자/창고관리자 전용)**

- `showStockChecklogMenu`를 통해 접근하며, 역할에 따라 수행 가능한 작업이 다릅니다.
- **재고 실사 등록 (`addCheckLog`)**: **창고관리자만** 가능합니다.
    - 담당 창고가 배정된 창고관리자만 이 기능을 사용할 수 있습니다.
    - 실제 재고와 전산상 재고가 일치하면 **`CORRECT`**, 불일치하면 **`INCORRECT`** 상태로 실사 로그를 기록합니다.
- **재고 실사 삭제 (`removeCheckLog`)**:
    - **총관리자**: 모든 재고 실사 기록을 삭제할 수 있습니다.
    - **창고관리자**: 자신이 관리하는 창고의 재고 실사 기록만 삭제(상태를 `DELETE`로 변경)할 수 있습니다.
- **재고 실사 조회**:
    - **전체 조회 (`getCheckLogList`)**: 총관리자는 모든 기록을, 창고관리자는 담당 창고의 기록만 조회합니다.
    - **섹션별 조회 (`getSectionCheckLogList`)**: 창고 내 특정 섹션을 지정하여 해당 구역의 실사 기록만 조회합니다.
    - **창고별 조회 (`getWarehouseCheckLogList`)**: 특정 창고를 지정하여 해당 창고의 모든 실사 기록을 조회합니다.
- **재고 실사 수정 (`updateCheckLog`)**: **창고관리자만** 가능합니다.
    - 담당 창고가 배정된 창고관리자만 사용할 수 있습니다.
    - 실제 재고와 전산 재고를 비교하여 불일치했던(`INCORRECT`) 기록을 **`CORRECT`** 상태로 수정할 수 있습니다.

### ⚠ **예외 처리**

- **입력 유효성 검사**: 사용자 입력이 정해진 형식(숫자, 문자열 패턴 등)에 맞지 않으면 오류 메시지를 출력하고 재입력을 요청합니다.
- **Service 계층**: `ExceptionManager`를 통해 비즈니스 로직 상의 예외(e.g., 권한 없음, 데이터 불일치)를 처리하고, 이를 Controller로 전달합니다.
- **DAO 계층**: 데이터베이스 접근 중 발생하는 `SQLException`을 `DaoException`으로 감싸 Service 계층으로 전달합니다. Service는 이를 받아 `null`, `1`, `false` 등 약속된 값으로 변환하여 Controller에 반환합니다.

### 🔁 입고**관리(Inbound), 출고관리(Outbound)**

`InboundControllerImpl.java`와 `OutboundControllerImpl.java`중심으로 구현되었으며, 사용자의 권한에 따라 입출고 요청과 처리를 효율적으로 지원하기 위해 다음과 같은 기능을 제공합니다:

### 👥 사용자 공통 기능

- **입출고 요청 수정(**`selectUpdateMenu`**)**

  등록된 ‘승인대기’ 상태인 입출고 요청을 수정할 수 있습니다.  요청 정보 수정과 물품 정보 수정을 선택할 수 있는 메뉴를 사용자에게 보여줍니다.

    - **요청 정보 수정(**`InputRequestDataUpdate`**)**

      해당 요청 번호의 요청 정보를 수정합니다. 창고번호와 입출고기한을 수정 가능하며, 해당 입출고 요청 수정 시 동일한 요청 번호에서 생성된 물품들의 창고번호도 수정된 값으로 반영됩니다.

    - **물품 정보 수정(**`InputRequestItemUpdate`**)**

      해당 요청 번호의 물품 정보를 수정합니다. 물품 순번을 입력받으면, 물품 번호와 물품 수량을 수정할 수 있습니다.

- **입출고 요청 취소(`cancelRequest`)**

  등록된 ‘승인대기’ 상태인 입출고 요청을 취소할 수 있습니다. 요청 번호를 입력받고, 해당하는 요청 번호의 입출고 요청 상태를 ‘취소됨’으로 변경합니다.

- **입출고고지서 출력(`printBill`)**

  등록된 ‘승인됨’ 상태의 입출고 요청에 대한 입출고고지서를 출력받을 수 있습니다. 요청 번호를 입력하면, 해당 요청 번호의 요청 정보와 물품 정보 목록을 고지서 형태로 사용자에게 보여줍니다.


### 👑 **총관리자 (TotalAdmin)**

- **입출고 요청 승인(**`approveRequest`**)**

  일반회원이 등록한 입출고 요청을 승인할 수 있습니다. 요청 번호를 입력받고, 해당하는 요청 번호의 입출고 요청 상태를 ‘승인됨’으로 변경합니다. 요청이 승인됨과 동시에 해당 요청에 명시된 창고로 물품이 입출고되어 재고에 반영됩니다.

- **입출고 현황 조회(`selectAdminInfoMenu`)**

  관리자에게 필요한 입출고 현황을 조회할 수 있습니다. 미승인 입출고 요청 조회와 기간별 입출고 내역 조회를 선택할 수 있는 메뉴를 사용자에게 보여줍니다.

    - **미승인 요청 조회(`printPendingRequest` )**

      ‘승인대기’ 상태인 모든 일반회원의 입출고 요청의 정보를 보여줍니다.

    - **기간별 입출고 현황(`printRequestByPeriod`)**

      시작일과 종료일을 입력받아, 해당 기간 사이에 승인된 입출고 요청들의 정보를 보여줍니다.


### 🙋‍♂️ **일반회원 (User)**

- **입출고 요청 등록(`InputRequestData`)**

  입출고 요청을 등록할 수 있습니다. 요청 정보(창고번호, 입출고기한)를 입력받고, 물품 정보(물품번호, 물품수량)를 입력하면 입출고 요청이 만들어집니다. 하나의 입출고 요청은 다수의 물품을 포함할 수 있기 때문에, 물품 정보를 이어서 추가로 입력할 수 있으며 입력이 종료되면 입출고 요청이 등록되고 요청번호가 부여됩니다.

- **입출고 현황 조회(`selectAdminInfoMenu`)**

  일반회원에게 필요한 입출고 현황을 조회할 수 있습니다. 미승인 입출고 요청 조회와 기간별 입출고 내역 조회를 선택할 수 있는 메뉴를 사용자에게 보여줍니다.

    - **입출고 요청 조회(`printRequestList`)**

      해당 일반회원의 ‘승인됨’ 상태인 모든 입출고 요청 정보를 보여줍니다.

    - **요청 상품 리스트(`printRequestItemList`)**

      해당 일반회원의 ‘승인됨’ 상태인 모든 입출고 요청의 물품 정보 목록을 보여줍니다.


### 💰 **재무 관리 (Finance)**

`FinanceControllerImpl.java`를 중심으로 구현되었으며, 사용자의 권한에 따라 재무 정보 조회, 지출 관리, 구독 서비스 등 차별화된 기능을 제공합니다.

### 👑 **총관리자 (TotalAdmin)**

- **전체 재무 조회(**`getFinanceList`**)**: 시스템에 등록된 모든 창고의 재무 현황을 **연/월 단위**로 통합하여 조회할 수 있습니다. 매출 조회, 지출 조회, 전체 조회 세 가지 중 선택할 수 있고, 이를 통해 전체적인 수익성을 한눈에 파악하고 분석할 수 있습니다.
- **창고별 재무 조회(**`getWhFinanceList`**)**: 특정 창고를 선택하여 해당 창고의 상세 재무 데이터를 조회할 수 있습니다. `getWhFinanceList` 메서드를 통해 특정 창고의 매출(Sales) 및 지출(Expense) 내역을 상세히 확인할 수 있어, 개별 창고의 운영 상태를 정밀하게 분석하고 관리하는 데 용이합니다.

### 👨‍💼 **창고관리자 (WarehouseAdmin)**

- **담당 창고 재무 조회**: 자신이 관리하는 창고(`wIdx`)의 재무 정보만 조회할 수 있습니다. 월별/연도별 매출, 지출, 정산 내역을 상세히 파악할 수 있습니다.
- **지출 내역 관리**: 창고 운영에 필요한 비용을 직접 관리합니다.
    - **지출 등록 (`addExpense`)**: 관리비(MAINTENANCE) 또는 임대료(RENT) 항목으로 지출 내역을 등록합니다.
    - **지출 수정 및 삭제 (`modifyExpense`, `removeExpense`)**: 등록된 지출 내역을 수정하거나 삭제(status만 변경)하여 항상 최신 상태를 유지합니다.
- **구독 승인 관리**: 일반회원이 신청한 창고 구독 서비스 요청을 관리합니다.
    - **신청 목록 조회 (`getPendingSubApprovalList`)**: 자신의 창고에 접수된 구독 신청 목록을 확인합니다.
    - **구독 승인/거절 (`approveSubscription`, `rejectSubscription`)**: 창고의 현재 수용 가능 용량과 사용자의 요구 용량을 비교 검토(`getSubApprovalDetail`)한 후, 구독 신청을 승인하거나 거절합니다.

### 🙋‍♂️ **일반회원 (User)**

- **구독 서비스 관리**: 창고 구독 서비스를 신청하고 관리합니다.
    - **구독 신청 (`addSubscription`)**: 구독 가능한 창고와 구독 모델(요금제) 목록을 확인하고 원하는 서비스를 신청합니다.
    - **구독 변경 (`modifySubscription`)**: 이용 중인 구독 서비스를 다른 모델로 변경 신청할 수 있습니다. 기존 구독이 만료된 후 변경된 구독이 적용됩니다.
    - **구독 취소 (`cancelSubscription`)**: 현재 구독을 취소하여 다음 결제일에 갱신되지 않도록 설정합니다.
    - **구독 상태 조회 (`getUserSubInfo`, `getSubStatus`)**: 자신의 현재 구독 정보 및 신청 상태(승인 대기, 승인 완료 등)를 확인할 수 있습니다.

### 💬 **고객센터 (Board)**

`BoardControllerImpl.java`를 중심으로 구현되었으며, 공지사항과 문의글을 통해 관리자와 사용자 간의 원활한 소통을 지원합니다. 비회원도 일부 기능을 사용할 수 있습니다.

### 📢 **공지사항 (Announcement)**

- **총관리자**:
    - **작성, 수정, 삭제 (`addAnnouncement`, `modifyAnnouncement`, `removeAnnouncement`)**: 시스템 전체에 공지할 내용을 작성하고 관리할 수 있는 유일한 권한을 가집니다. (삭제는 status만 변경)
- **비회원을 제외한 모든 사용자 (회원)**:
    - **목록 및 상세 조회 (`getAnnouncementList`, `getAnnouncement`)**: 등록된 모든 공지사항을 목록 형태로 확인하고, 특정 공지사항을 선택하여 상세 내용을 조회할 수 있습니다.

### ❓ **문의글 (Inquiry)**

- **비회원**:
    - **문의글 작성**: 회원가입 없이 서비스에 대한 문의가 가능합니다. 작성 시 **작성자명과 비밀번호**를 별도로 설정하여 추후 자신의 글을 조회하거나 수정/삭제할 수 있습니다.
    - **문의글 조회**: 자신이 작성한 글에 한해, 작성 시 설정한 정보(작성자명, 비밀번호)를 통해 상세 내용과 관리자의 답변을 확인할 수 있습니다.
- **일반회원 (User)**:
    - **문의글 작성 (`addInquiry`)**: **1:1 문의** 또는 **일반 문의** 유형을 선택하여 글을 작성할 수 있습니다. 1:1 문의는 작성자와 총관리자만 열람할 수 있어 개인적인 문의에 용이합니다.
    - **문의글 수정 및 삭제 (`modifyInquiry`, `removeInquiry`)**: 자신이 작성한 문의글을 직접 수정하거나 삭제(status만 변경)할 수 있습니다.
- **창고관리자 (WarehouseAdmin)**:
    - **공개 문의글 조회**: 1:1 문의를 제외한 모든 일반 문의글과 그에 대한 답변을 조회할 수 있습니다.
- **총관리자 (TotalAdmin)**:
    - **모든 문의글 조회**: 1:1 문의를 포함한 모든 문의글을 열람하고 관리할 수 있습니다.
    - **답변 관리 (`addResponse`, `modifyResponse`, `removeResponse`)**: 사용자의 문의에 대해 답변을 작성, 수정, 삭제(status만 변경)하여 고객 문의에 신속하게 대응할 수 있습니다.(문의글과 1:1 관계)

---

## 🛠️ 기술 스택

- **언어**: `Java 17`
- **데이터베이스**: `MySQL 8.0`
- **라이브러리**:
    - `Lombok`: `@Data` 어노테이션 등을 통해 Boilerplate 코드를 줄여 생산성을 향상시킵니다.
    - `mysql-connector-java-8.0.22(버전이 다를 수 있음)`: Java 애플리케이션과 MySQL 데이터베이스 간의 연결을 지원합니다.

---

## 🚀 Util

<details>
<summary><b>1. dbinfo.properties</b></summary>
<div markdown="1">

    driver = 'jdbc drvier'
    url = 'database url'
    username = 'Personal database username'
    password = 'Personal database userpassword'

</div>
</details>

<details>
<summary><b>2. DBUtil</b></summary>
<div markdown="1">

    package util;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;
    import java.util.ResourceBundle;
    
    public class* DBUtil {
        private static ResourceBundle bundle;
        static {
            bundle = ResourceBundle.getBundle("util.dbinfo");
            try{
                Class.forName(bundle.getString("driver"));
                //System.out.println("Driver loaded");
            } catch (ClassNotFoundException e) {
                System.out.println("driver not found!");
                e.printStackTrace();
            }
        }
    
        public static Connection* getConnection() {
            try{
                return DriverManager.getConnection(
                        bundle.getString("url"),
                        bundle.getString("username"),
                        bundle.getString("password")
                );
            } catch (SQLException e){
                System.out.println("Connection error!");
                return null;
            }
        }
    }

</div>
</details>

<details>
<summary><b>3. ErrorHandler</b></summary>
<div markdown="1">

    package util;
    public class ErrorHandler {
        public static void displayAndLog(String userMessage, Throwable throwable) {
            // User-facing message
            System.out.println(userMessage);
            // Developer-facing log
            if (throwable != null) {
                System.err.println("[ERROR] " + throwable.getClass().getName() + ": " + throwable.getMessage());
                throwable.printStackTrace(System.err);
            }
        }
    }

</div>
</details>

<details>
<summary><b>4. PasswordUtil</b></summary>
<div markdown="1">

    package util;
    import java.nio.charset.StandardCharsets;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    
    public class PasswordUtil {
        // 임시 Pepper
        private static final String PEPPER = "wms-pepper-2025";
    
        public static String hash(String rawPassword) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashed = digest.digest((rawPassword + PEPPER).getBytes(StandardCharsets.UTF_8));
                return bytesToHex(hashed);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    
        public static boolean matches(String rawPassword, String hashedPassword) {
            return hash(rawPassword).equals(hashedPassword);
        }
    
        public static String generateTemporaryPassword() {
            // 임시 비밀번호 생성 (8자리)
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(chars.charAt((int) (Math.random() * chars.length())));
            }
            return sb.toString();
        }
        private static* String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

</div>
</details>