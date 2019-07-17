package Model;

public class Ingredient {
    private String id;
    private String name;
    private String en_name;
    private String asis_name;
    private String asis_en_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getAsis_name() {
        return asis_name;
    }

    public void setAsis_name(String asis_name) {
        this.asis_name = asis_name;
    }

    public String getAsis_en_name() {
        return asis_en_name;
    }

    public void setAsis_en_name(String asis_en_name) {
        this.asis_en_name = asis_en_name;
    }
}
