package financialness.semen.seyfullah.com.financialness.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.SavingsSetAside;
import financialness.semen.seyfullah.com.financialness.Repository.SavingsRepository;

/*
 * Created by Seyfullah Semen on 16-12-2018.
 */
public class SavingsViewModel extends ViewModel {
    /*
     *
     */
    private SavingsRepository mSavingsRepository;
    private LiveData<List<SavingsSetAside>> mSetAside;

    /*
     * @param context
     */
    public SavingsViewModel(Context context) {
        mSavingsRepository = new SavingsRepository(context);
        mSetAside = mSavingsRepository.getAllSetAside();
    }

    /*
     * @return
     */
    public LiveData<List<SavingsSetAside>> getAllSetAside() {
        return mSetAside;
    }

    /*
     * @param setAside
     */
    public void insertNewSetAside(SavingsSetAside setAside) {
        mSavingsRepository.insertNewSetAside(setAside);
    }

}
