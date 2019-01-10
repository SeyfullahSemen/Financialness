package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;

/*
 * Created by Seyfullah Semen on 4-1-2019.
 * This class will be used to make it possible to only fetch the incomes which are added
 * by the user.
 */
public class FetchIncomes {
    // Income column
    @ColumnInfo(name = "income")
    public double income;


}
