package financialness.semen.seyfullah.com.financialness.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.TotalSaved;

/*
 * Created by Seyfullah Semen on 30-12-2018.
 */
@Dao
public interface TotalSavedDao {
    // Select everything from the totalsaved table
    @Query("SELECT * FROM TotalSaved")
    public LiveData<List<TotalSaved>> getTotalSaved();
    // Insert a new totalsaved into the table
    @Insert
    void insertNewTotalSaved(TotalSaved... totalsaved);
    // Query to update the already set totalsaved value.
    @Query("UPDATE TotalSaved SET totalsaved= :totalsaved WHERE TotalSavedId = :id")
    void updateCurrentSaved(int id, double totalsaved);
}
