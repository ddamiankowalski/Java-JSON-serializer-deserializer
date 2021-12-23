package com.example.jsonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SerachWordsTest extends TestCase {
    Dictionary dictionary = new Dictionary();

    @Before
    public void setUp() throws IOException {
        dictionary.setLanguage("English");
        dictionary.addWord("abortion");
    }

    // the test below checks whether the function searchWord retuns the String if the word does exist in the dictionary
    @Test
    public void testSearchWords() {
        Assert.assertEquals(dictionary.searchWord("abortion"), "abortion");
    }

    // the test below checks whether the function searchWord returns null if the word does not exist in the dictionary
    @Test
    public void testSearchWordsNull() {
        Assert.assertEquals(dictionary.searchWord("foo"), null);
    }

    // the test below checks whether the function gives back the right language
    @Test
    public void testGetLanguage() {
        Assert.assertEquals(dictionary.getLanguage(), "English");
    }
}