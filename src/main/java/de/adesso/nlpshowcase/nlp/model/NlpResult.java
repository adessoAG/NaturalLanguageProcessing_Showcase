package de.adesso.nlpshowcase.nlp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NlpResult {

    private String rawText;

    private List<AnnotatedSentences> annotatedSentences;

    /**
     * Convenience method to directly collect all annotated words from the sequence of annotated sentences.
     *
     * @return all annotated words
     */
    public List<AnnotatedWord> collectAllAnnotatedWords() {
        return this.getAnnotatedSentences().stream()
                .flatMap(listOfSentences ->
                        listOfSentences.getAnnotatedWords().stream())
                .collect(Collectors.toList());
    }
}
