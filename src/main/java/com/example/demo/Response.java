package com.example.demo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Response {
    int messageCount = 1;
    private List<String> response = new LinkedList<>();
    private List<String> emptyResponse = Collections.singletonList("No records were found in this input combination");

    void addToResponse(String transactionDate, String transactionId, Double transactionAmount, String transactionType,
                       String clientName, String clientLastName) {
        this.response.add(
                String.format(
                        "%d. (Data transakcji: %s, Identyfikator transakcji: %s, Kwota transakcji: %f rodzaj rachunku: %s, Imię zlecającego: %s, Nazwisko Zlecającego: %s",
                        messageCount, transactionDate, transactionId, transactionAmount, transactionType, clientName, clientLastName));
        this.messageCount++;
    }

    List<String>returnEmptyResponse(){
        return emptyResponse;
    }

    List<String> getResponse() {
        return response;
    }
}
