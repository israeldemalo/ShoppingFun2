package israel.shoppingfun.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hackeru on 26/06/2017.
 */

public class UserList implements Parcelable, Serializable {
    private String name;
    private String ownerID;
    private String ownerImage;
    private String listID;


    public UserList() {
    }

    public UserList(String name, String ownerID, String ownerImage, String listID) {
        this.name = name;
        this.ownerID = ownerID;
        this.ownerImage = ownerImage;
        this.listID = listID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "name='" + name + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", ownerImage='" + ownerImage + '\'' +
                ", listID='" + listID + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.ownerID);
        dest.writeString(this.ownerImage);
        dest.writeString(this.listID);
    }

    protected UserList(Parcel in) {
        this.name = in.readString();
        this.ownerID = in.readString();
        this.ownerImage = in.readString();
        this.listID = in.readString();
    }

    public static final Parcelable.Creator<UserList> CREATOR = new Parcelable.Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel source) {
            return new UserList(source);
        }

        @Override
        public UserList[] newArray(int size) {
            return new UserList[size];
        }
    };
}
