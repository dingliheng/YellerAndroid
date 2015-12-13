package edu.utaustin.yusun.yellerandroid.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yusun on 11/15/15.
 */
public class ListItem implements Comparable<ListItem>{
    private int id;
    private String name, status, image, profilePic, timeStamp, yeller_id;

    public ListItem () {

    }

    public ListItem(int id, String name, String image, String status,
                    String profilePic, String timeStamp, String yeller_id, String url) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.yeller_id = yeller_id;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getYeller_id() {
        return yeller_id;
    }

    public void setYeller_id(String yeller_id) {
        this.yeller_id = yeller_id;
    }

    @Override
    public int compareTo(ListItem another) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date myDate = null, anotherDate = null;
        try {
            myDate = format.parse(timeStamp);
            anotherDate = format.parse(another.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (myDate.after(anotherDate))
            return -1;
        else if (myDate.equals(anotherDate))
            return 0;
        else
            return 1;

    }
}
