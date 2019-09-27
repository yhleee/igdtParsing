import Model.Goods;
import Model.Ingredient;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class MatchHandler {

    //미매칭 분류
    public static void parsing(ArrayList<Ingredient> igdtArr, ArrayList<Goods> gdsArr ) {

        for (int index = 0; index < gdsArr.size(); index++) {
            //상품 하나의 전성분 가져오기
            String detail = gdsArr.get(index).getDetail();

            System.out.println("-------------------------------------------");

            StringTokenizer st = new StringTokenizer(detail, ",");

            //st : 전체 전성분 텍스트
            while (st.hasMoreTokens()) {
                String tk = st.nextToken();
                tk = tk.trim();
                System.out.println("token for " + index + " ::: "+ tk);
                //1,2 헥산디올 예외처리
                if (tk.equals("1")) {
                    if (st.hasMoreTokens()) {
                        String tk_next = st.nextToken();
                        if (tk_next.equals("2-헥산디올")) {
                            String[] strArr =
                                {gdsArr.get(index).getId(), "005355", gdsArr.get(index).getName(),
                                    "1,2-헥산디올", "1,2-Hexanediol"};
                            Main.matchedList.add(strArr);

                        }
                    }
                } else {    //예외처리 제외한 keyword - 모든 전성분에 대하여 matching되는 키워드 검색
                    boolean ifmatch = false;
                    for (int j = 0; j < igdtArr.size(); j++) {

                        //found matching ingredient
                        if (tk.equals(igdtArr.get(j).getName())) {
                            String[] strArr_1 = {gdsArr.get(index).getId(), igdtArr.get(j).getId(),
                                gdsArr.get(index).getName(), igdtArr.get(j).getName(),
                                igdtArr.get(j).getEn_name()};
                            Main.matchedList.add(strArr_1);
                            ifmatch = true;
                            break;

                        } else if (tk.equals(igdtArr.get(j).getAsis_name())) {
                            String[] strArr_2 = {gdsArr.get(index).getId(), igdtArr.get(j).getId(),
                                gdsArr.get(index).getName(), igdtArr.get(j).getName(),
                                igdtArr.get(j).getEn_name()};
                            Main.matchedList.add(strArr_2);

                            //success table update - 전성분 && 아이템 매치
                            //con.insertMatchKeywordItem(strArr_2);

                            ifmatch = true;
                            break;
                        }

                    }

                    //no matched ingredient
                    if (!ifmatch) {

                        String[] strArr =
                            {gdsArr.get(index).getId(), gdsArr.get(index).getName(), tk};
                        Main.unmatchedList.add(strArr);

                    }
                }

            }

            //goods 의 전성분 성공 / 실패 갯수 count 한 후 goods 에 업데이트 하기.
            // con.updateProduct(igdt_cnt, result_cnt, gdsArr.get(index).getId());

            System.out.println("completed :" + index + " |||   total : " + gdsArr.size());

        }
    }



    public static void insertFailedList(ArrayList<Ingredient> igdtArr, ArrayList<Goods> gdsArr) {

        for (int index = 0; index < gdsArr.size(); index++) {
            //for (int index = 0; index < 10; index++) {
            String detail = gdsArr.get(index).getDetail();

            //분류에 걸리는 시간 감소
            StringTokenizer st = new StringTokenizer(detail, ",");

            //st : 전체 전성분 텍스트
            while (st.hasMoreTokens()) {
                String tk = st.nextToken();
                //키워드 앞에 space 가 있을경우 삭제처리
                tk = tk.trim();

                //1,2 헥산디올 예외처리
                if (tk.equals("1")) {
                    if (st.hasMoreTokens()) {
                        String tk_next = st.nextToken();
                        if (tk_next.equals("2-헥산디올")) {
                            String[] strArr =
                                {gdsArr.get(index).getId(), "005355", gdsArr.get(index).getName(),
                                    "1,2-헥산디올", "1,2-Hexanediol"};
                            Main.matchedList.add(strArr);
                            //con.insertMatchKeywordItem(strArr);

                        }
                    }

                } else {    //예외처리 제외한 keyword - 모든 전성분에 대하여 matching되는 키워드 검색
                    boolean ifmatch = false;
                    for (int j = 0; j < igdtArr.size(); j++) {

                        //found matching ingredient
                        if (tk.equals(igdtArr.get(j).getName())) {
                            String[] strArr_1 = {gdsArr.get(index).getId(), igdtArr.get(j).getId(),
                                gdsArr.get(index).getName(), igdtArr.get(j).getName(),
                                igdtArr.get(j).getEn_name()};
                            Main.matchedList.add(strArr_1);
                            // con.insertMatchKeywordItem(strArr_1);6
                            //success table update - 전성분 && 아이템 매치
                            ifmatch = true;
                            break;

                        } else if (tk.equals(igdtArr.get(j).getAsis_name())) {
                            String[] strArr_2 = {gdsArr.get(index).getId(), igdtArr.get(j).getId(),
                                gdsArr.get(index).getName(), igdtArr.get(j).getName(),
                                igdtArr.get(j).getEn_name()};
                            Main.matchedList.add(strArr_2);

                            //success table update - 전성분 && 아이템 매치
                            //con.insertMatchKeywordItem(strArr_2);

                            ifmatch = true;
                            break;
                        }

                    }

                    //no matched ingredient
                    if (!ifmatch) {

                        String[] strArr =
                            {gdsArr.get(index).getId(), gdsArr.get(index).getName(), tk};
                        Main.unmatchedList.add(strArr);

                    }
                }

            }

            //goods 의 전성분 성공 / 실패 갯수 count 한 후 goods 에 업데이트 하기.
            // con.updateProduct(igdt_cnt, result_cnt, gdsArr.get(index).getId());

            System.out.println("completed :" + index + " |||   total : " + gdsArr.size());

        }
    }



}


