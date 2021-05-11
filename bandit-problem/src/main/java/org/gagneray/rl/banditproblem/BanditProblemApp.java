package org.gagneray.rl.banditproblem;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.gagneray.rl.banditproblem.configurations.TestBedConfiguration;
import org.gagneray.rl.banditproblem.dto.TestBedConfigurationDTO;
import org.gagneray.rl.banditproblem.services.BanditProblemTestBed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BanditProblemApp {

    private final static Logger LOGGER = LoggerFactory.getLogger(BanditProblemApp.class);

    public static void main(String... args) {

        Options commandLineOptions = createCommandLineOptions();

        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "mvn <jar file> <option>";
        String header = "----\n List of options available : \n----";
        String footer = "----";

        CommandLine commandLine = null;
        try {
            commandLine = parseCommandLine(commandLineOptions, args);

        } catch (ParseException e) {
            LOGGER.error("Error while parsing command line :\n{}", e.getMessage());
            formatter.printHelp(cmdLineSyntax, header, commandLineOptions, footer);
        }

        if (Objects.requireNonNull(commandLine).getOptions().length == 0 || commandLine.hasOption("help")) {
            formatter.printHelp(cmdLineSyntax, header, commandLineOptions, footer);
            Runtime.getRuntime().exit(0);
        }

        if (commandLine.hasOption("testbed")) {
            String[] values = commandLine.getOptionValues("testbed");
            String path = values[0];
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                LOGGER.info("Parsing json configuration to TestBed Configuration from file {}", path);
                jsonNode = mapper.readTree(new File(path));
            } catch (IOException e) {
                LOGGER.error("Error while parsing json file configuration : {}", e.getMessage());
            }

            TestBedConfigurationDTO testBedConfigurationDTO = new TestBedConfigurationDTO(jsonNode);
            LOGGER.info("TestBedConfigurationDTO created : {}", testBedConfigurationDTO);

            TestBedConfiguration testBedConfiguration = new TestBedConfiguration(testBedConfigurationDTO.getPolicies(), testBedConfigurationDTO.getBanditProblemCount(), testBedConfigurationDTO.getK(), testBedConfigurationDTO.getTotalSteps());
            BanditProblemTestBed testBed = new BanditProblemTestBed(testBedConfiguration);
            testBed.process();

            if (commandLine.hasOption("plot")) {
                testBed.plotMeanAverages();
            }
        }

    }

    private static CommandLine parseCommandLine(Options options, String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    private static Options createCommandLineOptions() {
        Options options = new Options();

        Option help = Option.builder("h")
                .longOpt("help")
                .desc("show this message")
                .build();


        Option config = Option.builder("t")
                .longOpt("testbed")
                .desc("absolute path for input json file used as bandit problem test bed configuration")
                .required(false)
                .numberOfArgs(1)
                .argName("file path")
                .build();

        Option plot = Option.builder("p")
                .longOpt("plot")
                .desc("plot mean average results after bedtest")
                .required(false)
                .numberOfArgs(0)
                .build();

        options.addOption(config);
        options.addOption(help);
        options.addOption(plot);

        return options;
    }
}
