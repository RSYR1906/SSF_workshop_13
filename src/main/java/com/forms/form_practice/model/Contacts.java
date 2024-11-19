package com.forms.form_practice.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class Contacts { // all the business logic will be here

    public String generateID() {

        final String HEX_CHARACTERS = "0123456789abcdef";
        final SecureRandom random = new SecureRandom();

        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(HEX_CHARACTERS.length());
            hexString.append(HEX_CHARACTERS.charAt(randomIndex));
        }
        return hexString.toString();
    }

    public void createFileAndWrite(String directory, String id, User user) throws IOException {
        File file = new File(directory, id);

        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) { // true for appending
            pw.println("User Details:");
            pw.println("Unique ID: " + id);
            pw.println("Name: " + user.getName());
            pw.println("Email: " + user.getEmail());
            pw.println("Phone number: " + user.getPhoneNumber());
            if (user.getDateOfBirth() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDateOfBirth = user.getDateOfBirth().format(formatter);
                pw.println("Date of birth: " + formattedDateOfBirth);
            }
            pw.println("-----");
        }
    }

    public static User findUserFromFile(File file) throws IOException {

        User user = new User();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("Name")) {
                user.setName(line.substring(6).trim());
            } else if (line.startsWith("Email")) {
                user.setEmail(line.substring(7).trim());
            } else if (line.startsWith("Phone number")) {
                user.setPhoneNumber(line.substring(14).trim());
            } else if (line.startsWith("Date of birth")) {
                String dob = line.substring(15).trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                user.setDateOfBirth(LocalDate.parse(dob, formatter));
            }
        }
        return user;
    }

}
