package financialness.semen.seyfullah.com.financialness.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import financialness.semen.seyfullah.com.financialness.Dao.TotalSavedDao;
import financialness.semen.seyfullah.com.financialness.Database.AppDatabase;
import financialness.semen.seyfullah.com.financialness.Entity.TotalSaved;

/*
 * Created by Seyfullah Semen on 30-12-2018.
 */
public class TotalSavedRepository {

    private AppDatabase mAppDatabase;
    private TotalSavedDao mTotalSavedDao;
    private LiveData<List<TotalSaved>> mTotalSaved;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public TotalSavedRepository(Context context) {
        mAppDatabase = AppDatabase.getInstance(context);
        mTotalSavedDao = mAppDatabase.totalSavedDao();
        mTotalSaved = mTotalSavedDao.getTotalSaved();
    }

    public LiveData<List<TotalSaved>> getAllSetAside() {
        return mTotalSaved;
    }


    public void insertNewTotalSaved(final TotalSaved totalSaved) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTotalSavedDao.insertNewTotalSaved(totalSaved);
            }
        });
    }

    public void updateTotalSaved(final int id,final double totalSaved) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTotalSavedDao.updateCurrentSaved(id,totalSaved);
            }
        });
    }
}
