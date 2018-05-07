package Communication;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Message {

    private int port;
    private InetAddress adress;
    private byte[] data;

    public Message(int port, InetAddress adress, byte[] data){
        this.port = port;
        this.adress = adress;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message :" +
                "port=" + port +
                ", adress=" + adress +
                ", data=" + new String(data, StandardCharsets.UTF_8);
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAdress() {
        return adress;
    }

    public byte[] getData() {
        return data;
    }
}
