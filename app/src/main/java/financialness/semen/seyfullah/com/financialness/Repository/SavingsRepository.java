package financialness.semen.seyfullah.com.financialness.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import financialness.semen.seyfullah.com.financialness.Dao.SavingsSetAsideDao;
import financialness.semen.seyfullah.com.financialness.Database.AppDatabase;
import financialness.semen.seyfullah.com.financialness.Entity.SavingsSetAside;

/*
 * Created by Seyfullah Semen on 16-12-2018.
 */
public class SavingsRepository {

    private AppDatabase mAppDatabase;
    private SavingsSetAsideDao mSavingsSetAsideDao;
    private LiveData<List<SavingsSetAside>> mSetAside;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public SavingsRepository(Context context) {
        mAppDatabase = AppDatabase.getInstance(context);
        mSavingsSetAsideDao = mAppDatabase.savingsDao();
        mSetAside = mSavingsSetAsideDao.getAllSetAside();
    }

    public LiveData<List<SavingsSetAside>> getAllSetAside() {
        return mSetAside;
    }


    public void insertNewSetAside(final SavingsSetAside setAside) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mSavingsSetAsideDao.insertNewSetAside(setAside);
            }
        });
    }


}
