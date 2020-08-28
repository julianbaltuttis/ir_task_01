package org;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Log4j
@Setter
@Getter
public class Argument {
    private String id;
    private String conclusion;
    private List<Premise> premises;
    private ObjectNode context;

    public static final String DOC_ARGUMENTS ="arguments";
    public static final String DOC_ID ="id";
    public static final String DOC_CONCLUSION="conclusion";
    public static final String DOC_PREMISES="premises";
    public static final String DOC_TEXT="text";
    public static final String DOC_STANCE="stance";
    public static final String DOC_CONTEXT="context";
    public static final String DOC_SOURCE_ID="sourceId";
    public static final String DOC_ANNOTATIONS="annotations";
    public static final String DOC_ASPECTS="aspects";

    @Override
    public String toString(){
        StringBuilder argument = new StringBuilder();
        argument.append("ID: "+id+"\nConclusion: "+ conclusion+"\n");
        for (Premise premise: premises) {
            argument.append("Text: "+ premise.getText()+"\nStance: "+premise.getStance()+"\n");
        }
        argument.append("sourceID: "+context.get("sourceId").asText()+"\n");
        return argument.toString();

    }
    public Document getArgumentAsDocument() {
        Document doc = new Document();
        doc.add(new StringField(DOC_ID, id, Field.Store.YES));
        doc.add(new TextField(DOC_CONCLUSION, conclusion, Field.Store.NO));

        for(Premise premise : premises) {

            doc.add(new TextField(DOC_TEXT, premise.getText(), Field.Store.NO));
            doc.add(new StringField(DOC_STANCE, premise.getStance(), Field.Store.NO));
        }

        doc.add(new StringField(DOC_SOURCE_ID, context.get(DOC_SOURCE_ID).asText(), Field.Store.YES));
        return doc;
    }

}
