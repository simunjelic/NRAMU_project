package ba.sum.fsre.nramu_project.Model;
import android.os.Parcel;
import android.os.Parcelable;

public class RVModal implements Parcelable {
    // creating variables for our different fields.
    private String Name;
    private String Desc;
    private String AutorID;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getAutorID() {
        return AutorID;
    }

    public void setAutorID(String autorID) {
        AutorID = autorID;
    }

    public String getAutorName() {
        return AutorName;
    }

    public void setAutorName(String autorName) {
        AutorName = autorName;
    }

    public String getBrTelefona() {
        return BrTelefona;
    }

    public void setBrTelefona(String brTelefona) {
        BrTelefona = brTelefona;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    private String AutorName;
    private String BrTelefona;
    private String slika;
    private String productID;

    public RVModal(String name, String desc, String autorID, String autorName, String brTelefona, String slika, String productID) {
        Name = name;
        Desc = desc;
        AutorID = autorID;
        AutorName = autorName;
        BrTelefona = brTelefona;
        this.slika = slika;
        this.productID = productID;
    }

    protected RVModal(Parcel in) {
        Name = in.readString();
        Desc = in.readString();
        AutorID = in.readString();
        AutorName = in.readString();
        BrTelefona = in.readString();
        slika = in.readString();
        productID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Desc);
        dest.writeString(AutorID);
        dest.writeString(AutorName);
        dest.writeString(BrTelefona);
        dest.writeString(slika);
        dest.writeString(productID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RVModal> CREATOR = new Creator<RVModal>() {
        @Override
        public RVModal createFromParcel(Parcel in) {
            return new RVModal(in);
        }

        @Override
        public RVModal[] newArray(int size) {
            return new RVModal[size];
        }
    };
}