package com.mmall.test;


import org.junit.Test;

import java.math.BigDecimal;

public class BigDecimalTest {
    @Test
    public void test(){

        BigDecimal bigDecimal = new BigDecimal("0.05");
        BigDecimal bigDecimal1 = new BigDecimal("0.01");
        System.out.println(bigDecimal.add(bigDecimal1));
    }
    @Test
    public void test1(){

        BigDecimal bigDecimal = new BigDecimal(0.05);
        BigDecimal bigDecimal1 = new BigDecimal(0.01);
        System.out.println(bigDecimal.add(bigDecimal1));
    }
}
