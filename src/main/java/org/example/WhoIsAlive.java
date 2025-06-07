package org.example;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

class Person {
    @SerializedName("n")
    String name;

    @SerializedName("b")
    Integer birth; // Nullable Integer to handle null values

    @SerializedName("a")
    Integer ageAtDeath; // Nullable Integer to handle null values

    // Constructor for validation
    public Person(String name, Integer birth, Integer ageAtDeath) {
        this.name = name;
        this.birth = birth != null ? birth : 0; // Default to 0 if null
        this.ageAtDeath = ageAtDeath != null ? ageAtDeath : 0; // Default to 0 if null
    }
}

public class WhoIsAlive {
    private static final List<Person> people = new ArrayList<>();

    public static void load() {
        Gson gson = new Gson();
        try (InputStream is = WhoIsAlive.class.getClassLoader().getResourceAsStream("people.json")) {
            if (is == null) {
                throw new FileNotFoundException("people.json not found in resources");
            }
            Reader reader = new InputStreamReader(is);
            Person[] personArray = gson.fromJson(reader, Person[].class);
            if (personArray != null) {
                for (Person p : personArray) {
                    if (p != null && p.name != null && p.birth != null && p.ageAtDeath != null) {
                        people.add(new Person(p.name, p.birth, p.ageAtDeath));
                        System.out.println(p.name + " was born on " + p.birth + " and lived for " + p.ageAtDeath + " years.");
                    } else {
                        System.out.println("Skipping invalid entry due to null or empty values.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void find(LocalDate start) {
        LocalDate from = start != null ? start : LocalDate.of(1980, 1, 1);
        LocalDate currentDate = LocalDate.now(); // Current date: 2025-06-07

        for (Person p : people) {
            if (p.birth == null || p.ageAtDeath == null) continue; // Skip if null
            LocalDate birth = LocalDate.of(p.birth, 1, 1); // Assuming birth year only
            long daysLived = p.ageAtDeath * 365L; // Rough estimate of days lived
            LocalDate death = birth.plusDays(daysLived);

            if (!death.isBefore(from) && !birth.isAfter(currentDate)) {
                System.out.println(p.name + " was alive during this time period.");
            }
        }
    }
}