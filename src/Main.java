import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws SQLException {
        ArrayList<String[]> matchedList = new ArrayList<String[]>();
        ArrayList<String[]> unmatchedList = new ArrayList<String[]>();

        //DB Connection
        DBConnection con = new DBConnection();

        //Get Data from Master Ingredient Table
        ArrayList<Ingredient> igdtArr = con.igdtSelect();

        //Get Data from Goods Table
        ArrayList<Goods> gdsArr = con.gdsSelect();

       for (int index = 0; index < gdsArr.size(); index++) {
        //for (int index = 0; index < 100; index++) {

            String detail = gdsArr.get(index).getDetail();

            int igdt_cnt = 0;
            int result_cnt = 0;


            StringTokenizer st = new StringTokenizer(detail, ",");
            while (st.hasMoreTokens()) {
                String tk = st.nextToken();
                //키워드 앞에 space 가 있을경우 삭제처리
                tk = tk.trim();

                //1,2 헥산디올 예외처리
                if (tk.equals("1") ) {
                    if(st.hasMoreTokens()){
                    String tk_next = st.nextToken();
                    if (tk_next.equals("2-헥산디올")) {
                        String[] strArr = {gdsArr.get(index).getId(), "005355", gdsArr.get(index).getName(),
                            "1,2-헥산디올", "1,2-Hexanediol"};
                        matchedList.add(strArr);

                        igdt_cnt++;
                        result_cnt++;

                    }
                    }

                }else {    //예외처리 제외한 keyword - 모든 전성분에 대하여 matching되는 키워드 검색

                    boolean ifmatch = false;

                    for (int j = 0; j < igdtArr.size(); j++) {


                        //found matching ingredient
                        if (tk.equals(igdtArr.get(j).getName())) {
                            String[] strArr = {gdsArr.get(index).getId(), igdtArr.get(j).getId(),
                                gdsArr.get(index).getName(), igdtArr.get(j).getName(),
                                igdtArr.get(j).getEn_name()};
                            matchedList.add(strArr);

                            igdt_cnt++;
                            result_cnt++;

                        ifmatch = true;
                        break;

                    }else if (tk.equals(igdtArr.get(j).getAsis_name())){
                            String[] strArr = {gdsArr.get(index).getId(), igdtArr.get(j).getId(),
                                gdsArr.get(index).getName(), igdtArr.get(j).getName(),
                                igdtArr.get(j).getEn_name()};
                            matchedList.add(strArr);

                            igdt_cnt++;
                            result_cnt++;

                            ifmatch = true;
                            break;
                        }

                }

                    //no matched ingredient
                if (!ifmatch) {

                        String[] strArr =
                            {gdsArr.get(index).getId(), gdsArr.get(index).getName(), tk};
                        unmatchedList.add(strArr);

                        igdt_cnt++;
                    }
                }

            }

            //goods 의 전성분 성공 / 실패 갯수 count 한 후 goods 에 업데이트 하기.
            con.updateProduct(igdt_cnt, result_cnt,gdsArr.get(index).getId());

            System.out.println("completed :" + index +" |||   total : " + gdsArr.size());

        }

        //db insert
       // int result = con.insertMatchKeyword(matchedList);


        //unmatched list print - download by excel
        ExcelExport ex = new ExcelExport();
        try {
            ex.excelExport_unmatched(unmatchedList);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        //matched list print - download by excel (생략)
//        ExcelExport ex2 = new ExcelExport();
//        try {
//            ex2.excelExport_matched(matchedList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }

}
