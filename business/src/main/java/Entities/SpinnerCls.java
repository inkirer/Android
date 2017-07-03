package Entities;

/**
 * Created by alvaro on 25/11/2015.
 */
public class SpinnerCls {

    private int databaseId;
    private String databaseValue;

    public SpinnerCls(int databaseId, String databaseValue) {
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    public int getId() {
        return databaseId;
    }

    public String getValue() {
        return databaseValue;
    }

    @Override
    public String toString() {
        return databaseValue;
    }
}
