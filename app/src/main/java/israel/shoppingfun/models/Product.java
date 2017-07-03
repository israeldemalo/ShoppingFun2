package israel.shoppingfun.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hackeru on 03/07/2017.
 */
//Ignorance is bliss
public class Product implements Parcelable {
    private String name;
    private String addedBy;
    private boolean purchased;

    //Constructor for POJO:
    public Product() {
    }

    //Constructor:
    public Product(String name, String addedBy, boolean purchased) {
        this.name = name;
        this.addedBy = addedBy;
        this.purchased = purchased;
    }

    //getters and setters:
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddedBy() {
        return addedBy;
    }
    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
    public boolean isPurchased() {
        return purchased;
    }
    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", purchased=" + purchased +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.addedBy);
        dest.writeByte(this.purchased ? (byte) 1 : (byte) 0);
    }

    protected Product(Parcel in) {
        this.name = in.readString();
        this.addedBy = in.readString();
        this.purchased = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
