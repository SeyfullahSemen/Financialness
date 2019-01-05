package financialness.semen.seyfullah.com.financialness.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.SavingsSetAside;

/*
 * Created by Seyfullah Semen on 16-12-2018.
 */
@Dao
public interface SavingsSetAsideDao {

    @Query("SELECT * FROM SavingsSetAside")
    public LiveData<List<SavingsSetAside>> getAllSetAside();

    @Insert
    void insertNewSetAside(SavingsSetAside... setAside);

//    @Insert
//    void insertNewFinancialGoal(double financialGoal);
//
//    @Insert
//    void insertNewTotalSaved(double totalSaved);
//
//    @Update
//    void updateTotalSaved(double totalSaved);
//
//    @Update
//    void updateFinancialGoal(double financialGoal);
}
