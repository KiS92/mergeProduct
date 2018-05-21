package com.kochubei.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class Price implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Version
    @Column
    private Long version;

    @Column(name = "product_code")
    private String productCode;

    @Column
    private int number;

    @Column
    private int depart;

    @Temporal(TemporalType.DATE)
    @Column
    private Date begin;

    @Temporal(TemporalType.DATE)
    @Column
    private Date end;

    @Column
    private long value;

    public Price() {
    }

    public Price(String productCode, int number, int depart, Date begin, Date end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Price(Price price) {
        this.productCode = price.getProductCode();
        this.number = price.getNumber();
        this.depart = price.getDepart();
        this.begin = price.getBegin();
        this.end = price.getEnd();
        this.value = price.getValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", version=" + version +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (getNumber() != price.getNumber()) return false;
        if (getDepart() != price.getDepart()) return false;
        if (getValue() != price.getValue()) return false;
        if (getProductCode() != null ? !getProductCode().equals(price.getProductCode()) : price.getProductCode() != null)
            return false;
        if (getBegin() != null ? !getBegin().equals(price.getBegin()) : price.getBegin() != null) return false;
        return !(getEnd() != null ? !getEnd().equals(price.getEnd()) : price.getEnd() != null);

    }

    @Override
    public int hashCode() {
        int result = getProductCode() != null ? getProductCode().hashCode() : 0;
        result = 31 * result + getNumber();
        result = 31 * result + getDepart();
        result = 31 * result + (getBegin() != null ? getBegin().hashCode() : 0);
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        result = 31 * result + (int) (getValue() ^ (getValue() >>> 32));
        return result;
    }
}
