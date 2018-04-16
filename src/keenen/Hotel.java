package keenen;

import java.util.HashMap;
import java.util.Map;

public class Hotel {

    private String hotelName;
    private double roomRates;
    private int numberOfRooms;
    private Map<Integer, Boolean> calendar;

    public Hotel(String hotelName, double roomRates, int numberOfRooms) {
        this.hotelName = hotelName;
        this.roomRates = roomRates;
        this.numberOfRooms = numberOfRooms;
        calendar = new HashMap<>();
        for(int i = 0; i < 31; i++){
            calendar.put(i, true);
        }
    }

    public String getHotelName() {
        return hotelName;
    }

    public double getRoomRates() {
        return roomRates;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public Map<Integer, Boolean> getCalendar() {
        return calendar;
    }

    public void setADate(int date, boolean booked){
        calendar.put(date, booked);
    }

    public boolean checkADate(int date){
        return calendar.get(date);
    }

    private void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public boolean enoughRooms(int rooms){
        return rooms <= numberOfRooms;
    }

    public boolean bookSomeRooms(int rooms){
        int newNumberOfRooms = numberOfRooms - rooms;
        if(newNumberOfRooms < 0){
            return false;
        } else {
            setNumberOfRooms(newNumberOfRooms);
            return true;
        }
    }

    public boolean noRoomsAvailable(){
        return numberOfRooms == 0;
    }

    public String printCalendar(){
        String message = "";
        for(int i = 0; i<calendar.size(); i++){
            if(calendar.get(i)){
                if(i == 0){
                    message += "\n\tJuly " + 1 + "st";
                } else if(i == 1){
                    message += "\n\tJuly " + 2 + "nd";
                } else if(i == 2){
                    message += "\n\tJuly " + 3 + "rd";
                } else {
                    message += "\n\tJuly " + (i+1) + "th";
                }
            }
        }
        return message;
    }
}
