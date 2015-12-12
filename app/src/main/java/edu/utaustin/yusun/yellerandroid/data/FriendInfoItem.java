package edu.utaustin.yusun.yellerandroid.data;

/**
 * Created by yusun on 12/12/15.
 */
public class FriendInfoItem {
    private String name, status, profilePic, timeStamp;
    public FriendInfoItem(String name, String status, String profilePic, String timeStamp) {
        super();

        this.name = name;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
