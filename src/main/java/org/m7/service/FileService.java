package org.m7.service;

import java.nio.file.Path;

public interface FileService {
    Path save(String data, Path path);

    String read(Path path);

}
