package financialness.semen.seyfullah.com.financialness.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.FetchIncomes;
import financialness.semen.seyfullah.com.financialness.Entity.Income;

/*
 * Created by Seyfullah Semen on 15-12-2018.
 */
@Dao
public interface IncomeDao {
    // Select everything there is from the Database.
    @Query("SELECT * FROM Income")
    public LiveData<List<Income>> getAllIncome();
    // Only fetch the incomes and not the id's
    @Query("SELECT income FROM Income")
    LiveData<List<FetchIncomes>> getOnlyIncomes();
    // Insert new Income into the Database.
    @Insert
    void insertNewIncome(Income... income);
}
