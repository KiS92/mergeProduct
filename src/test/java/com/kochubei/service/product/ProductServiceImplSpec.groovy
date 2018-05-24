package com.kochubei.service.product

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.kochubei.bean.Price
import com.kochubei.service.product.impl.PriceServiceImpl
import org.junit.Test
import spock.lang.Specification

class ProductServiceImplSpec extends Specification {
    @Subject
    PriceServiceImpl productService

    Price createPrice(opts = [:]) {
        def price = new Price()
        if(opts.productCode != null)
        price.setProductCode(opts.productCode)
        if(opts.number != null)
        price.setNumber(opts.number)
        if(opts.depart != null)
        price.setDepart(opts.depart)
        if(opts.begin != null)
        price.setBegin(opts.begin)
        if(opts.end != null)
        price.setEnd(opts.end)
        if(opts.value != null)
        price.setValue(opts.value)
        return price
    }

    @Test
    def "[mergePrices] return empty when current and new prices is null or empty"() {
        given:
        def currentPrices = null
        def newPrices = null

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result == Collections.EMPTY_LIST
    }

    @Test
    def "[mergePrices] return current prices when new prices is null or empty"() {
        given:
        def price = createPrice(productCode: "1")
        def currentPrices = [price]
        def newPrices = null

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result == currentPrices
    }

    @Test
    def "[mergePrices] return new prices when current prices is null or empty"() {
        given:
        def price = createPrice(productCode: "1")
        def currentPrices = null
        def newPrices = [price]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result == newPrices
    }

    @Test
    def "[mergePrices] return full prices when current prices product code not equals new prices product code"() {
        given:
        def priceProductOne = createPrice(productCode: "1")
        def priceProductTwo = createPrice(productCode: "2")
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return full prices when current prices depart not equals new prices depart"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1)
        def priceProductTwo = createPrice(productCode: "1", depart: 2)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return full prices when current prices number not equals new prices number"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 2)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return full prices when current prices begin-end before new prices begin-end"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,21), end: new Date(92,05,22))
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24))
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return full prices when current prices begin-end after new prices begin-end"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24))
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,21), end: new Date(92,05,22))
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return updated price when current prices value equals new prices value and newPrice-end after currentPrice-end "() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,26), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,27), value: 100)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]


        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 1
        def expected = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,27), value: 100)
        result.contains(expected)
    }


    @Test
    def "[mergePrices] return updated price when current prices value equals new prices value and newPrice-begin before currentPrice-begin "() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,26), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,25), value: 100)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 1
        def expected = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,26), value: 100)
        result.contains(expected)
    }

    @Test
    def "[mergePrices] return full prices when current prices value not equals new prices value and currentPrice dates before newPrice dates"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,25), end: new Date(92,05,27), value: 101)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return full prices when current prices value not equals new prices value and currentPrice dates after newPrice dates"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,25), end: new Date(92,05,27), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 101)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        result.containsAll([priceProductTwo, priceProductOne])
    }

    @Test
    def "[mergePrices] return added prices when current prices value not equals new prices value and newPrice dates into currentPrice dates"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,27), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 101)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 3
        def expectedOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,23), value: 100)
        def expectedTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 101)
        def expectedThree = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,24), end: new Date(92,05,27), value: 100)
        result.contains(expectedOne)
        result.contains(expectedTwo)
        result.contains(expectedThree)
    }

    @Test
    def "[mergePrices] return added prices when current prices value not equals new prices value and newPrice dates outer currentPrice dates"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,27), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,19), end: new Date(92,05,29), value: 101)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 3
        def expectedOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,19), end: new Date(92,05,20), value: 101)
        def expectedTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,27), value: 100)
        def expectedThree = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,27), end: new Date(92,05,29), value: 101)
        result.contains(expectedOne)
        result.contains(expectedTwo)
        result.contains(expectedThree)
    }

    @Test
    def "[mergePrices] return full prices when current prices value not equals new prices value and newPrice dates part before currentPrice dates"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,25), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,24), value: 101)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        def expectedOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,24), value: 101)
        def expectedTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,24), end: new Date(92,05,25), value: 100)
        result.contains(expectedOne)
        result.contains(expectedTwo)
    }

    @Test
    def "[mergePrices] return full prices when current prices value not equals new prices value and newPrice dates part after currentPrice dates"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,25), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,24), end: new Date(92,05,27), value: 101)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 2
        def expectedOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,20), end: new Date(92,05,24), value: 100)
        def expectedTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,24), end: new Date(92,05,27), value: 101)
        result.contains(expectedOne)
        result.contains(expectedTwo)
    }
}
