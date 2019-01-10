package financialness.semen.seyfullah.com.financialness;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import financialness.semen.seyfullah.com.financialness.Entity.SavingsSetAside;
import financialness.semen.seyfullah.com.financialness.Entity.TotalSaved;
import financialness.semen.seyfullah.com.financialness.Listeners.NavigationListener;
import financialness.semen.seyfullah.com.financialness.Listeners.ObserveValuesListener;
import financialness.semen.seyfullah.com.financialness.ViewModels.SavingsViewModel;
import financialness.semen.seyfullah.com.financialness.ViewModels.TotalSavedViewModel;

public class SavingsActivity extends AppCompatActivity implements NavigationListener, ObserveValuesListener {
    private static final String TAG = SavingsActivity.class.getSimpleName();
    /*
     * Declare all the variables made in the
     * layout file so we can use them in this file to edit them.
     */
    @BindView(R.id.bottom_navigation_savings_page)
    BottomNavigationView mBottomNavigationSavingsPage;
    @BindView(R.id.linearLayoutInputFields)
    LinearLayout mLinearLayoutInputFields;
    @BindView(R.id.LastIncomeEdit)
    EditText mLastIncomeEdit;
    @BindView(R.id.setAsideEditText)
    EditText mSetAsideEditText;
    @BindView(R.id.SaveSetAside)
    Button mSaveSetAside;
    @BindView(R.id.main_layout_savings_activity)
    ConstraintLayout mMainlayoutSavingsActivity;
    @BindView(R.id.totalsavedText)
    TextView mTotalSavedText;
    /*
     * Create two HashMaps. The reason I do this is
     * because I use firestore to store some values.
     * This database returns a JSON structure.
     */
    private Map<String, Float> savings = new HashMap<>();
    private Map<String, Float> income = new HashMap<>();
    // Create a new instance of the firestore.
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create new instances of the viewmodels
    private SavingsViewModel mSavingsViewModel;
    private TotalSavedViewModel mTotalSavedViewModel;
    // Create lists in which the newly added values will be added to.
    private List<SavingsSetAside> mSavingsSetASide;
    private List<TotalSaved> mTotalSaved;
    // Variable to save the last income. This value will be retrieved from firestore.
    private double lastIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);
        ButterKnife.bind(this); // In order to make Butterknife work, I need to add the bind() method.
        mTotalSaved = new ArrayList<>(); // Make new instance of the list
        mSavingsSetASide = new ArrayList<>(); // Make a new instance of the list.
        getLastIncome(); // Method which will get the last income. This value will come from the firestore.
        bottomNavigationClickListener(); // Method to survey the clicks on the bottomNavigation.
        mSavingsViewModel = new SavingsViewModel(getApplicationContext()); // Make new instance of the SavingsViewModel.
        mTotalSavedViewModel = new TotalSavedViewModel(getApplicationContext()); // make a new instance of the totalsavedviewmodel
        observeNewAddedValues(); // Observe newly added values.
        mTotalSavedViewModel.getAllTotalSaved().observe(this, new Observer<List<TotalSaved>>() {
            @Override
            public void onChanged(@Nullable List<TotalSaved> totalSaveds) {
                mTotalSaved = totalSaveds;
                mTotalSavedText.setText("" + mTotalSaved.get(mTotalSaved.size() - 1).totalsaved);

            }
        });


    }

    /*
     *  This function will add a new amount that a user wants
     *  to set aside. It will check if the the edittext field
     *  is not empty. If it is not empty than a new value will be added.
     */
    @OnClick(R.id.SaveSetAside)
    public void saveNewSetAside() {
        String newSetAsideString = mSetAsideEditText.getText().toString();
        if (!newSetAsideString.equals("")) { // Check if the edittext is not empty
            double newIncome = Double.valueOf(newSetAsideString); // change the string to a double.
            addNewMonthlySaving(newIncome); // call the function to add the new value.
        } else {
            Toast.makeText(this, getString(R.string.voer_een_bedrag_in), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * This function will make sure to add a newly added setaside
     * So this function will make sure that the user can see how much a user has already saved.
     * This function will be used with the financialgoal of the user.
     *
     * @param addNewSaving
     */
    @SuppressLint("SetTextI18n")
    private void addToTheCurrentSavings(final double addNewSaving) {
        try {
            mTotalSavedViewModel.getAllTotalSaved().observe(this, new Observer<List<TotalSaved>>() {
                @Override
                public void onChanged(@Nullable List<TotalSaved> totalSaveds) {
                    mTotalSaved = totalSaveds;
                    swapTotalSavedList(totalSaveds);

                }
            });
            if (mTotalSaved.size() == 0) {
                mTotalSavedViewModel.insertTotalSaved(new TotalSaved(addNewSaving));
            } else {
                double calculation = mTotalSaved.get(mTotalSaved.size() - 1).totalsaved + addNewSaving;
                Log.i(TAG, "addToTheCurrentSavings: " + mTotalSaved.get(mTotalSaved.size() - 1).totalsaved);
                int id = mTotalSaved.get(mTotalSaved.size() - 1).TotalSavedId;
                mTotalSavedViewModel.updateTotalSaved(id, calculation);
                mTotalSavedText.setText("" + mTotalSaved.get(mTotalSaved.size() - 1).totalsaved);
            }
        } catch (Exception ex) {
            Log.i(TAG, "There is something wrong with adding up new set aside value:  " + ex.getMessage());
        }

    }

    /*
     * This function will have all the checks that is needed in order to add a new
     * amount that the user wants to set aside. This method will check if the
     * amount is higher than the last income. If this is the case, than a snackbar will be shown
     * that the amount can not be set.
     * @param setAside
     */
    private void addNewMonthlySaving(double setAside) {
        try {
            if (setAside > getLastIncome()) {
                Snackbar snackbar = Snackbar.make(mMainlayoutSavingsActivity, R.string.nieuw_bedrag_aan_de_kant_gezet_error, Snackbar.LENGTH_LONG);
                snackbar.show();
                mSetAsideEditText.setText("");
            } else {
                mSavingsViewModel.insertNewSetAside(new SavingsSetAside(setAside));
                addToTheCurrentSavings(setAside);
                Snackbar snackbar = Snackbar.make(mMainlayoutSavingsActivity, R.string.nieuw_bedrag_aan_de_kant_gezet, Snackbar.LENGTH_LONG);
                snackbar.show();
                mSetAsideEditText.setText("");
            }
        } catch (Exception ex) {
            Log.i(TAG, getString(R.string.exception_error_message) + ex.getMessage());
        }
    }


    /*
     * This function will return the latest added income. This value will be retrieved from the firestore DB.
     * @return lastIncome
     */
    private double getLastIncome() {
        db.collection("user_income")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) { // Check if the task is successfully achieved.
                            for (QueryDocumentSnapshot document : task.getResult()) { // Loop through the documents in firestore
                                Log.d(TAG, document.getId() + " => " + Double.valueOf(document.getData().get("income").toString()));
                                mLastIncomeEdit.setText("€ " + Double.valueOf(document.getData().get("income").toString()) + " ,-");
                                lastIncome = Double.valueOf(document.getData().get("income").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return lastIncome;
    }

    /*
     * This function will have an eye on the newly added financialgoals and
     * newly added amount to set aside.
     */
    @Override
    public void observeNewAddedValues() {
        /*
         * This function will check if a new set aside is filled in
         * if there is some new value than the list will be notified.
         */
        mSavingsViewModel.getAllSetAside().observe(this, new Observer<List<SavingsSetAside>>() {
            @Override
            public void onChanged(@Nullable List<SavingsSetAside> asides) {
                mSavingsSetASide = asides;
                swapList(asides);
            }
        });

        /*

        /*
         * This one will check if there is a change in the totalsaved value.
         */
        mTotalSavedViewModel.getAllTotalSaved().observe(this, new Observer<List<TotalSaved>>() {
            @Override
            public void onChanged(@Nullable List<TotalSaved> totalSaveds) {
                mTotalSaved = totalSaveds;
                swapTotalSavedList(totalSaveds);

            }
        });
    }

    /*
     * When the user presses back, than I want the user to be able to the main page.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SavingsActivity.this, NewMainActivity.class);
        SavingsActivity.this.startActivity(intent);
        SavingsActivity.this.finish();
    }

    /*
     * This function is responsible of the selections that are made on the bottomNavigation.
     * When the user clicks on home in the BottomNavigation, than this function will know about it
     * and go to the homepage again.
     */
    @Override
    public void bottomNavigationClickListener() {

        mBottomNavigationSavingsPage.setSelectedItemId(R.id.save_page);
        mBottomNavigationSavingsPage.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.income_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(SavingsActivity.this, IncomeActivity.class);
                                        SavingsActivity.this.startActivity(mainIntent);
                                        SavingsActivity.this.finish();
                                    }
                                }, 500);
                                break;

                            case R.id.home_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(SavingsActivity.this, NewMainActivity.class);
                                        SavingsActivity.this.startActivity(mainIntent);
                                        SavingsActivity.this.finish();
                                    }
                                }, 500);
                                break;
                        }
                        return true;
                    }
                });

    }

    /*
     * @param newList
     */
    public void swapList(List<SavingsSetAside> newList) {
        if (mSavingsSetASide.size() != 0) {
            Log.i(TAG, "swapList: " + mSavingsSetASide.size());
        }
        mSavingsSetASide = newList;
    }

    /*
     * @param newList
     * @return
     */
    public List<TotalSaved> swapTotalSavedList(List<TotalSaved> newList) {
        if (mTotalSaved.size() != 0) {
            Log.i(TAG, "swapTotalSavedList: " + mTotalSaved.size());
        }
        mTotalSaved = newList;
        return mTotalSaved;
    }
}
