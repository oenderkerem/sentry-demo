package de.oender.yaz.demo.sentry.models;

public class Document {

    private final String id;
    private final String title;
    private Boolean valid;

    public Document(String id, String title){
        this.id = id;
        this.title = title;
        this.valid = true;
    }

    public void markAsInvalid(){
        this.valid = false;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getId() {
        return id;
    }

    public void process(){
        if(!valid){
            int getError = 1/0;
        }
    }
}
