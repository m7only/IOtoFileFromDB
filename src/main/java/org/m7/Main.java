package org.m7;

import org.m7.service.ApplicationService;
import org.m7.service.impl.ApplicationServiceImpl;

public class Main {
    public static void main(String[] args) {
        ApplicationService applicationService = new ApplicationServiceImpl();
        try {
            applicationService.parse(args);
            applicationService.run();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}