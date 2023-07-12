package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.swing.plaf.IconUIResource;
import javax.swing.text.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search extends Feature_model {
    public Search() {
        super(Mandatory.Features.Any.SEARCH);
    }

    @Override
    public ExecutionReport execute(Project project, Object... params) {
        StringBuilder query = new StringBuilder();
        Arrays.stream(params).toList().forEach(param -> {
            query.append((String) param);
        });
        Node n = project.getRootNode();
        Analyzer analyzer = new StandardAnalyzer();
        try {
            Project_model pr = (Project_model) project;
            Path indexPath = pr.indexPath;
            Directory directory = FSDirectory.open(indexPath);
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            List<org.apache.lucene.document.Document> list = new ArrayList<>();
            walk(n, directory, analyzer, list);

            List<String> files = new ArrayList<>();
            List<org.apache.lucene.document.Document> l = searchIndex("content", query.toString(), analyzer, directory);
            for (var el : l)
            {
                files.add(el.toString());
            }

            if (files.isEmpty())
            {
                return new ExecutionReport_model(false);
            }
            else
            {
                System.out.println(files);
                return new ExecutionReport_model(true);
            }
        }
        catch (Exception e)
        {
            return new ExecutionReport_model(false);
        }
    }

    public List<org.apache.lucene.document.Document> searchIndex(String inField, String query, Analyzer analyzer, Directory directory)
    {
        try {
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            // Parse a simple query that searches for "text":
            QueryParser parser = new QueryParser(inField, analyzer);
            Query q = parser.parse(query.toString());
            TopDocs hits = isearcher.search(q, 10);
            List<org.apache.lucene.document.Document> doc = new ArrayList<>();
            for (ScoreDoc scoreDoc : hits.scoreDocs)
            {
                doc.add(isearcher.doc(scoreDoc.doc));
            }
            if (doc.isEmpty())
            {
                return null;
            }
            return doc;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void walk(Node n, Directory directory, Analyzer analyzer, List<org.apache.lucene.document.Document> list)
    {
        if (n.isFile())
        {
            try {
                IndexWriterConfig config = new IndexWriterConfig(analyzer);
                IndexWriter iwriter = new IndexWriter(directory, config);
                org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                String txt = get_file_content(new File(n.getPath().toString()));
                doc.add(new TextField("content", txt, Field.Store.YES));
                iwriter.addDocument(doc);
                iwriter.close();
                list.add(doc);
            }
            catch (Exception e)
            {
                return;
            }
        }
        else
        {
            for (Node child : n.getChildren())
            {
                walk(child, directory, analyzer, list);
            }
        }
    }

    private String get_file_content(File file)
    {
        BufferedReader reader;
        StringBuilder Coubeh = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            String line = reader.readLine();

            while (line != null) {
                // read next line
                Coubeh.append(line);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            return "";
        }
        return Coubeh.toString();
    }
}
