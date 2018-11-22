[travis-image]: https://travis-ci.org/adessoAG/natural-language-processing-showcase.svg?branch=master
[travis-url]: https://travis-ci.org/adessoAG/natural-language-processing-showcase

# Natural Language Processing Showcase [![Travis Build Status][travis-image]][travis-url]

A spring boot web application to demonstrate Natural Language Processing (NLP) capabilities in Java.

##  Testing system

There's a testing system provided by heroku where the most recent application is deployed continously.

[Try it out](https://adesso-nlp-showcase.herokuapp.com/)

## NLP adapters

Beside the showcase, the idea behind the project is also to offer a stable API to run NLP tasks. The underlying adapters are designed to use different sub systems to solve the NLP tasks.

### Stanford Core NLP library

Currently, there's only one supported Natural Language Processing (NLP) adapter which uses the Stanford Core NLP library.
And it only supports German raw texts right now.

The implementation is provided [here](src/main/java/de/adesso/nlpshowcase/nlp/external/adapter/StanfordCoreNlpAdapter).

## Local development

It's a gradle project shipped with the gradle wrapper (gradlew). 

To build the project, run: 

```bash
gradlew build
```

To start the web application locally, run:

```bash
gradlew bootrun
```

Run all tests:

```bash
gradlew check
```


