package keenen;

public class Booking {

    private String bookingID;
    private String guestName;
    private String guestAddress;
    private String guestEmailAddress;
    private String guestContact;
    private String guestCreditCard;
    private String checkInDate;
    private String checkOutDate;
    private int numberOfRooms;
    private String hotel;
    private String city;

    public Booking(String bookingID) {
        this.bookingID = bookingID;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public void setGuestAddress(String guestAddress) {
        this.guestAddress = guestAddress;
    }

    public void setGuestEmailAddress(String guestEmailAddress) {
        this.guestEmailAddress = guestEmailAddress;
    }

    public void setGuestContact(String guestContact) {
        this.guestContact = guestContact;
    }

    public void setGuestCreditCard(String guestCreditCard) {
        this.guestCreditCard = guestCreditCard;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
