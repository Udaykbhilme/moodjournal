package moodjournal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

public class MoodDataExporter {
    private static final String EXPORT_FILE_PATH = "mood_journal_data.json";

    public static void exportMoodData(List<MoodEntry> moodEntries) {
        try (Writer writer = new FileWriter(EXPORT_FILE_PATH)) {
            Gson gson = new Gson();
            Type moodEntryListType = new TypeToken<List<MoodEntry>>() {}.getType();
            String json = gson.toJson(moodEntries, moodEntryListType);
            writer.write(json);
            System.out.println("Mood data exported to " + EXPORT_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to export mood data.");
        }
    }

    public static List<MoodEntry> importMoodData() {
        try {
            Gson gson = new Gson();
            Type moodEntryListType = new TypeToken<List<MoodEntry>>() {}.getType();
            List<MoodEntry> moodEntries = gson.fromJson(EXPORT_FILE_PATH, moodEntryListType);
            return moodEntries;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to import mood data.");
            return null;
        }
    }
}
