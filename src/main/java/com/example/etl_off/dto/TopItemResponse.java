package com.example.etl_off.dto;

/**
 * REST representation of a frequently used reference value.
 */
public class TopItemResponse {

    private String nom;
    private long occurrences;

    public TopItemResponse(String nom, long occurrences) {
        this.nom = nom;
        this.occurrences = occurrences;
    }

    public String getNom() {
        return nom;
    }

    public long getOccurrences() {
        return occurrences;
    }
}
