package financialness.semen.seyfullah.com.financialness;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import financialness.semen.seyfullah.com.financialness.Adapters.IncomeAdapter;
import financialness.semen.seyfullah.com.financialness.Entity.Income;
import financialness.semen.seyfullah.com.financialness.Listeners.NavigationListener;
import financialness.semen.seyfullah.com.financialness.ViewModels.IncomeViewModel;

public class IncomeListActivity extends AppCompatActivity implements NavigationListener {
    // This value will be used for debugging purposes.
    private static final String TAG = IncomeListActivity.class.getSimpleName();
    /*
     * I use Butterknife to bind to components in order to make use of them in the code.
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bottom_navigation_income_list_page)
    BottomNavigationView mBottomNavigationIncomeListPage;

    // Declare the variables we need.
    private List<Income> mIncomes;
    private IncomeAdapter mAdapter;
    private IncomeViewModel mIncomeViewModel;

    /*
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);
        ButterKnife.bind(this); // Bind the view so butterknife will know.

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mIncomes = new ArrayList<>();// Make a new instance of the incomes list

        mRecyclerView.setAdapter(mAdapter); // Set the adapter for the recyclerview

        mIncomeViewModel = new IncomeViewModel(getApplicationContext()); // Make a new instance of the viewmodel
        mIncomeViewModel.getAllIncome().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
                mIncomes = incomes;
                updateUI();// When new values have been added than the recyclerview will be notified.

                if (incomes.size() != 0) {
                    Log.i(TAG, "income: " + incomes.get(0).income);
                    Log.i(TAG, "income size: " + mIncomes.size());
                }
            }
        });
        handleSwipe(mAdapter, mRecyclerView); // Delete the income after swipe

        bottomNavigationClickListener();// listener for when somebody clicks on one of the buttons in the bottom navigation

    }

    /*
     * This will notify the recyclerview that there has been a change within the values
     */
    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new IncomeAdapter(mIncomes, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mIncomes);
        }
    }

    /*
     * When the back button has been pressed I want the user to go back to the income page.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(IncomeListActivity.this, IncomeActivity.class);
        IncomeListActivity.this.startActivity(intent);
        IncomeListActivity.this.finish();
    }

    /*
     * This will keep an eye on the click events which occur on the bottomnavigation.
     */
    @Override
    public void bottomNavigationClickListener() {
        mBottomNavigationIncomeListPage.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.income_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(IncomeListActivity.this, IncomeActivity.class);
                                        IncomeListActivity.this.startActivity(mainIntent);
                                        IncomeListActivity.this.finish();
                                    }
                                }, 500);
                                break;

                            case R.id.save_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(IncomeListActivity.this, SavingsActivity.class);
                                        IncomeListActivity.this.startActivity(mainIntent);
                                        IncomeListActivity.this.finish();
                                    }
                                }, 500);
                                break;

                            case R.id.home_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(IncomeListActivity.this, NewMainActivity.class);
                                        IncomeListActivity.this.startActivity(mainIntent);
                                        IncomeListActivity.this.finish();
                                    }
                                }, 500);
                                break;
                        }
                        return true;
                    }
                });
    }

    /**
     * If items are swiped left delete the item.
     *
     * @param mAdapter
     * @param mGeoRecyclerView
     */
    private void handleSwipe(final IncomeAdapter mAdapter, RecyclerView mGeoRecyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(
                    RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mIncomeViewModel.deleteIncome(mIncomes.get(position));
                Toast.makeText(
                        IncomeListActivity.this,
                        R.string.deleted_income,
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mGeoRecyclerView);
    }
}
