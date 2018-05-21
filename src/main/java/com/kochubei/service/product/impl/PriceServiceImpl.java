package com.kochubei.service.product.impl;

import com.kochubei.bean.Price;
import com.kochubei.service.product.PriceService;

import java.util.*;

import static java.util.Collections.EMPTY_LIST;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class PriceServiceImpl implements PriceService {

    @Override
    public List<Price> mergePrices(final List<Price> currentPrices, final List<Price> newPrices) {
        if (isEmpty(currentPrices) && isEmpty(newPrices)) {
            return EMPTY_LIST;
        }
        if (isEmpty(currentPrices) && isNotEmpty(newPrices)) {
            return newPrices;
        }
        if (isNotEmpty(currentPrices) && isEmpty(newPrices)) {
            return currentPrices;
        }

        final Map<String, List<Price>> productPrices = createProductCodePrices(currentPrices);

        for (final Price newPrice : newPrices) {
            final String key = newPrice.getProductCode();
            if (productPrices.containsKey(key)) {
                final List<Price> localPrices = productPrices.get(key);
                Set<Price> priceForAdd = new HashSet<>();
                List<Price> modifiedPrices = new ArrayList<>();
                for (final Price localPrice : localPrices) {
                    if (localPrice.getDepart() == newPrice.getDepart() && localPrice.getNumber() == newPrice.getNumber()) {
                        if (newPrice.getEnd().before(localPrice.getBegin()) || newPrice.getBegin().after(localPrice.getEnd())) {
                            //если новая цена заканчивается до локальной или начинается после текущей - добавляем новую цену
                            priceForAdd.add(newPrice);
                        } else {
                            if (newPrice.getValue() == localPrice.getValue()) {
                                //если значения цен одинаковы, период действия имеющейся цены увеличивается согласно периоду новой цены;
                                if (newPrice.getBegin().before(localPrice.getBegin())) {
                                    localPrice.setBegin(newPrice.getBegin());
                                }
                                if (newPrice.getEnd().after(localPrice.getEnd())) {
                                    localPrice.setEnd(newPrice.getEnd());
                                }
                            } else {
                                //если значения цен отличаются, добавляется новая цена, а период действия старой цены уменьшается согласно периоду новой цены.
                                priceForAdd.add(newPrice);
                                if (newPrice.getBegin().after(localPrice.getBegin()) && newPrice.getEnd().before(localPrice.getEnd())) {
                                    Price price = new Price(localPrice);
                                    price.setBegin(newPrice.getEnd());
                                    priceForAdd.add(price);
                                    localPrice.setEnd(newPrice.getBegin());
                                } else if (newPrice.getBegin().before(localPrice.getBegin()) && newPrice.getEnd().before(localPrice.getEnd()) && newPrice.getEnd().after(localPrice.getBegin())) {
                                    localPrice.setBegin(newPrice.getEnd());
                                } else if (newPrice.getBegin().after(localPrice.getBegin()) && newPrice.getBegin().before(localPrice.getEnd()) && newPrice.getEnd().after(localPrice.getEnd())) {
                                    localPrice.setEnd(newPrice.getBegin());
                                }
                            }
                        }
                    } else {
                        priceForAdd.add(newPrice);
                    }
                    modifiedPrices.add(localPrice);
                }
                productPrices.get(key).clear();
                productPrices.get(key).addAll(modifiedPrices);
                if (!priceForAdd.isEmpty()) {
                    productPrices.get(key).addAll(priceForAdd);
                    priceForAdd.clear();
                }
            } else {
                final List<Price> value = new ArrayList<>();
                value.add(newPrice);
                productPrices.put(key, value);
            }
        }

        List<Price> result = new ArrayList<>();
        for (Map.Entry<String, List<Price>> entry : productPrices.entrySet()) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    private Map<String, List<Price>> createProductCodePrices(List<Price> currentPrices) {
        final Map<String, List<Price>> productPrices = new HashMap<>();
        for (final Price price : currentPrices) {
            final String key = price.getProductCode();
            if (productPrices.containsKey(key)) {
                productPrices.get(key).add(price);
            } else {
                List<Price> value = new ArrayList<>();
                value.add(price);
                productPrices.put(key, value);
            }
        }
        return productPrices;
    }
}
