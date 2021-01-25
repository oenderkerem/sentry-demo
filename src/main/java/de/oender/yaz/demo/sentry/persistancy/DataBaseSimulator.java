package de.oender.yaz.demo.sentry.persistancy;

import de.oender.yaz.demo.sentry.models.Document;
import de.oender.yaz.demo.sentry.models.Job;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataBaseSimulator {

    List<Document> documents = List.of(new Document("d1","Dokument A"),new Document("d2","Dokument B"));
    List<Job> jobs = List.of(new Job("j1",List.of(documents.get(0),documents.get(1))),
            new Job("j2",List.of(documents.get(0)))
    );

    public List<Job> getJobs(){
        return jobs;
    }

    public void markAsInvalid(String id)throws Exception{
        Document document = documents.stream().filter(document1 -> id.equals(document1.getId())).findAny().orElse(null);
        if(document==null){throw new Exception("Invalid Input");}
        document.markAsInvalid();
    }

    public void doJob(String id){
            Job job = jobs.stream()
                    .filter(j -> id.equals(j.getId()))
                    .findAny()
                    .orElse(null);
            var documents = job.getDocuments();
            documents.forEach(document -> document.process());
            job.setJobFinished(true);
    }
}
