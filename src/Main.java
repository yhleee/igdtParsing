import Model.Goods;
import Model.Ingredient;
import Model.Keyword;
import Utils.CSVReader;
import Utils.DBConnection;
import Utils.ExcelExport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

public class Main {
    public static ArrayList<String[]> matchedList = new ArrayList<String[]>();
    public static ArrayList<String[]> unmatchedList = new ArrayList<String[]>();

    public static void main(String[] args) throws SQLException {

        //DB Connection
        DBConnection con = new DBConnection();
        System.out.println("please select the action");
        System.out.println("0. 1차 분류 - 상품 master detail 분류 ");
        System.out.println("1. 2차분류 - 미매칭 키워드 기준 수작업 성공 리스트 반영");
        System.out.println("2. 2차분류 - 전성분 재처리 - ',' 처리 후 성공 리스트 업데이트");

        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        //Get Data from Master Model.Ingredient Table
        ArrayList<Ingredient> igdtArr = con.igdtSelect();
        ArrayList<Goods> gdsArr = con.gdsSelect();
        CSVReader csvReader = new CSVReader();


        if (str.equals("0")) {
            ArrayList<String[]> matchedList = new ArrayList<String[]>();
            ArrayList<String[]> unmatchedList = new ArrayList<String[]>();

            MatchHandler.parsing(igdtArr, gdsArr);

            //분류 후 DB 입력
            con.insertMatchKeyword(matchedList);
            con.insertUnmatched(unmatchedList);

            //처리결과 다운로드
            ExcelExport ex = new ExcelExport();
            try {
                ex.excelExport_matched(matchedList);
                ex.excelExport_unmatched(unmatchedList);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (str.equals("1")) {//수작업 완료 리스트 반영 (미매칭 전성분 -> 매칭리스트 반영)

            //수작업 완료 리스트는 테이블에 저장됨

            //select query
            ArrayList<String[]> resultArray = con.selectFailedKeywords();

            //insert to matched List
            con.insertMatchKeyword(resultArray);

        } else if (str.equals("2")) {//, 구분자로 된 키워드 재처리 (9월 24일)
            int gdsArrSize = 0;
            ArrayList<Keyword> keywordArr = con.keywordSelect();
            ArrayList<Goods> goodsArr = new ArrayList<>();

            for (int i = 0; i < keywordArr.size(); i++) {
                Goods gds = new Goods(keywordArr.get(i).getGds_cd(), keywordArr.get(i).getGds_nm(),
                    keywordArr.get(i).getKeyword_nm());
                goodsArr.add(gdsArrSize, gds);
                gdsArrSize++;
            }

            //미매칭 전성분 대상 다시 분류
            MatchHandler.parsing(igdtArr, goodsArr);

            //미매칭 2차 분류 후 다시 생긴 미매칭 건 추가하기
            con.insertUnmatched(unmatchedList);

            //재처리결과 엑셀 다운로드
            ExcelExport ex = new ExcelExport();
            try {
                ex.excelExport_matched(matchedList);
                ex.excelExport_unmatched(unmatchedList);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


}
