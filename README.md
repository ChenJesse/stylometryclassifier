# Stylometry Classifier

## About
This is a platform that leverages Stanford CoreNLP and in-house machine learning functions written with Breeze + feature extraction techniques to classify writing style. Based on stylometry research, the vectorization of so-called segments (groups of n-sentences) is done by looking at parts-of-speech, vocabulary richness, common conjunction usage and common pronoun usage. We were able to achieve accurate validation and test errors classifying on two acclaimed fantasy series, *The Lord of the Rings* and *A Song of Ice and Fire*. Being written decades apart, although they share a genre, the writing styles are different enough for a simple algorithm to differentiate between them. By training on the first and second books of both series respectively, and validating on the third book, we achieved validation score of less than 6%. 

The platform is written with robustness and generalization in mind, so that it can ingest any pieces of writing as txt files, and train classifiers for them. Our implementation of Tolkien vs Martin is simply an example of what the platform can do.

Currently, it supports SVM, logistic regression, and naive bayes.

### Stack
- Play Framework for backend
- Breeze as numerical processing library for writing classifiers
- MongoDB for classifier persistence
- jQuery for frontend

![Alt text](img/architecture.jpg?raw=true "Architecture")


## Installation

### application.conf

- Configure MongoDB URI if necessary. If running on localhost, everything should be configured already for default settings for a db named "testdb".

## Setup

### MongoDB
1. First follow MongoDB basic installation steps [here](https://docs.mongodb.com/getting-started/shell/tutorial/install-mongodb-on-os-x/)

2. In `application.conf`, for default port configuration, set `mongodb.uri = "mongodb://localhost:27017/testdb"`

3. Make sure the Mongo server is running (`mongod`)

## Running

```
sbt run
```

And then go to http://localhost:9000

A lightweight frontend is implemented that ingests snippets of novels from either George RR Martin or J.R.R. Tolkien, and attempts to classify them using our default logistic regression classifier.  

## Testing

```
sbt test
```
