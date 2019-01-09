package financialness.semen.seyfullah.com.financialness.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import financialness.semen.seyfullah.com.financialness.R;

/*
 * Created by Seyfullah Semen on 25-10-2018.
 */
public class SliderAdapter extends PagerAdapter {
    /*
     * Declare variables which I need to use in the application.
     */
    private Context mContext;
    private List<String> incomeList;
    private List<Integer> icons;
    private List<String> sliderTitles;

    /*
     *
     * @param context
     * @param incomeList
     * @param icons
     * @param sliderTitles
     */
    public SliderAdapter(Context context, List<String> incomeList, List<Integer> icons, List<String> sliderTitles) {
        this.mContext = context;
        this.incomeList = incomeList;
        this.sliderTitles = sliderTitles;
        this.icons = icons;
    }

    /*
     *
     * @return
     */
    @Override
    public int getCount() {
        return incomeList.size();
    }

    /*
     *
     * @param view
     * @param o
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /*
     *
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView textMoney = view.findViewById(R.id.text_money);
        CardView sliderCardView = view.findViewById(R.id.slider_card_view);
        ImageView sliderImageIcon = view.findViewById(R.id.image_slider);
        TextView sliderTitle = view.findViewById(R.id.slider_title);
        ConstraintLayout mainLayoutSlider = view.findViewById(R.id.main_layout_slider);

        textMoney.setText(incomeList.get(position));
        sliderTitle.setText(sliderTitles.get(position));
        sliderImageIcon.setImageResource(icons.get(position));


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;

    }

    /*
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

}
