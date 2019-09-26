package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.uitils.PackageChallengeUtils;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class that contains all the logic related to the extraction of the items
 * that could be sent by each package
 */

public class Packer {

  private double limit;
  private ArrayList<Item> items;
  private ArrayList<ArrayList<Item>> combinations;

  public static String pack(String inputPath) throws APIException {
    return String.join("\n", readInputFile(inputPath));
  }

  private static List<String> readInputFile(String inputPath) throws APIException {
    List<String> packages = new ArrayList<>();
    File file = new File(inputPath);
    try {
      BufferedReader inputStream = new BufferedReader(new FileReader(file));
      String line;
      while ((line = inputStream.readLine()) != null) {
        if (line.length() == 0)
          continue;
        String[] packageContent = line.split(":");
        double weightLimit = Integer.parseInt(packageContent[0].trim());

        if (weightLimit > 100) {
          throw new APIException(String.format("weight that a package can take is <= 100 - Line %s - Weight: %s",
                  line, weightLimit));
        }

        ArrayList<Item> items = fillPackageItems(packageContent[1]);

        if (items.size() > 15) {
          throw new APIException(String.format("package must no have more than fifteen (15) items - Line %s - Weight: %s",
                  line, weightLimit));
        }

        Packer linePackage = new Packer(weightLimit, items);
        packages.add(linePackage.getPackage());
      }
    } catch (IOException e) {
      throw new APIException(e.getMessage());
    }

    return packages;
  }

  private static ArrayList<Item> fillPackageItems(String s) {
    String[] stringItems = s.trim().split(" ");
    ArrayList<Item> items = new ArrayList<>();
    for(String stringItem : stringItems){
      String[] itemDetails = stringItem.split(",");
      int id = Integer.parseInt(itemDetails[0].substring(1));
      double weight = Double.parseDouble(itemDetails[1]);
      double price = Double.parseDouble(itemDetails[2].substring(1, itemDetails[2].length()-1));
      Item item = new Item(id, weight, price);
      items.add(item);
    }
    return items;
  }

  public Packer(double limit, ArrayList<Item> items){
    this.limit = limit;
    this.items = items;
    combinations = new ArrayList<>();
  }

  private ArrayList<ArrayList<Item>> buildCombinations(){
    for(int itemIndex = 0; itemIndex < items.size(); itemIndex++){
      Item currentItem = items.get(itemIndex);
      int combinationSize = combinations.size();
      for(int combinationIndex = 0; combinationIndex < combinationSize; combinationIndex++){
        ArrayList<Item> combination = combinations.get(combinationIndex);
        ArrayList<Item> newCombination = new ArrayList<>(combination);
        newCombination.add(currentItem);
        combinations.add(newCombination);
      }
      ArrayList<Item> current = new ArrayList<>();
      current.add(currentItem);
      combinations.add(current);
    }

    return combinations;
  }

  private ArrayList<Item> getItemsFromBestCombinationPackage(){
    ArrayList<Item> itemCombination = new ArrayList<>();
    double bestCost = 0;
    double bestWeight = 100;
    for(ArrayList<Item> combination : combinations){
      double combinationWeight = PackageChallengeUtils.getItemWeight(combination);
      if(combinationWeight <= limit){
        double combinationPrice = PackageChallengeUtils.getItemCost(combination);
        if(combinationPrice > bestCost){
          bestCost = combinationPrice;
          itemCombination = combination;
          bestWeight = combinationWeight;
        }else if(combinationPrice == bestCost){
          //You would prefer to send a package which weighs less in case there is more than one package with the
          //same price
          if(combinationWeight < bestWeight){
            bestCost = combinationPrice;
            itemCombination = combination;
            bestWeight = combinationWeight;
          }
        }
      }
    }
    return itemCombination;
  }

  private String getPackage(){

    // Max weight and cost of an item is ≤ 100
    items.removeIf(i -> i.getWeight() > limit);
    items.removeIf(i -> i.getCost() > 100);

    combinations = buildCombinations();
    if(combinations.size() == 0){
      return "-";
    }else{
      ArrayList<Item> bestCombination = getItemsFromBestCombinationPackage();
      return PackageChallengeUtils.printPackageItems(bestCombination);
    }
  }
}
