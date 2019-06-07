import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DBConnection {

    String server = "116.121.31.99";
    String database = "tablet_app";
    String user_name = "tabletAdmin";
    String port = "3306";
    String password = "1q2w3e4r%T";
    String url = "jdbc:mysql://" + server + ":"+port+"/" + database + "?serverTimezone=UTC";

    public DBConnection () throws SQLException{

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.err.println(" !! <JDBC Error> Driver load Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Ingredient> igdtSelect() throws SQLException {
        Connection con = null;
        Statement st = null;
        ArrayList<Ingredient> igdtArr = new ArrayList<>();


        try {

            con = DriverManager.getConnection(url,user_name,password);
            st = con.createStatement();

            String sql;
            sql = "SELECT * from gds_ingredient_info";

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
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


        } catch(SQLException e) {
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

            con = DriverManager.getConnection(url,user_name,password);
            st = con.createStatement();

            String sql;
            sql = "SELECT * from oliveone_gds_detail";

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                Goods gds = new Goods();
                gds.setId(rs.getString("gds_cd"));
                gds.setName(rs.getString("gds_nm"));
                gds.setDetail(rs.getString("gds_detail"));

                gdsArr.add(gds);
            }

            rs.close();
            st.close();

        } catch(SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            return gdsArr;
        }




    }

    public int insertMatchKeyword(ArrayList<String[]> matchedList) throws SQLException {
        Connection con = null;
        Statement st = null;
        PreparedStatement pstmt = null;
        int r=0;


        try {

            con = DriverManager.getConnection(url,user_name,password);
           ;

            String sql = "INSERT INTO oliveone_gds_ingredient (gds_cd, igdt_cd) values (?,?)";



            for(String[] strArr : matchedList){ //gds_cd : igdt_cd

                pstmt = con.prepareStatement(sql);



                pstmt.setString(1, strArr[0]);
                pstmt.setString(2, strArr[1]);

                 r+= pstmt.executeUpdate();


            }


        } catch(SQLException e) {
            System.err.println("DB Connection Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
            return r;

        }




    }

}