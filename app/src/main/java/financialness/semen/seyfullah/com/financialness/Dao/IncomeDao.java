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

    @Query("SELECT * FROM Income")
    public LiveData<List<Income>> getAllIncome();

    @Query("SELECT income FROM Income")
    LiveData<List<FetchIncomes>> getOnlyIncomes();

    @Insert
    void insertNewIncome(Income... income);
}
