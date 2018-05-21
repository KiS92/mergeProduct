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
        Mock(Price) {
            getProductCode() >> opts.productCode
            getNumber() >> opts.number
            getDepart() >> opts.depart
            getBegin() >> opts.begin
            getEnd() >> opts.end
            getValue() >> opts.value
        }
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
    def "[mergePrices] return full prices when current prices value not equals new prices value"() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 100)
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
    def "[mergePrices] return update prices when current prices value equals new prices value and currentPrice-end after newPrice-begin "() {
        given:
        def priceProductOne = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,27), value: 100)
        def priceProductTwo = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 100)
        def currentPrices = [priceProductOne]
        def newPrices = [priceProductTwo]
        def expected = createPrice(productCode: "1", depart: 1, number: 1, begin: new Date(92,05,23), end: new Date(92,05,24), value: 100)

        when:
        def result = productService.mergePrices(currentPrices, newPrices)

        then:
        result.size() == 1
        result.containsAll([expected])
    }
}
