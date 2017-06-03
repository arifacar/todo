package com.arifacar.domain.model.quote;

public class Value {

    private Integer id;

    private String quote;

    public Value() {
    }

    public Value(Integer id, String quote) {
        this.id = id;
        this.quote = quote;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}