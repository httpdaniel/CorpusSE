# Corpus Search Engine

A Java implementation of a Lucene-based search engine. The corpus for this engine is a collection of news articles aggregated from 4 different sources. 

1. Financial Times Limited (1991, 1992, 1993, 1994)
2. Federal Register (1994)
3. Foreign Broadcast Information Service (1996)
4. Los Angeles Times (1989, 1990)

## Building the project

``` sh
$ git clone https://github.com/httpdaniel/CorpusSE.git

$ cd CorpusSE

$ mvn package
```

## Creating the index

``` sh
$ java -cp target/CorpusSE-1.0-SNAPSHOT.jar CreateIndex
```

## Querying the index

``` sh
$ java -cp target/CorpusSE-1.0-SNAPSHOT.jar CorpusSearch
```
The results will be outputted to a file "Results.txt" in the corpus folder

## Evaluating the results

``` sh
$ cd corpus
$ ./trec_eval-9.0.7/trec_eval qrels.assignment2.part1 Results.txt
```

## To display only Mean Average Precision & Recall

``` sh
$ ./trec_eval-9.0.7/trec_eval -m qrels.assignment2.part1 Results.txt
```
