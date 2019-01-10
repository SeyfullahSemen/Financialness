package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
 * Created by Seyfullah Semen on 30-12-2018.
 */
@Entity
public class TotalSaved {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "totalSavedId")
    public int TotalSavedId;

    @ColumnInfo(name = "totalsaved")
    public double totalsaved;

    public TotalSaved(double totalsaved) {
        this.totalsaved = totalsaved;
    }


}
