package ca.utoronto.cscb07project.ui.user;

import java.io.Serializable;

public class Announcement{
    private String title;
    private String announcmentId;
    private String date;
    private String description;

    public Announcement(String title, String date, String description, String announcmentid) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.announcmentId = announcmentid;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getAnnouncmentId(){return announcmentId;}

    public void setAnnouncmentId(String announcmentId){this.announcmentId = announcmentId;}


}


