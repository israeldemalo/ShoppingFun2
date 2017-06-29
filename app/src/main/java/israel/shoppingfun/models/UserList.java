package israel.shoppingfun.models;

/**
 * Created by hackeru on 26/06/2017.
 */

public class UserList {
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
}
