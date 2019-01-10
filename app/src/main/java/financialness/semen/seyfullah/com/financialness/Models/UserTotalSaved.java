package financialness.semen.seyfullah.com.financialness.Models;

/*
 * Created by Seyfullah Semen on 30-12-2018.
 *
 * This is the model class for how much money the user has saved in total
 */
public class UserTotalSaved {

    private double totalSaved;

    public UserTotalSaved(double totalSaved) {
        this.totalSaved = totalSaved;
    }

    public double getTotalSaved() {
        return totalSaved;
    }

    public void setTotalSaved(double totalSaved) {
        this.totalSaved = totalSaved;
    }
}
