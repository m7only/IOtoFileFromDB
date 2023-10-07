package org.m7.service;

import java.nio.file.Path;
import java.util.Optional;

public interface FileService {
    Path save(String data, Path path);
    Optional<String> read(Path path);

}
