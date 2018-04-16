package keenen;

import java.util.ArrayList;

public class City {

    private String cityName;
    private ArrayList<Hotel> hotels = new ArrayList<>();

    public City(String cityName) {
        this.cityName = cityName;
    }

    public void addHotel(String hotelName, double roomRates, int numberOfRooms){
        hotels.add(new Hotel(hotelName, roomRates, numberOfRooms));
    }

    public String getCityName() {
        return cityName;
    }

    public ArrayList<Hotel> getHotels() {
        return hotels;
    }

}
