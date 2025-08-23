package com.drawit.demo.service;

import com.drawit.demo.dto.WordEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class WordServiceImpl implements WordService {
    private final Map<String, List<WordEntry>> wordMap;

    public WordServiceImpl() throws IOException {
        try (InputStream is = getClass().
                getClassLoader().
                getResourceAsStream("words/words.json")) {
            if (is == null) throw new IllegalStateException("words.json not found");

            ObjectMapper mapper = new ObjectMapper();
            wordMap = mapper.readValue(
                    is, new TypeReference<Map<String, List<WordEntry>>>() {
                    }
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public WordEntry retrieveRandomWord() {
        Random random = new Random();
        int max = 15, min = 2;

        int length = random.nextInt(max - min + 1) + min;

        List<WordEntry> wordList = wordMap.get(String.valueOf(length));

        if (wordList == null || wordList.isEmpty()) {
            throw new IllegalStateException("word list is empty" + length);
        }

        int index = random.nextInt(wordList.size());

        return wordList.get(index);
    }
}
