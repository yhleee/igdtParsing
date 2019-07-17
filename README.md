# igdtParsing

### Overview

---

OliveOne 상품 마스터 DB 에 저장되어있는 텍스트 형식의 전성분 정보를 분류하여 표준화된 전성분 Master 와 매칭시킨 후, 상품별 전성분 DB 에 저장하는 프로그램 

### 결과물

---

 `27453` 개 상품 분류 (대분류 - 기초, 바디, 색조, 프레그런스,  헤어용품) 후 전성분 총 `759293` 개 로 집계 완료 

분류 실패 (수작업 필요) `93369` 개 상품 <> 전성분 자료 엑셀로 export. 

분류 성공 총 `665924` 개 전성분 매칭 정보 **oliveone_gds_ingredient** 테이블 저장 완료 

[ oliveone_gds_ingredient ] 

[](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#22c1e7763afe428d8cfd1e08f76bfbfb)

igdt_cnt : 분류완료한 전성분 카운트

result_cnt : 수작업 필요한 전성분 카운트

[2019년 6월 19일 기준]

- 각 상품별 분류한 성분수, 매칭 성공수, 매칭 실패수 + 카테고리별 전성분 분류 결과 자료

    [전성분 분류 counting 결과.xls](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#a882117b71db4333a088bfe8f3bcb356)

- 매칭 실패 List

    [[결과물] 전성분 matching 실패 리스트 20190619.xlsx](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#34453bdbde2c4c46b3303acc3d7f904f)

### DB 정보

---

Tablet App 개발 DB (MariaDB) 의 아래 Table 사용 

- **gds_ingredient_info -- 식약처 전성분 마스터**

    [](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#da746f07f7de48e8b8674d86e1c20bf9)

- **oliveone_gds_detail_cls - 상품별 카테고리 정보 추가**

    [](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#ceff9ecd14b94cd4a7db3c4b7b3eb335)

- **(결과물) oliveone_gds_detail_last - 올리브원 상품 / 전성분 데이터 with Count**

    [](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#7be9559314af4c15b5da826b7cd334e2)

- **(결과물) oliveone_gds_ingredient -- 올리브원 상품코드& 전성분 코드 매핑 테이블**

    [](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#39f05afc35ef4816968870cd62dbe371)

### 파일 구성

---

[](https://www.notion.so/a59e9128678840ccbbf101d8a2864541#54c69f17517f424bb9715db11781f71c)

- 추가한 library

    poi - 엑셀 read/write/export 용

    mariadb jdbc - DB connection 용 

        <root url="jar://$PROJECT_DIR$/../../Tools/poi-4.1.0/poi-4.1.0.jar!/" />
              <root url="jar://$PROJECT_DIR$/../../Tools/poi-4.1.0/poi-examples-4.1.0.jar!/" />
              <root url="jar://$PROJECT_DIR$/../../Tools/poi-4.1.0/poi-excelant-4.1.0.jar!/" />
              <root url="jar://$PROJECT_DIR$/../../Tools/poi-4.1.0/poi-ooxml-4.1.0.jar!/" />
              <root url="jar://$PROJECT_DIR$/../../Tools/poi-4.1.0/poi-ooxml-schemas-4.1.0.jar!/" />
              <root url="jar://$PROJECT_DIR$/../../Tools/poi-4.1.0/poi-scratchpad-4.1.0.jar!/" />
              <root url="jar://$PROJECT_DIR$/../../Tools/mariadb-java-client-2.4.1.jar!/" />

### 분류방법

---

- 대부분 전성분 구분은 `,` 로 되어있음

    예시 

        정제수, 글리세린, 사이클로펜타실록산, 에칠헥실팔미테이트, 세테아릴에칠헥사노에이트, 메칠글루세스-10, 나이아신아마이드, 펜틸렌글라이콜, 변성알코올, 부틸렌글라이콜, 카라기난추출물, 사탕수수추출물, 지모추출물, 베타인, 1,2-헥산디올, 판테놀(0.5%), 피토스테릴/옥틸도데실라우로일글루타메이트, 피이지-20글리세릴이소스테아레이트, 소듐시트레이트, 아데노신, 시트릭애씨드, 폴리글루타믹애씨드, 하이드로제네이티드폴리이소부텐, 마데카소사이드, 토코페롤, 디소듐이디티에이, 페녹시에탄올, 향료

- 전성분 text 를 `,` 로 나눈 후, Array 에 저장하여 나누어진 keyword 와 일치하는 값이 있는지 전성분 master 와 비교한다.
- 전성분 mater 의 `igdt_nm` (전성분명), `igdt_asis_nm` (전성분 구명칭) 와 일치하는지 비교함
- 아래 항목은 예외처리로 전성분 master 와 비교 하기 전에 걸러준다.
    - 1,2 헥산디올

- 한 상품의 분류가 끝날때마다 **`oliveone_gds_detail_last`** 에 성공/실패 count 업데이트 한다.

        //goods 의 전성분 성공 / 실패 갯수 count 한 후 goods 에 업데이트 하기.
         con.updateProduct(igdt_cnt, result_cnt,gdsArr.get(index).getId());

- 아래 두 arraylist 에 분류값을 모두 담은 후에 분류가 끝나면 db insert 후 excel 결과물을 생성한다.

        ArrayList<String[]> matchedList = new ArrayList<String[]>();
        ArrayList<String[]> unmatchedList = new ArrayList<String[]>();

### SQL

---

- 일치하는 전성분/화장품 리스트

        select a1.gds_cd, a1.gds_nm, a2.igdt_cd, a2.igdt_nm from oliveone_gds_detail_last a1,gds_ingredient_info a2, oliveone_gds_ingredient 

- 개발하며 사용한 쿼리

        --삭제 
        delete from oliveone_gds_ingredient; 
        
        --상품목록
        select * from oliveone_gds_detail_last;
        
        SHOW FULL COLUMNS FROM oliveone_gds_detail_last;
        
        --상품목록 with category 
        select * from oliveone_gds_detail_cls;
        
        --전성분 목록 
        select * from gds_ingredient_info;
        
        select * from gds_ingredient_info where igdt_cd LIKE "001650";
        select * from oliveone_gds_detail where gds_cd = "00000020105";
        
        --결과물확인
        select * from oliveone_gds_ingredient;
        
        
        --불러오기 test 11
        select a1.gds_cd, a1.gds_nm, a2.igdt_cd, a2.igdt_nm from oliveone_gds_detail_last a1,gds_ingredient_info a2, oliveone_gds_ingredient
        where a1.gds_cd = oliveone_gds_ingredient.gds_cd
        and a2.igdt_cd = oliveone_gds_ingredient.igdt_cd 
        limit 50;
        
        
        --불러오기 test 22 
        select a1.gds_cd, a1.gds_nm, a3.gds_lcls_nm, a3.gds_mcls_nm, a2.igdt_cd, a2.igdt_nm from oliveone_gds_detail_last a1,gds_ingredient_info a2, oliveone_gds_detail_cls a3, oliveone_gds_ingredient
        where a1.gds_cd = oliveone_gds_ingredient.gds_cd
        and a2.igdt_cd = oliveone_gds_ingredient.igdt_cd 
        and a3.gds_cd = oliveone_gds_ingredient.gds_cd
        limit 50;
        
        --불러오기 test 33 이형수님 전달자료 
        select a1.gds_cd, a1.gds_nm, a3.gds_lcls_nm, a3.gds_mcls_nm, a1.igdt_cnt, a1.result_cnt from oliveone_gds_detail_last a1,oliveone_gds_detail_cls a3
        where a1.gds_cd = a3.gds_cd
        ;
        
        --gds table column 추가 
        alter table oliveone_gds_detail_last modify column result_cnt int ;
        
        select count (1) from TB_RANK_GDS_LIST;

### 추가 예외처리 필요 항목

---

1. 여러개의 항목이 들어있는 경우  
    - 화장품 세트인 경우 여러개의 제품에 대한 분류 처리 (아래 비슷한 케이스 예시). 세트로 분류되어 있지 않은 상품도 같은 경우 발생.
    - [핀터놀크림]
    - *Step1:
    - 토너:정제수
2. 예외처리할 키워드 
    - 해당사항없음
    - _
3. 마스터 테이블과 100퍼센트 일치하지 않는 키워드 → 수동으로 걸러낼 수 있도록 비슷한 전성분 보여주기 
    - 판테놀(0.5%) - 판테놀 만 마스터에 존재
    - 전성분 앞에 * 붙은 경우 ex) *나이아신아마이드
    - 띄어쓰기
        - 아크릴레이트/C10-30알킬아크릴레이트크로스폴리머
        - 아크릴레이트/C10- 30알킬아크릴레이트크로스폴리머 (마스터표)
