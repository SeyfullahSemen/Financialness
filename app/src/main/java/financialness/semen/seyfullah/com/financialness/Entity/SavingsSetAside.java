package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Seyfullah Semen on 16-12-2018.
 */
@Entity
public class SavingsSetAside implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int setAsideId;

    @ColumnInfo(name = "setAside")
    public double setAside;

    public SavingsSetAside(double setAside) {
        this.setAside = setAside;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.setAsideId);
        dest.writeDouble(this.setAside);
    }

    protected SavingsSetAside(Parcel in) {
        this.setAsideId = in.readInt();
        this.setAside = in.readDouble();
    }

    public static final Parcelable.Creator<SavingsSetAside> CREATOR = new Parcelable.Creator<SavingsSetAside>() {
        @Override
        public SavingsSetAside createFromParcel(Parcel source) {
            return new SavingsSetAside(source);
        }

        @Override
        public SavingsSetAside[] newArray(int size) {
            return new SavingsSetAside[size];
        }
    };
}
