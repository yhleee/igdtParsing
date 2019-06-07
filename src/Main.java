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
            String detail = gdsArr.get(index).getDetail();

            ArrayList<String> leftList = new ArrayList<String>();

            StringTokenizer st = new StringTokenizer(detail, ", ");
            while (st.hasMoreTokens()) {
                String tk = st.nextToken();

                boolean ifmatch = false;

                for (int j = 0; j < igdtArr.size(); j++) {

                    //found matching ingredient
                    if (tk.equals(igdtArr.get(j).getName())) {
                        String[] strArr = {gdsArr.get(index).getId(), igdtArr.get(j).getId(), gdsArr.get(index).getName(), igdtArr.get(j).getName(), igdtArr.get(j).getEn_name()};
                        matchedList.add(strArr);

                        ifmatch = true;
                        break;

                    }

                }
                //no matched ingredient
                if(!ifmatch){

                    String[] strArr = {gdsArr.get(index).getId(), gdsArr.get(index).getName(), tk};
                    unmatchedList.add(strArr);
                }

            }

        }

        //db insert
        int result = con.insertMatchKeyword(matchedList);


        //unmatched & matched list print - download by excel
        ExcelExport ex = new ExcelExport();
        try {
            ex.excelExport_unmatched(unmatchedList, matchedList);
        } catch (IOException e) {
            e.printStackTrace();
        }






    }

}
