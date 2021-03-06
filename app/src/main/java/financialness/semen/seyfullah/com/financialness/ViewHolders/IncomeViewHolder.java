package financialness.semen.seyfullah.com.financialness.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import financialness.semen.seyfullah.com.financialness.R;

/*
 * Created by Seyfullah Semen on 1-1-2019.
 */
public class IncomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    /*
     * Make the instances of the views I will be using for the viewholder.
     */
    public final TextView mIncomeHeaderText;
    public final TextView mIncomeOfUser;

    /*
     * @param itemView
     */
    public IncomeViewHolder(@NonNull View itemView) {
        super(itemView);
        mIncomeHeaderText = itemView.findViewById(R.id.income_item_header);
        mIncomeOfUser = itemView.findViewById(R.id.income_of_user);
    }

    /*
     * @param view
     */
    @Override
    public void onClick(View view) {

    }
}
