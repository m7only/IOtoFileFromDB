package org.m7.service.impl;

import org.m7.model.Error;
import org.m7.model.Operation;
import org.m7.service.ApplicationService;
import org.m7.service.FileService;
import org.m7.service.SearchService;
import org.m7.service.StatService;

import java.nio.file.Paths;

public class ApplicationServiceImpl implements ApplicationService {

    private final static FileService fileService = new FileServiceImpl();
    private static String inputFile;
    private static String outputFile;
    private Operation operation;

    public static void error(String message) {
        fileService.save(new Error(message).toString(), Paths.get(outputFile));
    }

    @Override
    public void parse(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException();
        }
        operation = Operation.valueOf(args[0].toUpperCase());
        inputFile = args[1];
        outputFile = args[2];
    }

    @Override
    public void run() {
        switch (operation) {
            case STAT:
                stat();
                break;
            case SEARCH:
                search();
                break;
        }
    }

    @Override
    public void search() {
        SearchService searchService = new SearchServiceImpl();
        fileService.save(
                searchService.search(fileService.read(Paths.get(inputFile))),
                Paths.get(outputFile)
        );
    }

    @Override
    public void stat() {
        StatService statService = new StatServiceImpl();
        fileService.save(
                statService.stat(fileService.read(Paths.get(inputFile))),
                Paths.get(outputFile)
        );
    }
}
