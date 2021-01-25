package de.oender.yaz.demo.sentry.models;

import java.util.List;

public class Job {

    private final String id;
    private final List<Document> documents;
    private boolean isJobFinished;

    public Job(String id,List<Document> documents){
        this.id = id;
        this.documents = documents;
        this.isJobFinished = false;
    }

    public String getId() {
        return id;
    }

    public void setJobFinished(boolean jobFinished) {
        isJobFinished = jobFinished;
    }

    public boolean isJobFinished() {
        return isJobFinished;
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
