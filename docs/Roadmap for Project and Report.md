# Roadmap for Project and Report

## Synopsis

> Task: Given a query on a controversial topic, retrieve relevant arguments from a focused crawl of online debate portals.
>
> Input: args.me corpus
>
> Submission: TIRA

## Goal

We need to 

1. Build a Search Engine with Lucene using the args.me corpus,
2. Research different methodologies for information retrieval,
3. Pick at least 2 of them for our search engine,
4. Apply it to our search engine and evaluate and compare the results,
5. Write the evaluation as a lab report.
6. Create 3 runs in standard TREC format.

### User Story

We need to ...

1. Use Lucene to implement a search engine that can accept user queries and then retrieves arguments from the args.me corpus.
2. Use Lucene to build an index of the corpus.
3. Use the args.me API for a baseline retrieval.
4. Ensure that we retrieve **strong** arguments.
5. Use Docker to convert and submit the final system to TIRA.
6. Create a lab report.



## Task

> The goal of **Task 1** is to support users who search for  arguments to be used in conversations (e.g., getting an overview of pros and cons or just looking for arguments in line with a user's stance). Given a query on a controversial topic, the task is to retrieve relevant arguments from a focused crawl of online debate portals. 

Lucene defines the building of a search engine as follows:

1. Acquire Content
2. Build Document
3. Analyse Document
4. Index Document
5. Construct User Interface
6. Build Query

There already is an existing search engine framework that utilizes the args.me corpus. We can probably utilize much of the source code from it. The repository can be found [here](https://git.webis.de/code-research/arguana/args/args-framework).

Regarding this structure, we need ...

- **No** web crawler. The data we require is already contained in the the args.me corpus.
- To figure out how to read in the data from the `.json` files.
- To Analyse the given fields and construct data structures to support them, namely `Fields` and `Documents`.
- To tokenize the data and do the linguistic pre-processing of tokens.
- To build the index. Specifically, we need to...

  - Build the **inverse index**. 
  - Construct a **stop list**.
  - Perform **token normalization** to optimize the index size.
- **No** implementation of a user interface. Instead we need to use Docker to export the source code for Tira.
- Build a server model and a **Query Parser**.



## Data

> **The topics** for Tasks 1 will be send to each team via email upon completed registration. The topics will be provided as XML files.
>
> **Task 1** will use the [args.me corpus](https://zenodo.org/record/3734893); you may index the corpus with your favorite retrieval system. To ease participation, you may also directly use the [args.me](https://www.args.me/index.html) search engine's [API](https://www.args.me/api-en.html) for a baseline retrieval.

Example topic for **Task 1:**

```xml
<topic>
    <number>1</number>
    <title>Is climate change real?</title>
    <description>You read an opinion piece on how climate change is a hoax and disagree. Now you are looking for arguments supporting the claim that climate change is in fact real.</description>
    <narrative>Arguments will support the given stance that climate change is real or attack a hoax side's argument.</narrative>
</topic>
```

- The email is a lie. Instead we need to use the given [git repository](https://git.webis.de/code-teaching/readings/information-retrieval-ss20-leipzig) to extract the arguments. 
- The [args.me corpus](https://zenodo.org/record/3734893) can be downloaded as .json files online. However, we should be able to 

### Questions

- IIUC, we need to restrict the retrieval and construction of the search engine to the list of given topics.

- `Dataset Touche` contains a file named `topics.xml` with 50 topics. Are these the topics we need to implement?

- Do we need to parse this xml in our source code?

- What are we supposed to do with the other data sets? No.

- Why are the topics structured this way? Are we supposed to use that structure when building the index?

  We only need the number and the title. Title is for the query content.

- What is a baseline retrieval? How are we supposed to use/implement the API?



## Evaluation

> For **Task 1**, be sure to retrieve good  ''strong'' arguments. Our human assessors will label the retrieved documents manually, both for their general topical relevance, and for argument quality dimensions such as: 
>
> 1. whether an argumentative text is logically cogent, 
> 2. whether it is rhetorically well-written, and 
> 3. whether it helps a user in their stance-building process, i.e., somewhat similar to the concept of "utility".

- There are no actual human assessors. We can ignore this part.
- We need at least 2 retrieval models that we can implement and test.
- We need to test, compare and evaluate those two methods. That is the core of the lab report.
- Tira can automate the evaluation process.
- We need to interpret and analyse the results that Tira provides.

The evaluation needs to be tackled in the lab report. It should be structured as follows:

1.  Introduction 
2.  Related Work 
3.  Argument Retrieval Model(s) (i.e. a description of your contribution)
4.  Evaluation 
5.  Discussion and Conclusion 

We need to ... 

- Figure out which two Retrieval Models we want to use.
- Implement that model with Lucene in our source code.

### Questions

- What does **human assessor** mean in this context? Not relevant.
- Are we supposed to analyse and evaluate the contents of the args.me database? Depends on the lab report.
- What are the different argument retrieval models? 



## Submission

> We encourage participants to use [TIRA](https://www.tira.io/) for their submissions to allow for a better reproducibility. Please also have a look at the [dedicated TIRA tutorial for Touché](https://events.webis.de/touche-20/tira-guide-task-1.html)—in case of problems we will be able to assist you. Even though the preferred way of run submission is TIRA, in case of problems you may  also submit runs via email. We will try to quickly review your TIRA or  email submissions and provide feedback.
>
> Runs may be either automatic or manual. An automatic run does not use  the topic descriptions or narratives and must not "manipulate" the topic titles via manual intervention. A manual run is anything that is not an automatic run. Upon submission, please let us know which of your runs  are manual. For each topic, include up to 1,000 retrieved documents.                
>
> The submission format for the task will follow the standard TREC format: 
>
> ```
> qid Q0 doc rank score tag
> qid = topic number
> Q0 = not relevant
> doc = document ID from field id
> rank = the rank that our search enginge has computed for this query
> score = the score based on the aforementioned rank
> tag = not relevant
> ```

## Lab Report

The structure of the lab report should be as follows:

1.  Introduction 
2.  Related Work 
3.  Argument Retrieval Model(s) (i.e., a description of your contribution)
4.  Evaluation 
5.  Discussion and Conclusion 



### Questions

- [x] What is a standard TREC format?
- [x] What is a run?
- [ ] Aren't we supposed to use Docker at this stage?
- [ ] Does Lucene have classes for information retrieval methods or is this something we need to implement manually?
- [ ] What retrieval method is used by args.me? Bm25
- [ ] What should our abstract be? 

## Questions session

- args.me uses bm25 as retrieval model
- Tira needs the complete index, as part of the docker image to work
- A run is a search engine output.
- Baseline ist der score der auf standard einstellungen läuft
- Lucene already provides a stop list.
- We need to analyze the json corpus in order to figure out more stop words
- Tira can automate the evaluation process.
- the abstract should be a single paragraph that summarizes our entire lab report.





