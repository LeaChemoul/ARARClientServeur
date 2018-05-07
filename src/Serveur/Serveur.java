package Serveur;

import Communication.*;

import javax.naming.OperationNotSupportedException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Serveur {

    private static final int PORT = 1500;
    private static final String IP = "127.0.0.1";

    private static ArrayList<Connexion> connexionsEnCours = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Communication communication = new Communication(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT());

            Message message;

            while (true) {
                System.out.println("Serveur en attente :");
                message = null;
                try {
                    message = communication.recevoir(Serveur.getPORT());
                } catch (Exception ignored) { }

                if (message != null) {
                    //tester si message est q et alor supprimer la connexion
                    //if(message.getData())

                    if(!dejaConnectee(message.getAdress())){
                        Connexion connexion = new Connexion(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT(), new DatagramSocket(),communication.getDernierPacketRecu());
                        connexionsEnCours.add(connexion);
                        Thread thread = new Thread(connexion);
                        thread.start();
                    }
                }
           }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    public static boolean dejaConnectee(InetAddress address){
        for (Connexion connexionsEnCour : connexionsEnCours) {
            if (connexionsEnCour.getAdress() == address)
                return true;
        }
        return false;
    }


    public static int getPORT() {
        return PORT;
    }

    public static String getIP() {
        return IP;
    }
}
