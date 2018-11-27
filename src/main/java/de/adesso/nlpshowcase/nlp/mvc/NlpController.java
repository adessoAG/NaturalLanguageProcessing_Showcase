package de.adesso.nlpshowcase.nlp.mvc;

import de.adesso.nlpshowcase.nlp.model.NlpResult;
import de.adesso.nlpshowcase.nlp.service.NlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Spring MVC controller using Thymeleaf as view rendering engine.
 */
@Controller
public class NlpController {

    private final NlpService nlpService;

    @Autowired
    public NlpController(NlpService nlpService) {
        this.nlpService = nlpService;
    }

    @GetMapping("/")
    public String getNlpForm(Model model) {
        createInitialNlpParams(model);

        return "nlp";
    }

    @PostMapping("/")
    public String processRawText(@ModelAttribute NlpParams nlpParams, Model model) {
        NlpResult nlpResult = nlpService.annotate(nlpParams.getRawText());
        model.addAttribute("nlpResult", nlpResult);
        model.addAttribute("nlpParams", nlpParams);

        return "nlp";
    }

    private void createInitialNlpParams(Model model) {
        model.addAttribute("nlpParams", NlpParams
                .builder()
                .rawText("Max Mustermann wohnt in Berlin. Dort hat auch die Bundesregierung ihren Hauptsitz.")
                .build());
    }

}
