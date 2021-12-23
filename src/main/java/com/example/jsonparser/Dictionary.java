package com.example.jsonparser;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.jsonparser.Main.mapper;

public class Dictionary {

    private List<String> words = new ArrayList<>();
    private String language;
    @JsonIgnore
    private List<String> history = new ArrayList<>();

    // methods
    public void addWord(String word) {
        words.add(word);
    }

    public String searchWord(String word) {
        String result;
        return result = words.stream()
                .filter(element -> element.equalsIgnoreCase(word))
                .findFirst()
                .orElse(null);
    }

    public void setHistory(String word) {
        history.add(word);
    }

    public List<String> getHistory() {
        return this.history;
    }

    public void exportJson() throws IOException {
        mapper.writeValue(new File("src/main/java/com/example/jsonparser/" + this.language + ".json"), this);
    }

    // getters and setters
    public List<String> getWords() {
        return this.words;
    }
    public String getLanguage() {
        return this.language;
    }
    public void setWord(String word) {
        this.words.add(word);
    }
    public void setLanguage(String language) {
        this.language = language;
    }
}
