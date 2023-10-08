package org.m7.service.impl;

import org.m7.model.Operation;
import org.m7.service.ApplicationService;
import org.m7.service.FileService;
import org.m7.service.SearchService;
import org.m7.service.StatService;

import java.nio.file.Paths;

public class ApplicationServiceImpl implements ApplicationService {

    private Operation operation;
    private String inputFile;
    private String outputFile;

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
        FileService fileService = new FileServiceImpl();
        SearchService searchService = new SearchServiceImpl();
        fileService.save(
                searchService.search(fileService.read(Paths.get(inputFile))),
                Paths.get(outputFile)
        );
    }

    @Override
    public void stat() {
        FileService fileService = new FileServiceImpl();
        StatService statService = new StatServiceImpl();
        fileService.save(
                statService.stat(fileService.read(Paths.get(inputFile))),
                Paths.get(outputFile)
        );
    }
}
