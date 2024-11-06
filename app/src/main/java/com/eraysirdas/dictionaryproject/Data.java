package com.eraysirdas.dictionaryproject;

import com.google.firebase.Timestamp;

public class Data {
    public String word;
    public String wordMeaning;
    public String user;
    public Timestamp date;

    public Data(String word, String wordMeaning, String user,Timestamp date) {
        this.word = word;
        this.wordMeaning = wordMeaning;
        this.user = user;
        this.date=date;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public void setWordMeaning(String wordMeaning) {
        this.wordMeaning = wordMeaning;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
