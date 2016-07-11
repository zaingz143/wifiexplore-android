package com.example.dell.wifiexplorer;

import java.text.NumberFormat;

/**
 * Created by rwaldura on 12/22/14.
 */
public enum ImpulseItem {

    CHANEL_5(100, "Chanel #5 perfume", 4577 /* US cents */),
    SACK_POTATOES(101, "Sack of potatoes", 345),
    MACBOOK_HELIUM(102, "MacBook Helium", 49999),
    NEXUS_23(103, "Nexus 23", 22222),
    PING_PONG_BALLS(104, "Pack of 6 ping-pong balls", 1323),
    NOOGLER_CAP(105, "Noogler cap", 1200),
    NULL_OBJECT(106, "Null object", 0);

    public final Integer id;
    public final String description;
    public final Integer price; // in US cents

    ImpulseItem(int id, String description, int price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public String displayPrice() {
        return NumberFormat.getCurrencyInstance().format(price / 100.0);
    }

    @Override
    public String toString() {
        return id + " " + description + " $" + (price / 100.0);
    }

    public static ImpulseItem valueOf(int i) {
        if (i >= 0 && i < values().length)
            return values()[i];
        else
            return null;
    }



}
