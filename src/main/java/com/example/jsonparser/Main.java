package com.example.jsonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ObjectMapper mapper = new ObjectMapper();
    static List<Dictionary> dictionaries = new ArrayList<>();

    // search for words in all dictionaries
    static ArrayList<String> searchWords(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for (Dictionary dictionary : dictionaries) {
            String entry = dictionary.searchWord(word);
            if(entry != null) {
                result.add(entry);
                dictionary.setHistory(entry);
                System.out.println("The word " + word + " is in the " + dictionary.getLanguage() + " dictionary!");
            }
        }
        return result;
    }

    // add a new word/dictionary
    static void addEntry(String word, String language) throws IOException{
        boolean isNewLanguage = true;
        for(int i = 0 ; i < dictionaries.size(); i++) {
            if(dictionaries.get(i).getLanguage().equalsIgnoreCase(language)) {
                isNewLanguage = false;
            }
        }
        if(isNewLanguage) {
            Dictionary newDictionary = new Dictionary();
            newDictionary.setLanguage(language);
            newDictionary.setWord(word);
            newDictionary.exportJson();
            dictionaries.add(newDictionary);
            System.out.println("A " + language + " has been created and a word " + word + " assigned to it!");
        } else {
            for(int i = 0; i < dictionaries.size(); i++) {
                if(dictionaries.get(i).getLanguage().equalsIgnoreCase(language)) {
                    dictionaries.get(i).addWord(word);
                    dictionaries.get(i).exportJson();
                    System.out.println("The word " + word + " has been added to a " + dictionaries.get(i).getLanguage() + " dictionary!");
                }
            }
        }
    }

    // get all dictionaries
    static void getAllDictionaries() throws IOException {
        String folderPath = "src/main/java/com/example/jsonparser";
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if(file.isFile()) {
                String fileName = file.getName();
                String jsonExtension = fileName.substring(fileName.length() - 5);
                if(jsonExtension.equalsIgnoreCase(".json")) {
                    Dictionary dictionary = mapper.readValue(new File(folderPath + "/" + fileName), Dictionary.class);
                    dictionaries.add(dictionary);
                }
            }
        }
    }

    // main class
    public static void main(String[] args) throws IOException, DocumentException {
        getAllDictionaries();
        String input;
        do {
            input = scanner.next();
            if(input.charAt(0) == '*') {
                // adding a new word
                System.out.println("You're adding a new word. Please confirm the language:");
                String inputLanguage = scanner.next();
                System.out.println("Please confirm your word:");
                String inputWord = scanner.next();
                addEntry(inputWord, inputLanguage);
            } else {
                // searching for a given word
                searchWords(input);
            }
        } while(!input.equalsIgnoreCase("!"));

        // PDF section
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("pdffile.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
        Chunk chunk = new Chunk("This is a report of all the words that have been typed in:", font);
        document.add(chunk);
        document.add(new Paragraph());

        for (Dictionary dictionary : dictionaries) {
            document.add(new Paragraph("\n\n"));
            font = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            chunk = new Chunk("LANGUAGE: " + dictionary.getLanguage() + "\n", font);
            document.add(chunk);
            List<String> historyOfDict = dictionary.getHistory();
            if(historyOfDict.isEmpty()) {
                chunk = new Chunk("No words have been typed in for this dictionary", font);
                document.add(chunk);
                document.add(new Paragraph());
            }
            for (String s : historyOfDict) {
                chunk = new Chunk(s, font);
                document.add(chunk);
                document.add(new Paragraph());
            }
        }

        document.close();
    }
}
