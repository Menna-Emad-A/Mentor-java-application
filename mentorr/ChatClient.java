package com.example.mentorr;

import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 12345); // Connect to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.println("Connected to server. Type a message:");

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput); // Send message to the server
                System.out.println("Server response: " + in.readLine()); // Print server's response
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
