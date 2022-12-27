package org.goravski.exchangeCurrencyBelBot.entity;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Sberbank implements EntityInterface{
    private final String nameBank = "СБЕРБАНК";
    private final String path = "D:\\IDEA_Projects\\exchangeCurrencyBelBot\\assests\\sberbank.png";

    public String getNameBank() {
        return nameBank;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return nameBank;
    }
}