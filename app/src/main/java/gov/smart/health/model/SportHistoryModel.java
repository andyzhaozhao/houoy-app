package gov.smart.health.model;

/**
 * Created by laoniu on 2017/07/26.
 */

public class SportHistoryModel {
    private String sportDate;
    private String sportCal;

    public SportHistoryModel(String sportDate, String sportCal) {
        this.sportDate = sportDate;
        this.sportCal = sportCal;
    }

    public String getSportDate() {
        return sportDate;
    }

    public void setSportDate(String sportDate) {
        this.sportDate = sportDate;
    }

    public String getSportCal() {
        return sportCal;
    }

    public void setSportCal(String sportCal) {
        this.sportCal = sportCal;
    }
}
