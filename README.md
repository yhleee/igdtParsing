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

