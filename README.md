# Spectrum

Use various classifiers to determine the political sentiment of posts from Twitter and Reddit.

## Installation

### application.conf

- Set Twitter and Reddit consumer keys
- Configure MongoDB URI

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

## Testing

```
sbt test
```
