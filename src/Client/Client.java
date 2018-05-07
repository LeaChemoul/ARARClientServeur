package Client;

import Communication.Communication;
import Serveur.Serveur;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        boolean quitter = false;

        System.out.println("Nouvelle connection avec le serveur.");
        System.out.println("A tout moment tapez q uniquement pour quitter l'echange.");

        while(!quitter){
            try {
                Communication communication = new Communication(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT());

                Scanner scanner = new Scanner(System.in);
                String mess = scanner.nextLine();

                if(mess.equals("q"))
                    quitter = true;
                communication.envoyer(mess);

                String answer = communication.recevoir().toString();
                if (answer != null)
                    System.out.println("Vous avez reçu une réponse du serveur : \n \"" + answer + "\".");
                System.out.println("");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

}
