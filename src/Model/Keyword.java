package Model;

public class Keyword {


    private String gds_cd;
    private String gds_nm;
    private String keyword_cd;
    private String keyword_nm;

    public void setKeyword_cd(String keyword_cd){
        this.keyword_cd = keyword_cd;
    }
    public void setKeyword_nm(String keyword_nm){
        this.keyword_nm = keyword_nm;
    }
    public void setGds_cd(String gds_cd){
        this.gds_cd = gds_cd;
    }
    public void setGds_nm(String gds_nm){
        this.gds_nm = gds_nm;
    }


    public String getKeyword_cd(){
        return this.keyword_cd;
    }


    public String getKeyword_nm(){
        return this.keyword_nm;
    }
    public String getGds_cd(){
        return this.gds_cd;
    }

    public String getGds_nm(){
        return this.gds_nm;
    }


}

