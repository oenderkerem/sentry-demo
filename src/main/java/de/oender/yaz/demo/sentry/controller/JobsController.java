package de.oender.yaz.demo.sentry.controller;

import de.oender.yaz.demo.sentry.persistancy.DataBaseSimulator;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class JobsController {

    @Autowired
    DataBaseSimulator dataBaseSimulator;

    @GetMapping("jobs")
    public String getJobs(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("jobs",dataBaseSimulator.getJobs());
        return "redirect:/index#breadcumbs";
    }

    @PostMapping("markDocumentInvalid")
    public String markDocumentInvalid(@RequestParam String id, RedirectAttributes redirectAttributes){
        try{
            Sentry.configureScope(scope -> {
                scope.setContexts("Employee Action","Document changed");
            });
            Sentry.addBreadcrumb(String.format("Document %s marked invalid",id));
            dataBaseSimulator.markAsInvalid(id);
        }
        catch (Exception exception){
            redirectAttributes.addFlashAttribute("document_error","Es ist ein Fehler aufgetreten!");
        }
        redirectAttributes.addFlashAttribute("jobs",dataBaseSimulator.getJobs());
        return "redirect:/index#breadcumbs";
    }

    @PostMapping("doJob")
    public String doJob(@RequestParam String jobId, RedirectAttributes redirectAttributes){
        try {
            Sentry.addBreadcrumb(String.format("Job with id %s was started ",jobId));
            dataBaseSimulator.doJob(jobId);
        }catch (Exception exception){
            Sentry.captureException(exception);
            redirectAttributes.addFlashAttribute("jobs_error","Es ist ein Fehler aufgetreten!");
        }
        redirectAttributes.addFlashAttribute("jobs",dataBaseSimulator.getJobs());
        return "redirect:/index#breadcumbs";
    }

}
