package com.matera.bootcamp.digitalbank;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("en")
public class InglesPrinter implements Printer{
    @Override
    public void printer() {
        System.out.println("en-US");
    }
}