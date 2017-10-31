package org.panero.flink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
    private static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    public static void main(String[] args) throws Exception {
        logger.info("=== START ===");

        ExecutionEnvironment.getExecutionEnvironment()
                .generateSequence(1, 100000)
                .map(i -> i * i)
                .print();

        logger.info("=== FINISHED ===");
    }
}
