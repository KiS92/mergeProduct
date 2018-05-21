package com.kochubei.service.product;

import com.kochubei.bean.Price;
import com.kochubei.service.product.impl.PriceServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class JunitTests {
    @Test
    public void test1() {
        PriceServiceImpl service = new PriceServiceImpl();
        List cur = new LinkedList<>();
        cur.add(new Price("1", 1, 1, new Date(92, 05, 24), new Date(99, 05, 24), 12));
        List newp = new LinkedList<>();
        newp.add(new Price("1", 1, 1, new Date(95, 05, 24), new Date(96, 05, 24), 14));
        List resp = service.mergePrices(cur, newp);
        Assert.assertEquals(3, resp.size());
    }
}