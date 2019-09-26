package com.mobiquityinc.main;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;

public class PackageChallengeMain {

    public static void main(String[] args) throws APIException {

        verifyInputFileArgument(args);

        System.out.println(Packer.pack(args[0]));

    }

    private static void verifyInputFileArgument(String[] args) {
        if (args.length != 1){
            System.err.println("no arguments, please provide input path file as an argument");
            System.exit(1);
        }
    }
}
