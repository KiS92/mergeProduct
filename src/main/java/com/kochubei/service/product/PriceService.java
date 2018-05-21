package com.kochubei.service.product;

import com.kochubei.bean.Price;

import java.util.List;

public interface PriceService {
    List mergePrices(final List<Price> currentPrices, final List<Price> newPrices);
}
