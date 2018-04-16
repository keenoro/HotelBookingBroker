package keenen;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static final int PORT = 8100;
    private static ArrayList<City> cities = new ArrayList<>();

    public static void main(String[] args){

        //Adding Melbourne and their hotels
        City city = new City("Melbourne");
        city.addHotel("Hilton", 450.00, 20);
        city.addHotel("Mercure", 500.00, 10);
        cities.add(city);

        //Adding Perth and their hotels
        city = new City("Perth");
        city.addHotel("Continental", 350.00, 30);
        city.addHotel("Mercure", 600.00, 5);
        cities.add(city);


        ServerSocket socketServer = null;
        try {
            socketServer = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Port already in use");
        }

        System.out.println("Hotel Booking System running!");
        while (true){
            Socket socketClient = null;
            try{
                socketClient = socketServer.accept();
            } catch(IOException e){
                System.out.println("Failed to accept");
            }

            new ServerHandler(socketClient, cities).start();
        }
    }
}
