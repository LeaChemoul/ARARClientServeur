package Communication;

import Serveur.Serveur;

import javax.naming.OperationNotSupportedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Connexion implements Runnable {

    private InetAddress adress;
    private int port;
    private DatagramSocket ds;
    private DatagramPacket premierPacket;

    public Connexion(InetAddress adress, int port, DatagramSocket ds, DatagramPacket message) {
        this.ds = ds;
        this.adress = adress;
        this.port = port;
        this.premierPacket = message;
    }

    @Override
    public void run() {
        Communication communication = null;
        try {
            communication = new Communication(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT(),this.ds);
            communication.setDernierPacketRecu(premierPacket);
            Message message = new Message(premierPacket.getPort(),premierPacket.getAddress(),premierPacket.getData());
            while (true) {

                if (message != null) {
                    if(new String(message.getData(), StandardCharsets.UTF_8).equals("q"))
                        break;
                    System.out.println("Message recu : \"" + message.toString() + "\".");

                    String reponse = "Nous avons bien reçu votre message depuis " + message.getAdress();
                    communication.repondre(reponse);
                }

                try {
                    message = communication.recevoir(Serveur.getPORT());
                } catch (Exception ignored) {
                }
            }
        } catch (OperationNotSupportedException | UnknownHostException e1) {
            e1.printStackTrace();
        }
    }

    public InetAddress getAdress() {
        return adress;
    }
}
