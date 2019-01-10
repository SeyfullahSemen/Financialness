package financialness.semen.seyfullah.com.financialness.Models;

/*
 * Created by Seyfullah Semen on 29-11-2018.
 *
 * This is the model class for the income.
 */
public class UserIncome {
    private double income;

    public UserIncome(double income) {
        this.income = income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getIncome() {
        return this.income;
    }
}
