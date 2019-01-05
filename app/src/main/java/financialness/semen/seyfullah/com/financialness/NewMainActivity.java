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
    @BindView(R.id.bubbleChart)
    LineChartView mBubbleChart;
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

    /**
     * These are some variables I need in order to make the chart.
     * I want to set the shape the axes and labels for the chart.
     */
    private int numberOfLines = 3;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 13;

    List<AxisValue> xAxisValue = new ArrayList<>();
    Axis axisX = new Axis();
    private LineChartData data = new LineChartData();
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private IncomeViewModel mIncomeViewModel;
    private SavingsViewModel mSavingsViewModel;
    private List<Income> mIncomes = new ArrayList<>();
    private List<FetchIncomes> mOnlyIncomes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        ButterKnife.bind(this);
        mIncomeViewModel = new IncomeViewModel(getApplicationContext());
        mSavingsViewModel = new SavingsViewModel(getApplicationContext());
        generateData(); // This is the call to the method that will fill the graph with the information
        observeNewAddedValues();
        calculateOptimumSpend();
//        mProgressValue.setText(String.valueOf(mProgressBarGoal.getProgress()) + " %"); // Set the value of the progressbar
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

    /**
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


    /**
     * Method to generate data and fill the chart with data.
     * I want to have a connection to firebase to save and retrieve data.
     */
    private void generateData() {
        final List<PointValue> values = new ArrayList<>();
        final List<Line> lines = new ArrayList<>();
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
                            axisX.setName("Maand");
                        }
                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
                    } else {
                        data.setAxisXBottom(null);
                        data.setAxisYLeft(null);
                    }

                    data.setBaseValue(Float.NEGATIVE_INFINITY);
                    mBubbleChart.setLineChartData(data);
                }
            }
        });


        mBubbleChart.setInteractive(true);
        mBubbleChart.setValueSelectionEnabled(true);


    }


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
                                    mOptimaleUitgaveText.setText("€ " + Double.valueOf(document.getData().get("income").toString()) / 100 * 20 + " ,-");

                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            observeNewAddedValues();
            mOptimaleUitgaveText.setText("€ " + mIncomes.get(mIncomes.size() - 1).income / 100 * 20 + " ,-");
        }
    }


    @Override
    public boolean networkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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

