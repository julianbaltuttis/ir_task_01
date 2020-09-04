package org;

import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;


import org.apache.commons.cli.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class Main {

    public static void main(String[] args) {
        log.info("--> Main().");
        System.out.println("Hello World!");

        Options options = new Options();
        Option input = new Option("i", "input", true, "input directory");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output directory");
        output.setRequired(true);
        options.addOption(output);


        Option tag = new Option("t", "tag", true, "Method Description");
        input.setRequired(true);
        options.addOption(tag);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try{
            cmd = parser.parse(options,args);
            String inputDirPath = cmd.getOptionValue("input");
            String outputDirPath = cmd.getOptionValue("output");
            String methodTag = cmd.getOptionValue("tag");

            run(inputDirPath, outputDirPath, methodTag);

        }catch (ParseException e) {
            log.error(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        log.info("<-- Main().");
    }

    public static void run(String inputPath, String outputPath, String tag) {
        log.info("--> run().");
        String fileInputPath = inputPath+"/topics.xml";
        System.out.println(fileInputPath);
        Topics topics = new Topics(fileInputPath);
        List<Topic> topicList = topics.getTopics();


        try {
            QueryProcessor query = new QueryProcessor(tag);
            String fileOutputPath = outputPath+"/run.txt";
            System.out.println(fileInputPath);
            File outputFile = new File(fileOutputPath);
            FileWriter writer;
            if(outputFile.createNewFile()) {
                log.info("File is created!");
                writer = new FileWriter(outputFile);
            }else {
                writer = new FileWriter(outputFile,false);

            }


            for(Topic topic : topicList) {
                List<Result> results = new ArrayList<>();
                int topicNumber = topic.getNumber();
                String topicTitle = topic.getTitle();

                TopDocs hits = query.searchIndex(topicTitle);
                results = query.getResultsFromTopDocs(hits, topicNumber);
                for(Result result : results){
                    System.out.println(result.toString());
                    writer.write(result.toString());
                }

            }
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("<-- run().");
    }

}


