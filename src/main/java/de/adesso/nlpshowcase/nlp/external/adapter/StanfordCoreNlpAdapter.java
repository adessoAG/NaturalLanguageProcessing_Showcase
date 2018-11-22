package de.adesso.nlpshowcase.nlp.external.adapter;

import de.adesso.nlpshowcase.nlp.model.AnnotatedSentences;
import de.adesso.nlpshowcase.nlp.model.AnnotatedWord;
import de.adesso.nlpshowcase.nlp.model.NlpResult;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Adapter between the showcase project and Stanford Core NLP library.
 * Be aware of the Singleton scope of this class since it capsulates the expensive creation of Stanford's linguistic models.
 */
@Component
@Scope("singleton")
@Slf4j
public class StanfordCoreNlpAdapter implements NlpAdapter {

    // Huge German Corpus - for all possible taggers see JAR file stanford-corenlp-3.9.2-models-german.jar
    private static final String GERMAN_PART_OF_SPEECH_MODEL = "edu/stanford/nlp/models/pos-tagger/german/german-hgc.tagger";
    // Huge German Corpus - for all possible NER classifiers see JAR file stanford-corenlp-3.9.2-models-german.jar
    private static final String GERMAN_NAMED_ENTITY_RECOGNITION_MODEL = "edu/stanford/nlp/models/ner/german.conll.germeval2014.hgc_175m_600.crf.ser.gz";
    // MUC-7 class model - for all possible taggers see JAR file stanford-english-corenlp-2016-01-10-models.jar
    private static final String ENGLISH_NAMED_ENTITY_RECOGNITION_MODEL = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
    // English POS model - for all possible NER classifiers see JAR file stanford-english-corenlp-2016-01-10-models.jar
    private static final String ENGLISH_PART_OF_SPEECH_MODEL = "edu/stanford/nlp/models/pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger";

    private StanfordCoreNLP germanNlpPipeline;

    @PostConstruct
    public void initializeStanfordModels() {
        synchronized (this) {
            // be thread-safe. Anyway, if this component is set to singleton and managed by Spring MVC, there shouln't be any multi-threading problem here.
            initializeGermanModels();
        }
    }

    private void initializeGermanModels() {
        Properties stanfordCoreNlpPoperties = new Properties();

        stanfordCoreNlpPoperties.setProperty("annotators", getAnnotatorsProperty());
        // see https://github.com/stanfordnlp/CoreNLP/blob/master/src/edu/stanford/nlp/pipeline/StanfordCoreNLP-german.properties
        stanfordCoreNlpPoperties.put("tokenize.language", "de");
        stanfordCoreNlpPoperties.put("pos.model", GERMAN_PART_OF_SPEECH_MODEL);
        stanfordCoreNlpPoperties.put("ner.model", GERMAN_NAMED_ENTITY_RECOGNITION_MODEL); // Huge German Corpus
        stanfordCoreNlpPoperties.put("ner.useSUTime", "false"); // 	Whether or not to use SUTime. SUTime at present only supports English; if not processing English, make sure to set this to false.
        this.germanNlpPipeline = new StanfordCoreNLP(stanfordCoreNlpPoperties);
    }

    /**
     * Builds the "annotators" property to be put into the {@link StanfordCoreNLP} instance
     * by setting the user's desired annotations (such as Named Entity Recognition and/or Part Of Speech tagging).
     *
     * @return String
     */
    private String getAnnotatorsProperty() {
        final List<String> annotatorProperties = new ArrayList<>();
        // required: tokenizes the raw text (isolates words)
        annotatorProperties.add("tokenize");
        // required: splits sentences.
        annotatorProperties.add("ssplit");
        // activate: Part-of-Speech tagging
        annotatorProperties.add("pos");
        // required for NER: word Lemma
        annotatorProperties.add("lemma");
        // activate: Named Entity Recognition
        annotatorProperties.add("ner");

        return String.join(",", annotatorProperties);
    }

    @Override
    public NlpResult annotate(final String rawText) {
        Annotation annotation = new Annotation(rawText);

        // executes the NLP pipeline and fills the annotation structure
        germanNlpPipeline.annotate(annotation);

        return mapToNlpResult(rawText, annotation);
    }

    private NlpResult mapToNlpResult(String rawText, Annotation annotation) {
        return NlpResult.builder()
                .rawText(rawText)
                .annotatedSentences(mapToAnnotatedSentences(annotation.get(CoreAnnotations.SentencesAnnotation.class)))
                .build();
    }

    private List<AnnotatedSentences> mapToAnnotatedSentences(List<CoreMap> sentences) {
        return sentences.stream().map(sentence -> {
            return AnnotatedSentences.builder()
                    .annotatedWords(mapToAnnotatedWords(sentence))
                    .build();
        }).collect(Collectors.toList());
    }

    private List<AnnotatedWord> mapToAnnotatedWords(CoreMap sentence) {
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

        return tokens.stream().map(token -> {
            return AnnotatedWord.builder()
                    .word(token.get(CoreAnnotations.TextAnnotation.class)) // the raw word itself
                    .partOfSpeechTag(token.get(CoreAnnotations.PartOfSpeechAnnotation.class)) // the POS tag
                    .namedEntityRecognitionTag(token.get(CoreAnnotations.NamedEntityTagAnnotation.class)) // the POS tag
                    .build()
                    ;
        }).collect(Collectors.toList());
    }
}
