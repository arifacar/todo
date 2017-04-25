package com.arifacar.domain.model;

import javax.persistence.Entity;
import javax.xml.transform.stream.StreamResult;

@Entity
public class TestResult {
    public StreamResult content;

    public StreamResult getContent() {
        return content;
    }

    public void setContent(StreamResult content) {
        this.content = content;
    }
}
