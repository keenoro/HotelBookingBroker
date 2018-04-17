package keenen;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private static final int PORT = 8100;
    private static BufferedReader reader;
    private static PrintStream writer;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        InetAddress address;
        System.out.print("Enter IP address: ");
        String addressInString = scanner.nextLine();
        try{
            address = InetAddress.getByName(addressInString);
        } catch (UnknownHostException e){
            System.out.println("Invalid IP address");
            return;
        }

        //Create a new socket
        Socket socket;
        try{
            socket = new Socket(address, PORT);
        } catch(IOException e) {
            System.out.println("Failed to connect. Server is down");
            return;
        }

        //Set input and output
        InputStream in;
        OutputStream out;
        try{
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch(IOException e){
            System.out.println(" Client Blah ");
            return;
        }

        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintStream(out);

        String message = "";
        try {
            message = reader.readLine();
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //For choosing a city
        boolean exit = false;
        int choice;
        printCities();
        while(!exit){
            System.out.print("\nSelect a city: ");
            try{
                choice = scanner.nextInt();
            } catch(InputMismatchException e){
                choice = -1;
            }
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        writer.println("MELBOURNE");

                        //For choosing a hotel
                        while(true){
                            message = reader.readLine();
                            if(message.equalsIgnoreCase("END")){
                                break;
                            }
                            System.out.println(message);
                        }
                        message = reader.readLine();
                        System.out.println(message);

                        System.out.print("\nSelect a hotel: ");

                        choice = scanner.nextInt();
                        writer.println(choice);

                        message = reader.readLine();
                        if(message.equalsIgnoreCase("back")){
                            System.out.println("Back to city menu");
                            printCities();
                        } else {

                            //For choosing an option in the selected hotel
                            while(true) {

                                char c;
                                String hotelOptions = "";
                                while(true){
                                    c = (char) reader.read();
                                    if(c == '%'){
                                        break;
                                    }
                                    hotelOptions += c;
                                }
                                System.out.println(hotelOptions);
                                System.out.print("\nSelect an option: ");
                                choice = scanner.nextInt();
                                writer.println(choice);

                                message = reader.readLine();
                                if(message.equalsIgnoreCase("back")){
                                    System.out.println("Back to city menu");
                                    break;
                                } else if(message.equalsIgnoreCase("retry")){
                                    System.out.println("Invalid input");
                                } else if(message.equalsIgnoreCase("no_rooms")){
                                    System.out.println("Sorry, no rooms available.");
                                } else {
                                    if(startBooking()){
                                        break;
                                    }
                                }
                            }

                        }
                        break;
                    case 2:
                        writer.println("PERTH");

                        //For choosing a hotel
                        while(true){
                            message = reader.readLine();
                            if(message.equalsIgnoreCase("END")){
                                break;
                            }
                            System.out.println(message);
                        }
                        message = reader.readLine();
                        System.out.println(message);

                        System.out.print("\nSelect a hotel: ");

                        choice = scanner.nextInt();
                        writer.println(choice);

                        message = reader.readLine();
                        if(message.equalsIgnoreCase("back")){
                            System.out.println("Back to city menu");
                            printCities();
                        } else {

                            //For choosing an option in the selected hotel
                            while(true) {

                                char c;
                                String hotelOptions = "";
                                while(true){
                                    c = (char) reader.read();
                                    if(c == '%'){
                                        break;
                                    }
                                    hotelOptions += c;
                                }
                                System.out.println(hotelOptions);
                                System.out.print("\nSelect an option: ");
                                choice = scanner.nextInt();
                                writer.println(choice);

                                message = reader.readLine();
                                if(message.equalsIgnoreCase("back")){
                                    System.out.println("Back to city menu");
//                                    printCities();
                                    break;
                                } else if(message.equalsIgnoreCase("retry")){
                                    System.out.println("Invalid input");
                                } else if(message.equalsIgnoreCase("no_rooms")){
                                    System.out.println("Sorry, no rooms available.");
                                } else {
                                    if(startBooking()){
                                        break;
                                    }
                                }
                            }

                        }
                        break;
                    case 3:
                        System.out.println("Sorry, unable to reach Sydney server.");
                        break;
                    case 4:
                        printCities();
                        break;
                    case 5:
                        writer.println("EXIT");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            } catch(IOException e) {
                System.out.println(" Client BLAH ");
            }
        }
    }

    private static void printCities(){
        System.out.println("\t==========================");
        System.out.println("\tCities available");
        System.out.println("\t==========================");
        System.out.println("\t 1 - Melbourne");
        System.out.println("\t 2 - Perth");
        System.out.println("\t 3 - Sydney");
        System.out.println("\t==========================");
        System.out.println("\t 4 - Print city list");
        System.out.println("\t 5 - Quit the application");
        System.out.println("\t==========================");
    }

    //Start the booking process
    private static boolean startBooking(){
        String reply;

        scanner = new Scanner(System.in);
        try {
            //Check-in date
            getInfo("Check-in date: ");

            //Check-out date
            getInfo("Check-out date: ");

            //Check if the days are already booked
            reply = reader.readLine();
            if(reply.equalsIgnoreCase("DAYS_NOT_AVAILABLE")){
                System.out.println("Sorry, those days are fully booked. \nTry selecting a new check in and out date");
                return false;
            }

            //Number of rooms to book
            getInfo("Number of rooms to book: ");

            //Check if there are enough rooms to book
            reply = reader.readLine();
            if(reply.equalsIgnoreCase("NOT_ENOUGH_ROOMS")){
                System.out.println("Sorry, not enough rooms to meet your request");
                return false;
            }

            //Get required customer information and send it to the server
            getInfo("Name: ");
            getInfo("Email address: ");
            getInfo("Phone number: ");
            getInfo("Home address: ");
            getInfo("Credit card number: ");

            //Ask the user if they want to confirm the booking
            getInfo("Confirm booking? [type \"no\" to cancel]: ");

            reply = reader.readLine();
            if(reply.equalsIgnoreCase("NOT_CONFIRM")){
                System.out.println("Booking process cancelled");
                return false;
            }

            //Booking status
            reply = reader.readLine();
            if(reply.equalsIgnoreCase("BOOKING_FAIL_DATE")){
                System.out.println("Sorry, days just booked out");
                return false;
            }

            if(reply.equalsIgnoreCase("BOOKING_FAIL_ROOM")){
                System.out.println("Sorry, rooms just booked out");
                return false;
            }

            System.out.println("Booking successful!");

            reply = "";
            char c;
            while(true){
                c = (char) reader.read();
                if(c == '%'){
                    break;
                }
                reply += c;
            }

            System.out.println(reply);
            return true;

        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    //Get user input and send it to the server
    private static void getInfo(String sentence){
        System.out.print(sentence);
        String info = scanner.nextLine();
        writer.println(info);
    }
}
