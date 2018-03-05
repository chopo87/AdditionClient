package com.deontics.AdditionClient;

import com.deontics.AdditionClient.http.HttpService;
import com.deontics.AdditionAPI.models.ApiAbstractTransferModel;
import com.deontics.AdditionAPI.models.ApiRequestArray;
import com.deontics.AdditionAPI.models.ApiSession;
import com.deontics.AdditionAPI.models.ApiTransferContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientRunner {

    public static void main(String[] args) {

        String URI = "http://localhost:8080";
        String domainVertical = "AdditionEngine";
        ApiSession session;
        ApiTransferContainer atcResponce;
        int nJobs;


        Scanner scanner = new Scanner(System.in);

        System.out.println("Please confirm that the AdditionAPI server is located at the following URI: " + URI + " (y/n)");

        while (!scanner.hasNext("y")) {
            scanner.next();
            System.out.println("Please enter the correct URI including protocol and port");
            URI = scanner.next();
            System.out.println("Please confirm that the AdditionAPI server is located at the following URI: " + URI + " (y/n)");
        }

        System.out.println("Thank you\n> Initiating session with AdditionAPI server...");

        try {
            session = HttpService.getSession(URI, domainVertical);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, sorry, good bye...");
            return;
        }

        System.out.println("Session " + session.getId() + " successfully established");

        while (!scanner.hasNextInt()) {
            System.out.println("\nPlease specify the number of jobs to send (integer)");
            scanner.next();
        }

        nJobs = scanner.nextInt();
        System.out.println("Ok, lets prepare a batch of " + nJobs);

        List<ApiAbstractTransferModel> aral = new ArrayList<>();

        for (int i = 0; i < nJobs; i++) {
            ApiRequestArray ara = new ApiRequestArray();
            System.out.println("\nOk, lets generate array " + i);
            while (!scanner.hasNextDouble()) {
                System.out.println("Please specify the first number number of array " + i);
                scanner.next();
            }
            ara.addToSumList(scanner.nextDouble());
            while (true) {
                System.out.println("Now add another number or a character to finish array " + i);
                if (scanner.hasNextDouble())
                    ara.addToSumList(scanner.nextDouble());
                else break;
            }
            aral.add(ara);
        }

        ApiTransferContainer atc = new ApiTransferContainer(session, aral);

        System.out.println("Thank you\n> Getting ready to send transmission to server...");
        try {
            atcResponce = HttpService.postArrays(URI, domainVertical, atc);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, sorry, good bye...");
            return;
        }

        if(atcResponce.getSession().getStatus() == ApiSession.SessionStatus.AWAITING_RETRIEVAL) {
            System.out.println("Server batch is ready, lets get it...");
            try {
                HttpService.getBatchResults(URI, domainVertical, session);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Something went wrong, sorry, good bye...");
                return;
            }
        }

        System.out.println("Ok, all done! Cheerio...");

    }
}
