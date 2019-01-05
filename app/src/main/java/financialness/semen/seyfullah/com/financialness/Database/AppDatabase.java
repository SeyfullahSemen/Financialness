package financialness.semen.seyfullah.com.financialness.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import financialness.semen.seyfullah.com.financialness.Dao.IncomeDao;
import financialness.semen.seyfullah.com.financialness.Dao.SavingsSetAsideDao;
import financialness.semen.seyfullah.com.financialness.Dao.TotalSavedDao;
import financialness.semen.seyfullah.com.financialness.Entity.Income;
import financialness.semen.seyfullah.com.financialness.Entity.SavingsSetAside;
import financialness.semen.seyfullah.com.financialness.Entity.TotalSaved;

/*
 * Created by Seyfullah Semen on 15-12-2018.
 */
@Database(entities = {Income.class, SavingsSetAside.class,TotalSaved.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IncomeDao incomeDao();

    public abstract SavingsSetAsideDao savingsDao();

    public abstract TotalSavedDao totalSavedDao();

    private static AppDatabase sInstance;

    private final static String NAME_DATABASE = "boost_db";

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, NAME_DATABASE).build();
        }
        return sInstance;
    }
}
