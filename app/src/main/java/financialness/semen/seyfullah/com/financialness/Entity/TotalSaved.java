package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Seyfullah Semen on 30-12-2018.
 */
@Entity
public class TotalSaved implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "totalSavedId")
    public int TotalSavedId;

    @ColumnInfo(name = "totalsaved")
    public double totalsaved;

    public TotalSaved(double totalsaved) {
        this.totalsaved = totalsaved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.TotalSavedId);
        dest.writeDouble(this.totalsaved);
    }

    protected TotalSaved(Parcel in) {
        this.TotalSavedId = in.readInt();
        this.totalsaved = in.readDouble();
    }

    public static final Parcelable.Creator<TotalSaved> CREATOR = new Parcelable.Creator<TotalSaved>() {
        @Override
        public TotalSaved createFromParcel(Parcel source) {
            return new TotalSaved(source);
        }

        @Override
        public TotalSaved[] newArray(int size) {
            return new TotalSaved[size];
        }
    };
}
