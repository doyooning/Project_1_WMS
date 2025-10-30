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
- **`TotalAdmin`**: 총관리자 정보
- **`Warehouse`**: 창고의 기본 정보 (위치, 종류, 용량 등)
- **`Product`**: 상품 정보
- **`Stock`**: 창고별 상품 재고 현황
- **`Inbound` / `Outbound`**: 입고 및 출고 요청 정보
- **`InboundItem` / `OutboundItem`**: 입/출고 요청에 포함된 상품 목록

---

## ✨ 상세 기능 설명

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
