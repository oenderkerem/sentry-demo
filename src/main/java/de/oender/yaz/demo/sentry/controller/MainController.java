package de.oender.yaz.demo.sentry.controller;

import de.oender.yaz.demo.sentry.persistancy.DataBaseSimulator;
import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.UserFeedback;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;


@Controller
public class MainController {

    @Autowired
    private DataBaseSimulator dataBaseSimulator;

    @GetMapping("")
    public String root(Model model) {
        return "index";
    }

    @GetMapping("index")
    public String getIndex(Model model) {
        return "index";
    }

    @PostMapping("rating")
    public String postFeedback(@RequestParam String stars, RedirectAttributes redirectAttributes) {
        try {
            System.out.println((Integer.parseInt(stars)));
            redirectAttributes.addFlashAttribute("rating_message", "Danke für das Feedback");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("rating_error_message", "Fehler aufgetreten!");
            Sentry.captureException(exception);
        }
        return "redirect:/index#rating";
    }

    @PostMapping("unhandled")
    public String postUnHandledException(){
        createError();
        return "index";
    }

    @PostMapping("handled_exception")
    public String postHandledException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("unh_error_message", "Fehler aufgetreten");
        return "redirect:/index#handledException";
    }

    @PostMapping("user")
    public String postUserExample(RedirectAttributes redirectAttributes) {
        Sentry.configureScope(scope -> {
            User user = new User();
            user.setUsername("Some Username");
            user.setIpAddress("IP ADRESS CAN BE ENTERED HERE");
            user.setId("USER ID");
            user.setEmail("logged_in_user@example.com");
            user.setOthers(
                    Map.of("Registered since","Project Start",
                    "Average product usage behaviour","Very often")
            );
            scope.setUser(user);
        });
        Sentry.captureException(new Exception("User Example Exception"));
        redirectAttributes.addFlashAttribute("user_error_message", "Fehler mit Userdaten an Sentry erfolgreich gemeldet");
        return "redirect:/index#user";
    }

    @PostMapping("message")
    public String captureMessage(@RequestParam String message, RedirectAttributes redirectAttributes) {
        Sentry.captureMessage(message);
        redirectAttributes.addFlashAttribute("sentMessage", String.format("Die Nachricht '%s' wurde erfolgreich an Sentry übermittelt.",message));
        return "redirect:/index#message";
    }

    @PostMapping("error_feedback")
    public String postUserErrorFeedback(RedirectAttributes redirectAttributes, @RequestParam String name, @RequestParam String email, @RequestParam String comment){
        SentryId sentryId = Sentry.captureMessage("Error Feedback Demo");
        UserFeedback userFeedback = new UserFeedback(sentryId);
        userFeedback.setComments(comment);
        userFeedback.setEmail(email);
        userFeedback.setName(name);
        Sentry.captureUserFeedback(userFeedback);
        redirectAttributes.addFlashAttribute("message","Vielen Dank für das Feedback");
        return "redirect:/index";
    }

    @GetMapping("error_feedback")
    public String getUserFeedbackExample(){
        return "error1";
    }

    @PostMapping("context")
    public String addContext(RedirectAttributes redirectAttributes){
        Sentry.configureScope(scope -> {
            scope.setContexts("isLoggedIn",false);
            scope.setContexts("operation mode", "Demo");
            scope.setContexts("user.age", 13);
        });
        Sentry.captureException(new Exception("Add Context Example"));
        redirectAttributes.addFlashAttribute("addContextExample_error_message","Beispiel mit Kontext wurde erfolgreich an Sentry übertragen");
        return "redirect:/index#context";
    }


    private void createError(){
        int x = 5/0;
    }
}
