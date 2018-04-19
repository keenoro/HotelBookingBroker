package keenen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler extends Thread {

    private Socket client;

    //Create a lock to be used for synchronization
    private static final Object LOCK = new Object();

    //Keep track of number of clients and label each client uniquely
    private static int noOfClient = 0;
    private static int clientNo = 0;
    private int clientID;

    private static int bookingNumber = 0;
    private static ArrayList<City> cities;
    private static ArrayList<Booking> bookings = new ArrayList<>();
    private BufferedReader reader;
    private PrintStream out;


    public ServerHandler(Socket client, ArrayList<City> cities) {
        this.client = client;
        ServerHandler.cities = cities;
    }

    @Override
    public void run() {
        noOfClient += 1;
        clientNo += 1;
        clientID = clientNo;

        System.out.println("New client accepted. Connected clients: " + noOfClient);
        try{
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintStream(client.getOutputStream());

            out.println("Welcome to the Hotel Booking System");

            //For choosing a city
            String message;
            boolean exit = false;
            String cityName;
            ArrayList<Hotel> hotels;
            Hotel hotel;
            while (!exit){
                message = reader.readLine();
                switch(message.toLowerCase()){
                    case "melbourne":
                        System.out.println("Client " + clientID + ": Melbourne requested");

                        //For choosing a hotel
                        cityName = cities.get(0).getCityName();
                        hotels = cities.get(0).getHotels();

                        hotel = chooseHotel(hotels, cityName);

                        if(hotel == null){
                            out.println("BACK");
                        } else {
                            out.println("HOTEL_DETAILS");

                            //For choosing an option in the selected hotel
                            while(true){

                                //Prints the hotel details
                                message = printHotelDetails(hotel);
                                out.print(message);
                                out.flush();

                                int choice = Integer.parseInt(reader.readLine());
                                if(choice == 1){
                                    if(hotel.noRoomsAvailable()){
                                        out.println("NO_ROOMS");
                                    } else {
                                        System.out.println("Client " + clientID + ": Booking for " + hotel.getHotelName() + " started");
                                        out.println("BOOKING_START");
                                        if(startBooking(hotel, cityName)){
                                            break;
                                        }
                                    }
                                } else if (choice == 2){
                                    out.println("BACK");
                                    break;
                                } else {
                                    out.println("RETRY");
                                }
                            }
                        }
                        break;
                    case "perth":
                        System.out.println("Client " + clientID + ": Perth requested");

                        //For choosing a hotel
                        cityName = cities.get(1).getCityName();
                        hotels = cities.get(1).getHotels();
                        hotel = chooseHotel(hotels, cityName);
                        if(hotel == null){
                            out.println("BACK");
                        } else {
                            out.println("HOTEL_DETAILS");

                            //For choosing an option in the selected hotel
                            while(true){

                                //Prints the hotel details
                                message = printHotelDetails(hotel);
                                out.print(message);
                                out.flush();

                                int choice = Integer.parseInt(reader.readLine());
                                if(choice == 1){
                                    if(hotel.noRoomsAvailable()){
                                        out.println("NO_ROOMS");
                                    } else {
                                        System.out.println("Client " + clientID + ": Booking for " + hotel.getHotelName() + " started");
                                        out.println("BOOKING_START");
                                        if(startBooking(hotel, cityName)){
                                            break;
                                        }
                                    }
                                } else if (choice == 2){
                                    out.println("BACK");
                                    break;
                                } else {
                                    out.println("RETRY");
                                }
                            }
                        }
                        break;
                    case "exit":
                        exit = true;
                        break;
                }
            }
            noOfClient -= 1;
            System.out.println("Client " + clientID + " disconnected. Connected clients: " + noOfClient);
            client.close();

        } catch(IOException e){
            noOfClient -= 1;
            System.out.println("Client " + clientID + " disconnected abruptly. Connected clients: " + noOfClient);
        }
    }


    private String printHotel(Hotel hotel){
        return hotel.getHotelName() + " - $" + hotel.getRoomRates();
    }


    //Returns the hotel details
    private String printHotelDetails(Hotel hotel){
        String blank = "\n\t===========================";
        String message = blank + "\n\t" + hotel.getHotelName() + blank + "\n\tAvailable days in July 2018";
        message += hotel.printCalendar();

        message += blank + "\n\tRoom rate: $" + hotel.getRoomRates();
        message += "\n\tRooms available: " + hotel.getNumberOfRooms();

        message += blank + "\n\t1 - Book \n\t2 - Go back";

        message += "%";
        return message;
    }


    //Prints the hotel selections and return the selected hotel
    private Hotel chooseHotel(ArrayList<Hotel> hotels, String cityName){

        int choice;
        for(int i = 0;i < hotels.size();i++){
            out.println((i+1) + " - " + printHotel(hotels.get(i)));
        }
        out.println("END");
        out.println((hotels.size()+1) + " - " + "Go back");

        try {
            choice = Integer.parseInt(reader.readLine());
            if (choice == (hotels.size() + 1)) {
                return null;
            } else if (choice > (hotels.size() + 1) || choice < 0){
                return null;
            } else {
                Hotel hotel = hotels.get(choice - 1);
                System.out.println("Client " + clientID + ": " + hotel.getHotelName() + " in " + cityName + " requested");
                return hotel;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    //Start the booking process
    private Boolean startBooking(Hotel hotel, String cityName){
        String guestInfo;
        int checkInDate;
        int checkOutDate;
        int noOfRooms;

        try {
            //Check-in date
            checkInDate = Integer.parseInt(reader.readLine());

            //Check-out date
            checkOutDate = Integer.parseInt(reader.readLine());

            //Check if the days are already booked
            for (int i = checkInDate - 1; i < checkOutDate - 1; i++) {
                if (!hotel.checkADate(i)) {
                    out.println("DAYS_NOT_AVAILABLE");
                    return false;
                }
            }
            out.println("DAYS_AVAILABLE");

            //Number of rooms to book
            guestInfo = reader.readLine();
            noOfRooms = Integer.parseInt(guestInfo);

            //Check if there are enough rooms to book
            if(!hotel.enoughRooms(noOfRooms)){
                out.println("NOT_ENOUGH_ROOMS");
                return false;
            }
            out.println("ENOUGH_ROOMS");

            //Get required customer information from the client
            String guestName = reader.readLine();          //Name
            String guestEmailAddress = reader.readLine();  //Email address
            String guestPhoneNumber = reader.readLine();   //Phone number
            String guestAddress = reader.readLine();       //House address
            String guestCreditCard = reader.readLine();    //Credit card number

            //Receive the user response to confirm booking
            guestInfo = reader.readLine();
            if(guestInfo.equalsIgnoreCase("no") || guestInfo.equalsIgnoreCase("n")){
                out.println("NOT_CONFIRM");
                return false;
            }
            out.println("CONFIRM");

            //Check again if the rooms or days are booked by someone else before actually booking
            synchronized (ServerHandler.LOCK){
                //Check if the days are already booked
                for (int i = checkInDate - 1; i < checkOutDate - 1; i++) {
                    if (!hotel.checkADate(i)) {
                        out.println("BOOKING_FAIL_DATE");
                        return false;
                    }
                }

                //Check if there are enough rooms to book
                if(!hotel.bookSomeRooms(noOfRooms)){
                    out.println("BOOKING_FAIL_ROOM");
                    return false;
                }

                /*Make a booking by creating a new booking class to store the information
                  and update the hotel's number of rooms and the appropriate duration of stay
                */
                bookingNumber += 1;
                String bookingID = "B" + hotel.getHotelName().substring(0,1) +
                        cityName.substring(0,1) +
                        String.format("%04d", bookingNumber);

                Booking booking = new Booking(bookingID);

                //Set the dates to false for occupied
                for (int i = checkInDate - 1; i < checkOutDate - 1; i++) {
                    hotel.setADate(i, false);
                }

                //Set the customer's details to the booking instance
                booking.setCheckInDate("July " + checkInDate);
                booking.setCheckOutDate("July " + checkOutDate);
                booking.setNumberOfRooms(noOfRooms);
                booking.setHotel(hotel.getHotelName());
                booking.setCity(cityName);

                booking.setGuestName(guestName);
                booking.setGuestEmailAddress(guestEmailAddress);
                booking.setGuestContact(guestPhoneNumber);
                booking.setGuestAddress(guestAddress);
                booking.setGuestCreditCard(guestCreditCard);

                //Add the booking into the booking list
                bookings.add(booking);

                System.out.println("Client " + clientID + ": " + "Booking at " + hotel.getHotelName() + " in " + cityName + " with Booking ID: " + bookingID);
                out.println("BOOKING_SUCCESS");

                //Show the confirmed booking information to the client
                String blank = "\n\t===========================";
                String output = "\n\tBooking ID: " + bookingID +
                        "\n\tName: " + guestName +
                        "\n\tEmail address: " + guestEmailAddress +
                        "\n\tPhone number: " + guestPhoneNumber +
                        "\n\tCity: " + cityName +
                        "\n\tHotel: " + hotel.getHotelName() +
                        "\n\tCheck-in date: July " + checkInDate +
                        "\n\tCheck-out date: July " + checkOutDate +
                        "\n\tNumber of rooms: " + noOfRooms;

                out.print(blank + output + blank + "%");
                out.flush();

                return true;
            }

        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
