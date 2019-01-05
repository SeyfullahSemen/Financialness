package financialness.semen.seyfullah.com.financialness.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.Entity.Income;
import financialness.semen.seyfullah.com.financialness.R;
import financialness.semen.seyfullah.com.financialness.ViewHolders.IncomeViewHolder;

/*
 * Created by Seyfullah Semen on 1-1-2019.
 */
public class IncomeAdapter extends RecyclerView.Adapter<IncomeViewHolder> {

    private List<Income> mIncomes;
    private Context mContext;

    public IncomeAdapter(List<Income> mIncomes, Context mContext) {
        this.mIncomes = mIncomes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.income_item, viewGroup, false);

        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder viewHolder, int i) {
        Income income = mIncomes.get(i);

        viewHolder.mIncomeHeaderText.setText("Income: ");
        viewHolder.mIncomeOfUser.setText(String.valueOf(income.income));
    }

    @Override
    public int getItemCount() {
        return mIncomes.size();
    }

    public void swapList(List<Income> newList) {
        mIncomes = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }
}
