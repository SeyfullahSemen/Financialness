package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Seyfullah Semen on 15-12-2018.
 */
@Entity
public class Income implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "income_id")
    public int incomeId;

    @ColumnInfo(name = "income")
    public double income;

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
