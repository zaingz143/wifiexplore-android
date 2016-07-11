package com.example.dell.wifiexplorer;

import java.util.Random;

/**
 * Created by rwaldura on 12/22/14.
 */
public class ImpulseStore {
    public static final String TAG = "impulsebuy";

    public static final String DESCRIPTION_LINE_ITEM_SHIPPING = "shipping";
    public static final String DESCRIPTION_LINE_ITEM_TAX = "tax";

    public static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final int FLAT_SHIPPING_FEE = 1000; // $10 in pennies
    private static final float TAX = 10 / 100; // 10%

    // Intent extra keys
    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    public static final String EXTRA_TX_STATUS = "EXTRA_TX_STATUS";

    /**
     *
     * @param item
     * @return
     */
    public static int computeTotalPrice(ImpulseItem item) {
        if (item == null) throw new IllegalArgumentException("Invalid item: " + item);
        return item.price + getShipping(item) + getTax(item);
    }

    /**
     * shipping rules impl
     * @param item
     * @return
     */
    public static int getShipping(ImpulseItem item) {
        return FLAT_SHIPPING_FEE;
    }

    /**
     * get taxation rules impl
     * @param item
     * @return
     */
    public static int getTax(ImpulseItem item) {
        return (int) (item.price * TAX);
    }

    /**
     *
     * @return
     */
    public static ImpulseItem pickRandomItem() {
        int i = RANDOM.nextInt(ImpulseItem.values().length);
        return ImpulseItem.valueOf(i);
    }

    public static String toUSD(int price) {
        return String.valueOf(price / 100.0);
    }
}
