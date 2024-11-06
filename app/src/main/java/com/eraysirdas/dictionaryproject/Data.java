package com.eraysirdas.dictionaryproject;

import com.google.firebase.Timestamp;

public class Data {
    private String documentId;
    private String uid;
    private String word;
    private String wordMeaning;
    private String user;
    private Timestamp date;

    public Data(String documentId,String uid,String word, String wordMeaning, String user,Timestamp date) {
        this.uid=uid;
        this.documentId=documentId;
        this.word = word;
        this.wordMeaning = wordMeaning;
        this.user = user;
        this.date=date;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
