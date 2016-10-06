package ayry.com.ary_app;

import java.util.ArrayList;

/**
 * Created by Stephen Kearns on 29/03/2016.
 */
public class ObjectRequestHolder {

    static User userApp;
    static Shop_items shop;
    static EventsModel event;

    static ArrayList<Shop_items> shopList;
    private ArrayList<EventsModel> eventList;
    public ObjectRequestHolder(){
        shopList = new ArrayList<>();
        eventList = new ArrayList<>();


    }

    public void setUserApp(User userApp) {
        this.userApp = userApp;
    }

    public void setShop(Shop_items shop) {
        this.shop = shop;
    }


    public void addShop(Shop_items shop){
         shopList.add(shop);
    }

    public void addEvent(EventsModel event){
        eventList.add(event);
    }

    public ArrayList<EventsModel> getEventList(){
        return eventList;
    }
    public ArrayList<Shop_items> getShopList(){
        return shopList;
    }

    public EventsModel getEvent() {
        return event;
    }

    public User getUserApp() {
        return userApp;
    }

    public Shop_items getShop() {
        return shop;
    }

    public void setEvent(EventsModel event) {
        this.event = event;
    }
}
