package ayry.com.ary_app;

import java.util.ArrayList;

/**
 * Created by Stephen Kearns on 05/03/2016.
 */
public class Shop_items{
    //This would contain the path to the local shop image
    private int id;
    private String img;
    private String title;
    private String desc;
    private String address;
    private int pNumber;
    private String openingtime;
    private String cloisngtime;
    private Double lat;
    private Double longatt;

     public String geoLocation;

    public Shop_items(int id, String img, String title, String desc, String address, int pNumber, String openingtime, String cloisngtime, Double lat, Double longatt){
        this.id = id;
        this.img = img;
        this.title = title;
        this.desc = desc;
        this.address = address;
        this.pNumber = pNumber;
        this.openingtime = openingtime;
        this.cloisngtime = cloisngtime;
        this.lat = lat;
        this.longatt = longatt;
    }





    public String getTitle(){
        return title;
    }

    public String getDesc(){
        return desc;
    }

    public String getImg(){
        return img;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getpNumber() {
        return pNumber;
    }


    public String getOpeningtime() {
        return openingtime;
    }

    public String getCloisngtime() {
        return cloisngtime;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLongatt() {
        return longatt;
    }




}
