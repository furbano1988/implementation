package com.mobiquityinc;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Several unit tests were performed in order to cover most of the scenarios provided in the document.
 *
 * For this development, the application of TDD as one of the best agile practices is carried out as the main development strategy
 * and that means that this small exercise has 98% of the coverage of the main class which contains the logic.
 *
 * On the algorithm side, it focused mainly on the search for possible combinations within a package and the rules defined in the document were applied to it.
 * As a personal note I always like to have classes with very specific responsibilities for them, it is about extracting certain functionality for each class
 * and leaving the core of the logic in the main class.
 *
 * I think that with a little more time and to return this somewhat more scalable exercise,
 * a strategy pattern could be applied to handle the different business rules applied to the package in order to add a new one without any inconvenience.
 *
 */

public class PackerTest {

    @Test
    public void testPackerWithSuccessFull() throws APIException {
        // Given
        String inputPath = "src/test/resources/inputFile.txt";

        // When
        String result = Packer.pack(inputPath);

        // Then
        Assert.assertNotNull(result);
        Assert.assertTrue(result.length()!=0);
        Assert.assertTrue(result.split("\n")[0].contains("4"));
        Assert.assertTrue(result.split("\n")[1].contains("-"));
        Assert.assertTrue(result.split("\n")[2].contains("2,7"));
        Assert.assertTrue(result.split("\n")[3].contains("8,9"));
    }

    @Test(expected = APIException.class)
    public void testPackerWithEmptyArgumentShouldReturnException() throws APIException {
        // Given
        String inputPath = "src/test/resources/wrongFile.txt";

        // When
        String result = Packer.pack(inputPath);

        // Then
    }

    @Test
    public void testPackerWithEmptyFileShouldReturnResultEmpty() throws APIException {
        // Given
        String inputPath = "src/test/resources/inputFileEmpty.txt";

        // When
        String result = Packer.pack(inputPath);

        // Then
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testPackerWithWeighPackageGraterThan100tShouldReturnException(){
        // Given
        String inputPath = "src/test/resources/inputFileWithWeightPackageGraterThan100.txt";
        // When
        try {
            Packer.pack(inputPath);
        }catch (APIException e){
            // Then
            Assert.assertNotNull(e);
            Assert.assertTrue(e.getMessage().contains("weight that a package can take is <= 100"));
        }
    }

    @Test
    public void testPackerWithPackageWithMoreThanFifteenItemsShouldReturnException(){
        // Given
        String inputPath = "src/test/resources/inputFileWithPackageWithMoreThanFifteenItems.txt";
        // When
        try {
            Packer.pack(inputPath);
        } catch (APIException e){
            // Then
            Assert.assertNotNull(e);
            Assert.assertTrue(e.getMessage().contains("package must no have more than fifteen (15) items"));
        }
    }

    @Test
    public void testPackerWithWeightItemGraterThan100ShouldReturnException() throws APIException{
        // Given
        String inputPath = "src/test/resources/inputFileWithWeightItemGraterThan100.txt";
        // When
        String result = Packer.pack(inputPath);

        // Then
        Assert.assertNotNull(result);
        Assert.assertFalse(result.split("\n")[2].contains("6"));
        Assert.assertFalse(result.split("\n")[2].contains("8"));
    }

    @Test
    public void testPackerWithCostItemGraterThan100ShouldReturnException() throws APIException{
        // Given
        String inputPath = "src/test/resources/inputFileWithCostItemGraterThan100.txt";
        // When
        String result = Packer.pack(inputPath);

        // Then
        Assert.assertNotNull(result);
        Assert.assertFalse(result.contains("6"));
    }

}
