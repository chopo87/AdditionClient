package com.deontics.AdditionClient.http;

import com.deontics.AdditionAPI.models.ApiAbstractModel;
import com.deontics.AdditionAPI.models.ApiSession;
import com.deontics.AdditionAPI.models.ApiTransferContainer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpService {

    public static ApiSession getSession(String URI, String domainVertical) throws Exception {

        URL url = new URL(URI+"/"+domainVertical);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("\nSending 'GET' request to URL : " + url);
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        //Decode Response Body
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Server response message:");
        System.out.println(response.toString());

        return (ApiSession) ApiAbstractModel.getObjectMapper().readValue(response.toString(), ApiSession.class);

    }

    public static ApiTransferContainer getBatchResults(String URI, String domainVertical, ApiSession session) throws Exception {

        URL url = new URL(URI+"/"+domainVertical+"/"+session.getId());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("\nSending 'GET' request to URL : " + url);
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        //Decode Response Body
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Server response message:");
        System.out.println(response.toString());

        return (ApiTransferContainer) ApiAbstractModel.getObjectMapper().readValue(response.toString(), ApiTransferContainer.class);
    }

    public static ApiTransferContainer postArrays(String URI, String domainVertical, ApiTransferContainer atc) throws Exception {

        URL url = new URL(URI+"/"+domainVertical);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // Set Request Body
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(atc.toJson());
        wr.flush();
        wr.close();

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("here is the JSON payload:\n" + atc.toJson());
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        //Decode Response Body
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Server response message:");
        System.out.println(response.toString());

        return (ApiTransferContainer) ApiAbstractModel.getObjectMapper().readValue(response.toString(), ApiTransferContainer.class);
    }
}
