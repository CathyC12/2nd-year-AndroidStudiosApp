package ayry.com.ary_app;

/**
 * Created by stephen on 12/04/2016.
 */
public class EventsModel {
    private int id;
    private String title;
    private String catagory;
    private String time;
    private String date;
    private Double lat;
    private Double longattl;
    private String location;

    private int image;

    public EventsModel(int id, String catagory, String title,String location, String time, String date, Double lat, Double longattl) {
        this.id = id;
        this.title = title;
        this.catagory = catagory;
        this.time = time;
        this.date = date;
        this.location = location;
        this.lat = lat;
        this.longattl = longattl;
    }

    public EventsModel(String catagory, String date, String location){
        this.catagory = catagory;
        this.date = date;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCatagory() {
        return catagory;
    }

    public int getId() {
        return id;
    }

    public String getLocatiion() {
        return location;
    }

    public String getDate() {
        return date;
    }


    public Double getLat() {
        return lat;
    }


    public Double getLongattl() {
        return longattl;
    }


    public int getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}