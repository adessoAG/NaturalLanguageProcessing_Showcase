package de.adesso.nlpshowcase.nlp.service;

import de.adesso.nlpshowcase.nlp.external.adapter.StanfordCoreNlpAdapter;
import de.adesso.nlpshowcase.nlp.model.AnnotatedSentences;
import de.adesso.nlpshowcase.nlp.model.AnnotatedWord;
import de.adesso.nlpshowcase.nlp.model.NlpResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests the {@link NlpService} component using Spring runner.
 */
@RunWith(SpringRunner.class)
@Import({StanfordCoreNlpAdapter.class, NlpService.class})
public class NlpServiceTest {

    @Autowired
    private NlpService nlpService;

    @Test
    public void annotate_shouldAnnotateGermanRawText() throws Exception {
        // given
        final String givenRawText = "Berlin ist die Hauptstadt von Deutschland.";

        // when
        NlpResult nlpResult = nlpService.annotate(givenRawText);

        // then
        assertThat(nlpResult).isNotNull();
        assertThat(nlpResult.getRawText()).isEqualTo(givenRawText);
        assertThat(nlpResult.getAnnotatedSentences().size()).isEqualTo(1);

        AnnotatedSentences firstSentence = nlpResult.getAnnotatedSentences().get(0);
        assertThat(firstSentence.getAnnotatedWords().size()).isGreaterThan(6);

        AnnotatedWord firstWord = firstSentence.getAnnotatedWords().get(0);
        assertThat(firstWord.getWord()).isEqualTo("Berlin");
        assertThat(firstWord.getPartOfSpeechTag()).isEqualTo("NE");
        assertThat(firstWord.getNamedEntityRecognitionTag()).isEqualTo("LOCATION");
    }


//    @Test
//    public void annotate_shouldAnnotateEnglishRawText() throws Exception {
//        // given
//        final String givenRawText = "Berlin is the capital of Germany.";
//
//        // when
//        NlpResult nlpResult = nlpService.annotate(givenRawText);
//
//        // then
//        assertThat(nlpResult).isNotNull();
//        assertThat(nlpResult.getRawText()).isEqualTo(givenRawText);
//        assertThat(nlpResult.getAnnotatedSentences().size()).isEqualTo(1);
//
//        AnnotatedSentences firstSentence = nlpResult.getAnnotatedSentences().get(0);
//        assertThat(firstSentence.getAnnotatedWords().size()).isGreaterThan(6);
//
//        AnnotatedWord firstWord = firstSentence.getAnnotatedWords().get(0);
//        assertThat(firstWord.getWord()).isEqualTo("Berlin");
//        assertThat(firstWord.getPartOfSpeechTag()).isEqualTo("NNP");
//        assertThat(firstWord.getNamedEntityRecognitionTag()).isEqualTo("CITY");
//    }
}