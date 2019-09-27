package Utils;

import Model.Goods;
import Model.Ingredient;
import Model.Keyword;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DBConnection {

    String server = "git.oliveyoung.co.kr";
    String database = "tablet_app";
    String user_name = "tabletAdmin";
    String port = "3306";
    String password = "1q2w3e4r%T";
    String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?serverTimezone=UTC";

    public DBConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println(" !! <JDBC Error> Driver load Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> selectFailedKeywords() throws SQLException {
        Connection con = null;
        Statement st1 = null;
        Statement st2 = null;
        PreparedStatement pstmt1 = null;
        ArrayList<String[]> resultArray = new ArrayList<>();


        try {

            con = DriverManager.getConnection(url, user_name, password);
            st1 = con.createStatement();

            String sql;
            sql = "SELECT * from igdt_keyword_matched";

            ResultSet rs1 = st1.executeQuery(sql);

            while (rs1.next()) {
                String str_keyword = rs1.getString("keyword_cd");
                String str_igdt = rs1.getString("igdt_cd");


                String sql2 = "SELECT * from igdt_gds_keyword WHERE keyword_cd = ? ";
                pstmt1 = con.prepareStatement(sql2);
                pstmt1.setString(1, str_keyword);

                ResultSet rs2 = pstmt1.executeQuery();
                ArrayList<String> strArr = new ArrayList<>();

                //keyword 를 가지고있는  gds 가져오기
                while(rs2.next()){
                     strArr.add(rs2.getString("gds_cd"));
                }

                int len = strArr.size();

                for(int i=0;i<len;i++){
                    String [] temp = new String[2];
                    temp[0] = strArr.get(i);
                    temp[1] = str_igdt;
                    System.out.println(temp[0]+ " "+temp[1]);
                    resultArray.add(temp);
                }

                //gds <> 미매칭 - status 변경해주기
                //inserted?
                for(int i=0;i<len;i++){
                String sql3 = "UPDATE igdt_gds_keyword SET useYN = 'N'  WHERE keyword_cd = ? and gds_cd =?";
                PreparedStatement pstmt2 = con.prepareStatement(sql3);
                pstmt2.setString(1, str_keyword);
                pstmt2.setString(2, strArr.get(i));
                pstmt2.executeUpdate();
                pstmt2.close();
                }
                rs2.close();
            }

            rs1.close();
            pstmt1.close();

            st1.close();


        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            return resultArray;
        }


    }


    public ArrayList<Keyword> keywordSelect() throws SQLException {
        Connection con = null;
        Statement st = null;
        ArrayList<Keyword> keywordArray = new ArrayList<>();


        try {

            con = DriverManager.getConnection(url, user_name, password);
            st = con.createStatement();

            String sql;
            sql = "SELECT * from igdt_gds_keyword WHERE keyword_nm like '%[]%' and length (keyword_nm) > 200" ;

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Keyword keyword = new Keyword();
                keyword.setGds_cd(rs.getString("gds_cd"));
                keyword.setGds_nm(rs.getString("gds_nm"));
                keyword.setKeyword_cd(rs.getString("keyword_cd"));
                keyword.setKeyword_nm(rs.getString("keyword_nm"));

                keywordArray.add(keyword);
            }

            rs.close();
            st.close();


        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            return keywordArray;
        }


    }





    public void insertUnmatched(ArrayList<String[]> unmatchedList) throws SQLException {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt3 = null;
        try {
            con = DriverManager.getConnection(url, user_name, password);

            Iterator<String[]> iterator = unmatchedList.iterator();
            while (iterator.hasNext()) {
                String[] unmatched = iterator.next();
                String sql_keyword = "SELECT * from igdt_keyword_list WHERE keyword_nm = ?";
                String keyword_cd = "";

                pstmt1 = con.prepareStatement(sql_keyword);
                pstmt1.setString(1, unmatched[2]);

                ResultSet rs1 = pstmt1.executeQuery();

                if (!rs1.next()) {
                    //keyword 존재하지않음 -> insert
                    String sql = "INSERT INTO igdt_keyword_list (keyword_nm) values (?)";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, unmatched[2]);
                    pstmt.executeUpdate();
                    pstmt.close();
                }
                rs1.close();

                ResultSet rs2 = pstmt1.executeQuery();

                //keyword 존재 -> return
                if (!rs2.next()) {

                }else{
                    keyword_cd = rs2.getString("keyword_cd");
                }

                String sql =
                    "INSERT INTO igdt_gds_keyword (gds_cd, gds_nm, keyword_cd, keyword_nm) values (?,?,?,?)";
                pstmt3 = con.prepareStatement(sql);

                    pstmt3.setString(1, unmatched[0]);
                    pstmt3.setString(2, unmatched[1]);
                    pstmt3.setString(3, keyword_cd);
                    pstmt3.setString(4, unmatched[2]);

                pstmt3.executeUpdate();

                pstmt3.close();
                pstmt1.close();
                rs2.close();

            }

        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();


        }

    }


    public ArrayList<Ingredient> igdtSelect() throws SQLException {
        Connection con = null;
        Statement st = null;
        ArrayList<Ingredient> igdtArr = new ArrayList<>();


        try {

            con = DriverManager.getConnection(url, user_name, password);
            st = con.createStatement();

            String sql;
            sql = "SELECT * from igdt_gds_ingredient_info";

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Ingredient igdt = new Ingredient();
                igdt.setId(rs.getString("igdt_cd"));
                igdt.setName(rs.getString("igdt_nm"));
                igdt.setEn_name(rs.getString("igdt_en_nm"));
                igdt.setAsis_name(rs.getString("igdt_asis_nm"));
                igdt.setAsis_en_name(rs.getString("igdt_asis_en_nm"));

                igdtArr.add(igdt);
            }

            rs.close();
            st.close();


        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            return igdtArr;
        }


    }

    public ArrayList<Goods> gdsSelect() throws SQLException {
        Connection con = null;
        Statement st = null;
        ArrayList<Goods> gdsArr = new ArrayList<>();


        try {

            con = DriverManager.getConnection(url, user_name, password);
            st = con.createStatement();

            String sql;
            //sql = "SELECT * from oliveone_gds_detail";
            sql = "SELECT * from igdt_oliveone_gds_detail_last";

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Goods gds = new Goods();
                gds.setId(rs.getString("gds_cd"));
                gds.setName(rs.getString("gds_nm"));
                gds.setDetail(rs.getString("gds_detail"));

                gdsArr.add(gds);
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();

            return gdsArr;
        }

    }

    public int insertMatchKeywordItem(String[] item) throws SQLException {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstmt = null;
        int r = 0;


        try {

            con = DriverManager.getConnection(url, user_name, password);


            String sql = "INSERT INTO igdt_oliveone_gds_ingredient (gds_cd, igdt_cd) values (?,?)";

            String[] strArr = item;

            pstmt = con.prepareStatement(sql);



            pstmt.setString(1, strArr[0]);
            pstmt.setString(2, strArr[1]);

            r += pstmt.executeUpdate();


        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            pstmt.close();
            return r;

        }



    }

    public int insertMatchKeyword(ArrayList<String[]> matchedList) throws SQLException {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstmt = null;
        int r = 0;


        try {

            con = DriverManager.getConnection(url, user_name, password);
            ;

            String sql = "INSERT INTO igdt_gds_ingredient_matched (gds_cd, igdt_cd) values (?,?)";


            for (String[] strArr : matchedList) { //gds_cd : igdt_cd

                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, strArr[0]);
                pstmt.setString(2, strArr[1]);

                r += pstmt.executeUpdate();

            }


        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            pstmt.close();
            return r;

        }



    }


    public void updateProduct(int igdt_cnt, int result_cnt, String gds_cd) throws SQLException {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstmt = null;



        try {

            con = DriverManager.getConnection(url, user_name, password);
            ;

            String sql =
                "UPDATE igdt_oliveone_gds_detail_last SET igdt_cnt_last = ?, result_cnt_last = ? WHERE gds_cd = ?";



            pstmt = con.prepareStatement(sql);



            pstmt.setString(1, igdt_cnt + "");
            pstmt.setString(2, result_cnt + "");
            pstmt.setString(3, gds_cd);

            pstmt.executeUpdate();



        } catch (SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            pstmt.close();


        }



    }

}
