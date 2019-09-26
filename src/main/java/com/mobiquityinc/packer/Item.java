package com.mobiquityinc.packer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents an Item that a Package might have
 * i.e: (1,15.3,€34)
 */

@Data
@AllArgsConstructor
public class Item {
    private int index;
    private double weight, cost;
}
