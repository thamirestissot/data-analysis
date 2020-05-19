package com.thamirestissot.factory;

import com.thamirestissot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.thamirestissot.enumerator.DataTypeEnum.*;

public class ParserFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserFactory.class);

    public static Object processLine(String line) {
        String separator = String.valueOf(line.charAt(3));
        String[] words = line.split(separator);
        int codeLine = Integer.parseInt(words[0]);
        if (SALESMAN.getCode() == codeLine) {
            return new Salesman(words[1], words[2], Double.parseDouble(words[3]));
        } else if (CUSTOMER.getCode() == codeLine) {
            return new Customer(words[1], words[2], words[3]);
        } else if (SALE.getCode() == codeLine) {
            words[2] = words[2].replace("[", "");
            words[2] = words[2].replace("]", "");
            String[] items = words[2].split(",");
            List<SaleItem> saleItem = new ArrayList<SaleItem>();
            for (String item : items) {
                String[] attributeItem = item.split("-");
                Item item1 = new Item(Integer.parseInt(attributeItem[0]), Double.parseDouble(attributeItem[2]));
                saleItem.add(new SaleItem(item1, Integer.parseInt(attributeItem[1])));
            }
            return new Sale(Integer.parseInt(words[1]), saleItem, words[3]);
        }
        throw new RuntimeException("Info code not registered.");
    }
}
