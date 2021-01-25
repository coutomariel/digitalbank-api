package com.matera.bootcamp.digitalbank;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {


    Printer inglesPrinter;
    Printer portuguesPrinter;

    public AppStartupRunner(Printer inglesPrinter, Printer portuguesPrinter) {
        this.inglesPrinter = inglesPrinter;
        this.portuguesPrinter = portuguesPrinter;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        inglesPrinter.printer();
        portuguesPrinter.printer();
        System.out.println("Hello world!");

    }
}
