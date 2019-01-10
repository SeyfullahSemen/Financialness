package financialness.semen.seyfullah.com.financialness;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import financialness.semen.seyfullah.com.financialness.Entity.FetchIncomes;
import financialness.semen.seyfullah.com.financialness.Entity.Income;
import financialness.semen.seyfullah.com.financialness.Listeners.IsNetworkAvailableListener;
import financialness.semen.seyfullah.com.financialness.Listeners.ObserveValuesListener;
import financialness.semen.seyfullah.com.financialness.ViewModels.IncomeViewModel;
import financialness.semen.seyfullah.com.financialness.ViewModels.SavingsViewModel;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class NewMainActivity extends AppCompatActivity implements IsNetworkAvailableListener, ObserveValuesListener {
    private static final String TAG = NewMainActivity.class.getSimpleName();
    /*
     * I make use of the butterknife library and in order to make
     * the different components you need to make calls with the
     * butterknife annotations.
     * The butterknife library will make the findviewbyId method unecassry
     * */
    @BindView(R.id.lineChart)
    LineChartView mLineChart;
    @BindView(R.id.inkomenCard)
    CardView mInkomenCard;
    @BindView(R.id.uitgaveCard)
    CardView mUitgaveCard;
    @BindView(R.id.optimaleUitgaveCard)
    CardView mOptimaleUitgaveCard;
    @BindView(R.id.optimaleUitgaveText)
    TextView mOptimaleUitgaveText;
    @BindView(R.id.main_layout_content_main)
    ConstraintLayout mMainLayout;

    /*
     * Make a new list if axisvalues this list will be used for adding new values
     * on the X axis.
     *
     * Also I have made a new instance of the linechartdata. This will be used for adding
     * values into the chart.
     */
    List<AxisValue> xAxisValue = new ArrayList<>();
    Axis axisX = new Axis();
    private LineChartData data = new LineChartData();

    /*
     * I want the chart to have axis and I want to have it axesnames
     * So Here I made two boolean values and set them to true. When I call the function
     * of adding new axes names or to tell the chart that I want the chart to have axes
     * I just need to pass these two values to it.
     */
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean isInteractive = true;
    private boolean isSelectionEnabled = true;

    // Make a new instance of the firestore. This will make sure that I can make calls to my firestore data.
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Create new viemodels
    private IncomeViewModel mIncomeViewModel;
    private SavingsViewModel mSavingsViewModel;

    /*
     * Make new instances of two lists. These two lists are completely different from each other.
     * One of the lists will only be used for fetching data about the income and the other one can fetch
     * the id and the income.
     */
    private List<Income> mIncomes = new ArrayList<>();
    private List<FetchIncomes> mOnlyIncomes = new ArrayList<>();

    /*
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        ButterKnife.bind(this);
        // Initialize the viewmodels.
        mIncomeViewModel = new IncomeViewModel(getApplicationContext());
        mSavingsViewModel = new SavingsViewModel(getApplicationContext());
        generateData(); // This is the call to the method that will fill the graph with the information
        observeNewAddedValues();// call the observer to see if there is any new value added into the db
        calculateOptimumSpend(); // call the calculate optimum spendings level that a user can spend.
    }

    /**
     * Add a annotation with the use of butterknife to make a click method for the inkomenCard button
     */
    @OnClick(R.id.inkomenCard)
    public void goToIncomePage() {
        /*
         * I wanted a little bit of a delay when the user clicks on the button.
         * This is why I use the Handler class.
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(NewMainActivity.this, IncomeActivity.class);
                NewMainActivity.this.startActivity(mainIntent);
                NewMainActivity.this.finish();
            }
        }, 400);
    }

    /*
     * Add a annotation from butterknife to make it possible to click on the uitgave button.
     */
    @OnClick(R.id.uitgaveCard)
    public void goToSpendPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(NewMainActivity.this, SavingsActivity.class);
                NewMainActivity.this.startActivity(mainIntent);
                NewMainActivity.this.finish();
            }
        }, 400);
    }


    /*
     * Method to generate data and fill the chart with data.
     * I want to have a connection to firebase to save and retrieve data.
     */
    private void generateData() {
        final List<PointValue> values = new ArrayList<>(); // Make new list which will hold all of the points in the chart.
        final List<Line> lines = new ArrayList<>(); // Make a new list which will hold all of the lines in the chart.
        /*
         * Call the observer of the getIncomesOnly function. This function will make sure to only
         * fetch the incomes of the user. Inside the body of this observer I have a check
         * which checks whether the List is empty or not.
         *
         * If the list is not empty it will loop through all the values and add them to the chart.
         */
        mIncomeViewModel.getIncomesOnly().observe(this, new Observer<List<FetchIncomes>>() {
            @Override
            public void onChanged(@Nullable List<FetchIncomes> fetchIncomes) {
                mOnlyIncomes = fetchIncomes;

                assert mOnlyIncomes != null;
                if (mOnlyIncomes.size() != 0) {
                    Log.i(TAG, "onChanged: " + mOnlyIncomes.get(0).income);
                    for (int i = 0; i < mOnlyIncomes.size(); i++) {
                        String income = String.valueOf(mOnlyIncomes.get(i).income);
                        float incomeFloat = Float.parseFloat(income);
                        values.add(new PointValue(i, incomeFloat));
                        xAxisValue.add(new AxisValue(i + 1));
                        if (xAxisValue.size() == 12) {
                            xAxisValue.clear();
                        }
                    }
                    // Set the color of the line within the chart
                    Line line = new Line(values).setColor(getResources().getColor(R.color.blue1)).setCubic(true);
                    line.setPointRadius(5);
                    line.setHasLabelsOnlyForSelected(true);
                    lines.add(line);

                    data.setLines(lines);
                    // Check if I wanted my chart to have axes. if so than add the new axes to it.
                    if (hasAxes) {
                        axisX.setValues(xAxisValue);
                        Axis axisY = new Axis().setHasLines(true);
                        axisY.setTextSize(9);
                        axisX.setTextSize(9);
                        if (hasAxesNames) { // Check if hasAxesNames is set to true. If so than set the name of the axes.
                            axisX.setName("Maand");
                        }
                        // Set the name of the axes.
                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
                    } else {
                        data.setAxisXBottom(null);
                        data.setAxisYLeft(null);
                    }

                    data.setBaseValue(Float.NEGATIVE_INFINITY);
                    // Set the data into the chart.
                    mLineChart.setLineChartData(data);
                }
            }
        });


        mLineChart.setInteractive(isInteractive); // Make this true in order to make the points clickable and interactive
        mLineChart.setValueSelectionEnabled(isSelectionEnabled); // Set this to true to enable the fact that you can see the value of a point after clicking on it.


    }

    /*
     * This function will calculate the optimal money that a user can spend
     */
    private void calculateOptimumSpend() {
        if (networkAvailable()) {
            db.collection("user_income")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + Double.valueOf(document.getData().get("income").toString()));
                                    mOptimaleUitgaveText.setText(getResources().getString(R.string.euro_sign) + Double.valueOf(document.getData().get("income").toString()) / 100 * 20 + " ,-");

                                }
                            } else {
                                Log.w(TAG, getResources().getString(R.string.general_error_message), task.getException());
                            }
                        }
                    });
        } else {
            mIncomeViewModel.getAllIncome().observe(this, new Observer<List<Income>>() {
                @Override
                public void onChanged(@Nullable List<Income> incomes) {
                    mIncomes = incomes;
                    mOptimaleUitgaveText.setText(getResources().getString(R.string.euro_sign) + mIncomes.get(mIncomes.size() - 1).income / 100 * 20 + " ,-");
                }
            });

        }
    }


    /*
     * This function will look if there is any internet available on the phone of the user.
     * @return true if there is internet connection
     */
    @Override
    public boolean networkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
     * Observe the newly added values into the Database.
     */
    @Override
    public void observeNewAddedValues() {
        mIncomeViewModel.getAllIncome().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
                mIncomes = incomes;
            }
        });
    }
}

