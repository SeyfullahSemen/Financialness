package financialness.semen.seyfullah.com.financialness.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.FetchIncomes;
import financialness.semen.seyfullah.com.financialness.Entity.Income;
import financialness.semen.seyfullah.com.financialness.Repository.IncomeRepository;


/*
 * Created by Seyfullah Semen on 16-12-2018.
 */
public class IncomeViewModel extends ViewModel {

    // Make new instances
    private IncomeRepository mIncomeRepository;
    private LiveData<List<Income>> mIncomes;
    private LiveData<List<FetchIncomes>> mOnlyIncomes;

    /*
     * @param context
     */
    public IncomeViewModel(Context context) {
        mIncomeRepository = new IncomeRepository(context);
        mIncomes = mIncomeRepository.getAllIncomes();
        mOnlyIncomes = mIncomeRepository.getIncomeOnly();
    }

    /*
     * @return income, id list
     */
    public LiveData<List<Income>> getAllIncome() {
        return mIncomes;
    }

    /*
     * @return income list
     */
    public LiveData<List<FetchIncomes>> getIncomesOnly() {
        return mOnlyIncomes;
    }

    /*
     * @param income
     */
    public void insertIncome(Income income) {
        mIncomeRepository.insert(income);
    }

    /*
     * @param income
     */
    public void deleteIncome(Income income) {
        mIncomeRepository.deleteIncome(income);
    }
}
