package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Seyfullah Semen on 15-12-2018.
 *
 * This class is an entity class for the income of the user
 * The Table has two columns. One is for the income itself and the other
 * one is for the id of the income
 *
 */
@Entity
public class Income implements Parcelable {

    // Make a primary
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "income_id")
    public int incomeId;
    // make a column called income
    @ColumnInfo(name = "income")
    public double income;

    // make a constructor with the income parameter.
    public Income(double income) {
        this.income = income;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.incomeId);
        dest.writeDouble(this.income);
    }

    protected Income(Parcel in) {
        this.incomeId = in.readInt();
        this.income = in.readDouble();
    }

    public static final Creator<Income> CREATOR = new Creator<Income>() {
        @Override
        public Income createFromParcel(Parcel source) {
            return new Income(source);
        }

        @Override
        public Income[] newArray(int size) {
            return new Income[size];
        }
    };
}
