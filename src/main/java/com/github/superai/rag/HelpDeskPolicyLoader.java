package com.github.superai.rag;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//@Component
@Slf4j
@RequiredArgsConstructor
public class HelpDeskPolicyLoader {

    private final SimpleVectorStore simpleVectorStore;

    @Value("classpath:/docs/terms-of-service.txt")
    private Resource policyResource;

    @Value("superair-vectorstore.json")
    private String vectorStoreName;

    @PostConstruct
    public void load() throws IOException {
        var vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("Vector Store File Exists,");
            simpleVectorStore.load(vectorStoreFile);
        } else {

            log.info("Vector Store File Does Not Exist, loading documents");
            TextReader textReader = new TextReader(policyResource);
            textReader.getCustomMetadata().put("filename", "terms-of-service.txt");
            List<Document> documents = textReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        log.info("Vector Store File - {}" , absolutePath);
        return new File(absolutePath);
    }
}
