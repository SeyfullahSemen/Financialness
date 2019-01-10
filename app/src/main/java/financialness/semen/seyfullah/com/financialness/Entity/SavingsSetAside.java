package financialness.semen.seyfullah.com.financialness.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
 * Created by Seyfullah Semen on 16-12-2018.
 */
@Entity
public class SavingsSetAside {
    @PrimaryKey(autoGenerate = true)
    public int setAsideId;

    @ColumnInfo(name = "setAside")
    public double setAside;

    public SavingsSetAside(double setAside) {
        this.setAside = setAside;
    }


}
