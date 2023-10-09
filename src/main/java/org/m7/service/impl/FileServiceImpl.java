package org.m7.service.impl;

import org.m7.service.FileService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileServiceImpl implements FileService {

    /**
     * Сохранение тектового файла.
     *
     * @param data сохраняемый текст
     * @param path путь к сохраняемому файлу
     * @return {@code Path} путь к сохраненному файлу
     */
    @Override
    public Path save(String data, Path path) {
        try {
            Files.write(path, data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Чтение текстового файла.
     *
     * @param path путь читаемого файла
     * @return {@code Optional<String>} текст из файла
     */
    @Override
    public String read(Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
