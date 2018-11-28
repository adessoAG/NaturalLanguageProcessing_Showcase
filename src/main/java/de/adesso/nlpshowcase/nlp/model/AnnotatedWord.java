package de.adesso.nlpshowcase.nlp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnotatedWord {

    private String word;

    private String partOfSpeechTag;

    private String namedEntityRecognitionTag;

    private String coreferenceMention;

}
