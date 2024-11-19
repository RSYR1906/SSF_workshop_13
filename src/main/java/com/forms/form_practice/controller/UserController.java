package com.forms.form_practice.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.forms.form_practice.model.Contacts;
import com.forms.form_practice.model.User;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Value("${dataDir:default-directory}") // Inject the directory path
    private String directoryPath;

    private final Contacts contacts;

    public UserController(Contacts contacts) {
        this.contacts = contacts;
    }

    @GetMapping("/")
    public String showForm(@ModelAttribute("user") User user, Model model) {
        return "index"; // Form page template
    }

    @PostMapping("/contacts")
    public String processUserRegistration(@Valid User user, BindingResult binding, Model model) throws IOException {

        if (binding.hasErrors()) {
            binding.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "index"; // Return to the form if there are validation errors
        }

        String generatedID = contacts.generateID();

        // Create file with generated ID and save user data
        contacts.createFileAndWrite(directoryPath, generatedID, user);

        // Add user data to the model for rendering the view
        model.addAttribute("user", user);
        model.addAttribute("generatedID", generatedID);
        return "user";
    }

    @GetMapping("/contacts/{id}")
    public String searchContact(@PathVariable String id, Model model) throws IOException {

        File file = new File(directoryPath, id);

        // Check if the file exists
        if (file.exists()) {
            System.out.println("File exists!");
            User user = contacts.findUserFromFile(file);

            model.addAttribute("user", user);
            model.addAttribute("generatedID", id);
            return "user";
        } else {
            System.out.println("File does not exist.");
            return HttpStatus.NOT_FOUND + ": File is not found.";
        }
    }

    @GetMapping("/contacts/contactslist")
    public String listContacts(Model model) {

        File dataDir = new File(directoryPath);
        System.out.println("Files in Directory: " + Arrays.toString(dataDir.list()));

        // Ensure the directory exists and is readable
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.err.println("Data directory does not exist or is not a directory: " + directoryPath);
            model.addAttribute("contacts", new ArrayList<>()); // Send an empty list
            return "contacts"; // Return the empty page
        }

        File[] contactFiles = dataDir.listFiles((dir, name) -> name.matches("[0-9a-fA-F]{8}")); // Only valid ID files
        List<Map<String, String>> contactsList = new ArrayList<>();

        if (contactFiles != null) {
            for (File file : contactFiles) {
                try {
                    // Use `findUserFromFile` to extract user details
                    User user = contacts.findUserFromFile(file);

                    // Map user name and ID
                    Map<String, String> contact = new HashMap<>();
                    contact.put("id", file.getName()); // File name (ID)
                    contact.put("name", user.getName()); // User's name
                    contactsList.add(contact);
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getName());
                }
            }
        }

        // Add the list of contacts to the model
        model.addAttribute("contacts", contactsList);
        System.out.println("Contacts passed to model: " + contactsList);

        return "contacts"; // Return contacts.html
    }
}
