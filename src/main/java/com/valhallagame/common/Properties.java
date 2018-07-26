package com.valhallagame.common;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Properties {
    public static void load(String args[], Logger logger){
        if (args.length > 0) {
            if(logger.isInfoEnabled()) {
                logger.info("Args passed in: {}",  Arrays.asList(args).toString());
            }
            // override system properties with local properties

            for (String arg : args) {
                String[] split = arg.split("=");

                if (split.length == 2) {
                    System.getProperties().setProperty(split[0], split[1]);
                } else {
                    try (InputStream inputStream = new FileInputStream(args[0])) {
                        System.getProperties().load(inputStream);
                    } catch (IOException e) {
                        logger.error("Failed to read input.", e);
                    }
                }
            }

        } else {
            logger.info("No args passed to main");
        }
    }
}
