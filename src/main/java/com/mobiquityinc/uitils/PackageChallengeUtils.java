package com.mobiquityinc.uitils;

import com.mobiquityinc.packer.Item;

import java.util.ArrayList;

/**
 * Class that contains some utilities related to the Package and Item
 */

public class PackageChallengeUtils {

    public static String printPackageItems(ArrayList<Item> items){
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Item i : items) {
            if(isFirst){
                stringBuilder.append(i.getIndex());
                isFirst = false;
            }else{
                stringBuilder.append("," + i.getIndex());
            }
        }
        return stringBuilder.toString();
    }

    public static double getItemWeight(ArrayList<Item> items){
        double total = 0;
        for(Item i : items){
            total += i.getWeight();
        }
        return total;
    }

    public static double getItemCost(ArrayList<Item> items){
        double total = 0;
        for(Item i : items){
            total += i.getCost();
        }
        return total;
    }
}
