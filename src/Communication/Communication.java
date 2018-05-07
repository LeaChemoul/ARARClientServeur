package Communication;

import com.sun.istack.internal.NotNull;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communication {

    public static final int MAX_LENGTH = 2048;
    private DatagramSocket ds;
    private InetAddress adress;
    private int port;
    private Message message;
    private DatagramPacket dernierPacketRecu;

    public Communication(InetAddress address, int port) {
        setDs(null);
        setAdress(address);
        setPort(port);
    }

    public Communication(InetAddress address, int port,DatagramSocket ds) {
        setDs(ds);
        setAdress(address);
        setPort(port);
    }

    public boolean envoyer(String data){
        try {
            setDs(new DatagramSocket());
            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), getAdress(), getPort());
            getDs().send(dp);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Message recevoir(){
        byte[] data = new byte[MAX_LENGTH];
        DatagramPacket dp;
        try{
            if(this.ds == null)
                this.ds = new DatagramSocket(this.port);
            dp = new DatagramPacket(data, data.length);
            ds.receive(dp);
            this.dernierPacketRecu = dp;
            return new Message(dp.getPort(),dp.getAddress(),dp.getData());

        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Message recevoir(int port) {
        setPort(port);
        return recevoir();
    }

    public void repondre(String reponse) throws OperationNotSupportedException {
        if (this.dernierPacketRecu == null)
            throw new OperationNotSupportedException("Aucun paquet n'a été recu dernierement");

        try {
            if(this.ds == null)
                this.ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(reponse.getBytes(), reponse.length(), this.dernierPacketRecu.getAddress(), this.dernierPacketRecu.getPort());
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DatagramSocket getDs() {
        return ds;
    }

    public void setDs(DatagramSocket ds) {
        this.ds = ds;
    }

    public InetAddress getAdress() {
        return adress;
    }

    public void setAdress(InetAddress adress) {
        this.adress = adress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DatagramPacket getDernierPacketRecu() {
        return dernierPacketRecu;
    }

    public void setDernierPacketRecu(DatagramPacket dernierPacketRecu) {
        this.dernierPacketRecu = dernierPacketRecu;
    }
}
