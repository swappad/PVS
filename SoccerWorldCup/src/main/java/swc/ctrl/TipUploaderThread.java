package swc.ctrl;

import swc.data.Tip;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TipUploaderThread extends Thread {
    private Tip tip;
    private String betterEmail;
    private String betterPin;



    private Boolean uploadStatus=false;

    public TipUploaderThread(Tip tip, String betterEmail, String betterPin) {
        this.tip = tip;
        this.betterEmail = betterEmail;
        this.betterPin = betterPin;
        System.out.println(this.toString()+" created");
    }

    @Override
    public void run() {
        String combinedURL ="http://swc.dbis.info/api/Betting/"
                +betterEmail+"/"+betterPin+"/"+tip.getGameId()+"/"+tip.getGoalsHome() +"/"+tip.getGoalsGuest();

        System.out.println(this.toString()+" calling Betting Service");
        try{
            URL url = new URL(combinedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            uploadStatus = (bufferedReader.readLine()).equals("true");


        }catch(MalformedURLException e1){

        }catch(IOException e2){};
        System.out.println(this.toString()+" Betting Service returned "+uploadStatus.toString());

    }

    public Boolean getUploadStatus() {
        return uploadStatus;
    }

}
