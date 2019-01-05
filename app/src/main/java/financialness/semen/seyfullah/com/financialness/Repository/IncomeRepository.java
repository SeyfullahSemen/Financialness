package financialness.semen.seyfullah.com.financialness.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import financialness.semen.seyfullah.com.financialness.Dao.IncomeDao;
import financialness.semen.seyfullah.com.financialness.Database.AppDatabase;
import financialness.semen.seyfullah.com.financialness.Entity.FetchIncomes;
import financialness.semen.seyfullah.com.financialness.Entity.Income;

/*
 * Created by Seyfullah Semen on 15-12-2018.
 */
public class IncomeRepository {

    private AppDatabase mAppDatabase;
    private IncomeDao mIncomeDao;
    private LiveData<List<Income>> mIncomes;
    private LiveData<List<FetchIncomes>> mIncomeOnly;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public IncomeRepository(Context context) {
        mAppDatabase = AppDatabase.getInstance(context);
        mIncomeDao = mAppDatabase.incomeDao();
        mIncomes = mIncomeDao.getAllIncome();
        mIncomeOnly = mIncomeDao.getOnlyIncomes();
    }

    public LiveData<List<Income>> getAllIncomes() {
        return mIncomes;
    }

    public LiveData<List<FetchIncomes>> getIncomeOnly() {
        return mIncomeOnly;
    }

    public void insert(final Income income) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mIncomeDao.insertNewIncome(income);
            }
        });
    }


}
