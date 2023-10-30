package moodjournal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MoodJournal {
    private List<MoodEntry> moodEntries;
    private static final String DATA_FILE = "mood_entries.json";
    private MoodDataExporter dataExporter;

    public MoodJournal() {
        moodEntries = new ArrayList<>();
        dataExporter = new MoodDataExporter();
        loadMoodEntries();
    }

    public void addMoodEntry(int moodRating, String notes) {
        MoodEntry entry = new MoodEntry(new Date(), moodRating, notes);
        moodEntries.add(entry);
        saveMoodEntries();
    }

    public List<MoodEntry> getMoodEntries() {
        return moodEntries;
    }

    public void addUserEntry() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter your mood rating (1-10): ");
            int moodRating = scanner.nextInt();

            scanner.nextLine();  // Consume the newline
            System.out.print("Add any notes (optional): ");
            String notes = scanner.nextLine();

            addMoodEntry(moodRating, notes);
        }

        System.out.println("Mood entry added successfully.");
    }

    private void saveMoodEntries() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            gson.toJson(moodEntries, new TypeToken<List<MoodEntry>>() {}.getType(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMoodEntries() {
        try (Reader reader = new FileReader(DATA_FILE)) {
            Gson gson = new Gson();
            moodEntries = gson.fromJson(reader, new TypeToken<List<MoodEntry>>() {}.getType());
        } catch (IOException e) {
            // Ignore if the file doesn't exist or can't be read
        }
    }

    public void exportMoodData() {
        dataExporter.exportMoodData(moodEntries);
    }

    public void importMoodData() {
        moodEntries = dataExporter.importMoodData();
    }

    public static void main(String[] args) {
        MoodJournal moodJournal = new MoodJournal();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Mood Journal Menu:");
            System.out.println("1. Add Mood Entry");
            System.out.println("2. View Mood Entries");
            System.out.println("3. Export Mood Data");
            System.out.println("4. Import Mood Data");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline

                switch (choice) {
                    case 1:
                        moodJournal.addUserEntry();
                        break;
                    case 2:
                        List<MoodEntry> entries = moodJournal.getMoodEntries();
                        System.out.println("Mood Entries:");
                        for (MoodEntry entry : entries) {
                            System.out.println("Date: " + entry.getDate());
                            System.out.println("Mood Rating: " + entry.getMoodRating());
                            System.out.println("Notes: " + entry.getNotes());
                            System.out.println();
                        }
                        break;
                    case 3:
                        moodJournal.exportMoodData();
                        break;
                    case 4:
                        moodJournal.importMoodData();
                        break;
                    case 5:
                        System.out.println("Exiting the Mood Journal.");
                        exit = true; // Setting the flag to exit the loop
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } else {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
        scanner.close();
    }
}
