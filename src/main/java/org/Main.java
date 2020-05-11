package org;

import lombok.extern.log4j.Log4j;

@Log4j
public class Main {

    public static void main(String[] args) {
        log.info("--> Main().");
        System.out.println("Hello World!");
        log.info("<-- Main().");
    }

}
