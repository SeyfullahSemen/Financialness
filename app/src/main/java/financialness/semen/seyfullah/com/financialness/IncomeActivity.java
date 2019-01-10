package financialness.semen.seyfullah.com.financialness;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import financialness.semen.seyfullah.com.financialness.Entity.Income;
import financialness.semen.seyfullah.com.financialness.Listeners.IsNetworkAvailableListener;
import financialness.semen.seyfullah.com.financialness.Listeners.NavigationListener;
import financialness.semen.seyfullah.com.financialness.Listeners.ObserveValuesListener;
import financialness.semen.seyfullah.com.financialness.ViewModels.IncomeViewModel;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class IncomeActivity extends AppCompatActivity implements IsNetworkAvailableListener, NavigationListener, ObserveValuesListener {

    private static final String TAG = IncomeActivity.class.getSimpleName();
    /*
     * Use butterknife to bind view components to the view.
     * These are all the components within the incomeactivity.
     */
    @BindView(R.id.bottom_navigation_income_page)
    BottomNavigationView mBottomNavigationIncomePage;
    @BindView(R.id.chart)
    LineChartView mChartView;
    @BindView(R.id.editTextIncome)
    EditText mEditTextIncome;
    @BindView(R.id.main_layout_income_activity)
    ConstraintLayout mMainLayoutIncomeActivity;
    @BindView(R.id.listIncomePageButton)
    FloatingActionButton mListIncomePageButton;

    // This List will be used in adding new values on the X axis on the axis of the chart.
    List<AxisValue> xAxisValue = new ArrayList<>();
    Axis axisX = new Axis();
    // Create a new instance of the LineChartData Class.
    private LineChartData data = new LineChartData();

    // Create two boolean values for the chart. This values will determine whether the chart will have axes and axes names.
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;

    // I use a hashmap because the data I get from firestore is in JSON format
    private Map<String, Double> income = new HashMap<>();
    // In order to do something with firestore, you need to make an instance.
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Initialize the IncomeViewmodel to add Room functionalities.
    private IncomeViewModel mIncomeViewModel;
    // List to hold the data which will be retrieved from the local Database.
    private List<Income> mIncomes;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this); // Use Butterknife to bind the view in order to use the components inside the view
        mIncomes = new ArrayList<>(); // make new income list array list
        mIncomeViewModel = new IncomeViewModel(getApplicationContext());
        getLastIncome(); // This function will get the latest income that a user has gotten
        bottomNavigationClickListener(); // Click events for the bottom navigation are inside this function
        observeNewAddedValues();// When a new value has been added, the
        generateData(); // this will fill the chart with values
    }

    /*
     * When the user clicks he or she will go to the page where he or she
     * can see there latest income and so on.
     */
    @OnClick(R.id.listIncomePageButton)
    public void goToListIncomePage() {
        Intent intent = new Intent(IncomeActivity.this, IncomeListActivity.class);
        IncomeActivity.this.startActivity(intent);
        IncomeActivity.this.finish();
    }

    /*
     * With the help of butterknife we can just add the ID of the button
     * to the OnClick annotation and we are ready to go.
     *
     * This button click will add a new income to the database if the income
     * field is not empty.
     */
    @OnClick(R.id.buttonSave)
    public void saveNewIncome() {
        String newIncomeString = mEditTextIncome.getText().toString();
        try {
            if (!newIncomeString.equals("")) {
                double newIncome = Double.valueOf(newIncomeString);
                addNewIncomeWithRoom(newIncome);
                addNewIncome(newIncome);
                Toast.makeText(
                        IncomeActivity.this,
                        getResources().getString(R.string.income_has_been_added),
                        Toast.LENGTH_SHORT
                ).show();
                mEditTextIncome.setText("");
            } else {
                Toast.makeText(
                        IncomeActivity.this,
                        getResources().getString(R.string.vul_een_inkomen_in),
                        Toast.LENGTH_SHORT
                ).show();
                mEditTextIncome.setText("");
            }
        } catch (Exception ex) {
            Log.i(TAG, getResources().getString(R.string.general_error_message) + ex.getMessage());
        }
    }

    /*
     * With this function the chart inside the income activity will be filled
     * with income data which will be retrieved from the local database.
     */
    private void generateData() {
        final List<PointValue> values = new ArrayList<>();
        final List<Line> lines = new ArrayList<>();
        mIncomeViewModel.getAllIncome().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> fetchIncomes) {
                mIncomes = fetchIncomes;

                assert mIncomes != null;
                if (mIncomes.size() != 0) {
                    Log.i(TAG, "onChanged: " + mIncomes.get(0).income);
                    for (int i = 0; i < mIncomes.size(); i++) {
                        String income = String.valueOf(mIncomes.get(i).income);
                        float incomeFloat = Float.parseFloat(income);
                        values.add(new PointValue(i, incomeFloat));
                        xAxisValue.add(new AxisValue(i + 1));
                    }
                    Line line = new Line(values).setColor(getResources().getColor(R.color.blue1)).setCubic(true);
                    line.setPointRadius(5);
                    line.setHasLabelsOnlyForSelected(true);
                    lines.add(line);

                    data.setLines(lines);

                    if (hasAxes) {
                        axisX.setValues(xAxisValue);
                        Axis axisY = new Axis().setHasLines(true);
                        axisY.setTextSize(9);
                        axisX.setTextSize(9);
                        if (hasAxesNames) {
                            axisX.setName("");
                        }
                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
                    } else {
                        data.setAxisXBottom(null);
                        data.setAxisYLeft(null);
                    }

                    data.setBaseValue(Float.NEGATIVE_INFINITY);
                    mChartView.setLineChartData(data);
                }
            }
        });


        mChartView.setInteractive(true);
        mChartView.setValueSelectionEnabled(true);


    }

    /*
     * When the user presses back, I want the user to go back to
     * the homepage.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(IncomeActivity.this, NewMainActivity.class);
        IncomeActivity.this.startActivity(intent);
        IncomeActivity.this.finish();
    }

    /*
     * This function will get the latest added income from firestore,
     * but if there is no internet connection than it will be retrieved from the
     * Room local database.
     */
    private void getLastIncome() {
        if (networkAvailable()) {
            db.collection("user_income")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + Double.valueOf(document.getData().get("income").toString()));
                                }
                            } else {
                                Log.w(TAG, getResources().getString(R.string.general_error_message), task.getException());
                            }
                        }
                    });
        } else {
            if (mIncomes.size() != 0) {
                Log.i(TAG, "income: " + mIncomes.get(0).income);
                Log.i(TAG, "income size: " + mIncomes.size());
            }
        }

    }

    /*
     * If there is network available, than the new value will be added into the local database.
     * @param newIncome
     */
    private void addNewIncome(final double newIncome) {
        if (networkAvailable()) {
            income.put("income", newIncome);
            db.collection("user_income").document("income")
                    .set(income)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, getResources().getString(R.string.general_error_message), e);
                        }
                    });
        } else {
            addNewIncomeWithRoom(newIncome);
        }
    }

    /*
     *  this function will take control over adding a new income into the local database.
     *  @param income
     */
    private void addNewIncomeWithRoom(double income) {
        try {
            mIncomeViewModel.insertIncome(new Income(income));
        } catch (Exception ex) {
            Log.d(TAG, getResources().getString(R.string.general_error_message) + ex.getMessage());
        }
    }

    /*
     *
     * @return if there is internet connection or not.
     */
    @Override
    public boolean networkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
     * This function will check whether there is a click event in the bottom navigation.
     */
    @Override
    public void bottomNavigationClickListener() {
        mBottomNavigationIncomePage.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.save_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(IncomeActivity.this, SavingsActivity.class);
                                        IncomeActivity.this.startActivity(mainIntent);
                                        IncomeActivity.this.finish();
                                    }
                                }, 500);
                                break;

                            case R.id.home_page:
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Intent mainIntent = new Intent(IncomeActivity.this, NewMainActivity.class);
                                        IncomeActivity.this.startActivity(mainIntent);
                                        IncomeActivity.this.finish();
                                    }
                                }, 500);
                                break;
                        }
                        return true;
                    }
                });
    }

    /*
     * This function will keep an eye on newly added incomes
     * and immediately update the database.
     */
    @Override
    public void observeNewAddedValues() {

        mIncomeViewModel.getAllIncome().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
                mIncomes = incomes;

                if (incomes.size() != 0) {
                    swapList(mIncomes);
                    Log.i(TAG, "income: " + incomes.get(0).income);
                    Log.i(TAG, "income size: " + mIncomes.size());
                }
            }
        });
    }

    /*
     * Update the List of incomes after changes have been made
     * @param newList
     */
    public void swapList(List<Income> newList) {
        mIncomes = newList;
    }
}
