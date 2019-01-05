package financialness.semen.seyfullah.com.financialness.ViewModels;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.TotalSaved;
import financialness.semen.seyfullah.com.financialness.Repository.TotalSavedRepository;

/*
 * Created by Seyfullah Semen on 30-12-2018.
 */
public class TotalSavedViewModel {

    private TotalSavedRepository mTotalRepository;
    private LiveData<List<TotalSaved>> mTotalSaved;

    public TotalSavedViewModel(Context context) {
        mTotalRepository = new TotalSavedRepository(context);
        mTotalSaved = mTotalRepository.getAllSetAside();
    }

    public LiveData<List<TotalSaved>> getAllTotalSaved() {
        return mTotalSaved;
    }


    public void insertTotalSaved(TotalSaved totalSaved) {
        mTotalRepository.insertNewTotalSaved(totalSaved);
    }

    public void updateTotalSaved(int id, double totalSaved) {
        mTotalRepository.updateTotalSaved(id, totalSaved);
    }
}
