package Client;

import Communication.Communication;
import Serveur.Serveur;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sun.font.CharToGlyphMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Application implements Runnable{

    private String nom = "Client";
    private TextFlow echange = new TextFlow();
    TextField input = new TextField();
    private String mess ="";
    private boolean newMess = false;

    private Parent createContent(){
        Text t1 = new Text();
        t1.setStyle("-fx-fill: #4F8A10;-fx-font-weight:bold;");
        t1.setText(">>Nouvelle connection avec le serveur : bienvenue !\n");
        Text t2 = new Text();
        t2.setStyle("-fx-fill: #6c6c75;-fx-font-weight:normal;");
        t2.setText(">>A tout moment tapez q uniquement pour quitter l'echange.\n");
        echange.getChildren().addAll(t1, t2);
        echange.setLineSpacing(2);
        echange.setMinWidth(600);
        echange.setMinHeight(500);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(echange);
        scrollPane.setPrefSize(600,500);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


        input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

            }
        });

        input.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent ke){
                if(ke.getSource()==input)
                    if(ke.getCode() == KeyCode.ENTER){
                        mess = input.getText();
                        if(mess.equals("q")) {
                            try {
                                stop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Communication communication = null;
                        try {
                            communication = new Communication(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT());
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        communication.envoyer(mess);
                        newMess = false;
                        Text t3 = new Text();
                        t3.setStyle("-fx-fill: #874ab7;-fx-font-weight:normal;");
                        t3.setText("Vous : " + mess + "\n");
                        echange.getChildren().add(t3);
                        //scrollPane.setVvalue( 2.0d );

                        String answer = communication.recevoir().toString();
                        if (answer != null){
                            Text t4 = new Text();
                            t4.setStyle("-fx-fill: #1d9691;-fx-font-weight:normal;");
                            t4.setText("Vous avez reçu une réponse du serveur : \n " + answer + ".\n");
                            echange.getChildren().add(t4);
                        }
                    }
            }
        });

        VBox root = new VBox(20,scrollPane,input);
        root.setPrefSize(600,600);
        return root;

    }


    public  void demarrer(){
        boolean quitter = false;

        while(!quitter){
            try {
                Communication communication = new Communication(InetAddress.getByName(Serveur.getIP()), Serveur.getPORT());

                if(newMess){
                    if(mess.equals("q"))
                        quitter = true;
                    communication.envoyer(mess);
                    newMess = false;
                    Text t3 = new Text();
                    t3.setStyle("-fx-fill: #874ab7;-fx-font-weight:normal;");
                    t3.setText(mess + "\n");
                    echange.getChildren().add(t3);
                }


                String answer = communication.recevoir().toString();
                if (answer != null)
                    System.out.println("Vous avez reçu une réponse du serveur : \n \"" + answer + "\".");
                System.out.println("");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        /*boolean quitter = false;

        System.out.println("Nouvelle connection avec le serveur : bienvenue !");
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
        }*/

        Application.launch(args);

    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void run() {
        demarrer();
    }
}
