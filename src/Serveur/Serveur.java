package Serveur;

import Communication.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Serveur {

    private static final int PORT = 1500;
    private static final String IP = "127.0.0.1";

    private static ArrayList<Connexion> connexionsEnCours = new ArrayList<>();

    public static void main(String[] args) {
        try {
            System.out.println("Bienvenue sur le serveur d'adresse : " + IP + " ! Port d'écoute : " + PORT);

            Communication communication = new Communication(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT());

            Message message;

            while (true) {
                message = null;
                try {
                    message = communication.recevoir(Serveur.getPORT());
                } catch (Exception ignored) { }

                if (message != null) {
                    //tester si message est q et alors supprimer la connexion
                    if(new String(message.getData(), StandardCharsets.UTF_8).equals("q")){
                        //supprimer la connexion
                    }

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

    public  static int findConnexion(InetAddress inetAddress){
        for (Connexion connexion:connexionsEnCours) {
            if(connexion.getAdress().equals(inetAddress))
                return connexionsEnCours.indexOf(connexion);
        }
        return -1;
    }

    public static ArrayList<Connexion> getConnexionsEnCours() {
        return connexionsEnCours;
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getIP() {
        return IP;
    }
}
