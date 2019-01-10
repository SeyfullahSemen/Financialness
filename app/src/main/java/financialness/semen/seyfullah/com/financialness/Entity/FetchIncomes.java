package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Seyfullah Semen on 4-1-2019.
 * This class will be used to make it possible to only fetch the incomes which are added
 * by the user.
 */
public class FetchIncomes implements Parcelable {
    @ColumnInfo(name = "income")
    public double income;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.income);
    }

    public FetchIncomes() {
    }

    protected FetchIncomes(Parcel in) {
        this.income = in.readDouble();
    }

    public static final Parcelable.Creator<FetchIncomes> CREATOR = new Parcelable.Creator<FetchIncomes>() {
        @Override
        public FetchIncomes createFromParcel(Parcel source) {
            return new FetchIncomes(source);
        }

        @Override
        public FetchIncomes[] newArray(int size) {
            return new FetchIncomes[size];
        }
    };
}
