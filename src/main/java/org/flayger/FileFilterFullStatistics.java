package org.flayger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class FileFilterFullStatistics implements FileFilterStatistics{

    private static Integer intsCount = 0;
    private static Integer floatsCount = 0;
    private static Integer stringsCount = 0;


    private static BigInteger intsMaxValue;
    private static BigInteger intsMinValue;
    private static BigInteger intsAverage = new BigInteger("0");
    private static BigInteger intsSum = new BigInteger("0");

    private static BigDecimal floatsMaxValue;
    private static BigDecimal floatsMinValue;
    private static BigDecimal floatsAverage = new BigDecimal("0.0");
    private static BigDecimal floatsSum = new BigDecimal("0");

    private static BigInteger stringsMaxLength;
    private static BigInteger stringsMinLength;
    @Override
    public void updateStatistics(BigDecimal floatData) {
        floatsCount++;

        if (floatsMaxValue == null) {
            floatsMaxValue = floatData;
        }
        if (floatsMaxValue.compareTo(floatData) < 0) {
            floatsMaxValue = floatData;
        }
        if (floatsMinValue == null) {
            floatsMinValue = floatData;
        }
        if (floatsMinValue.compareTo(floatData) > 0) {
            floatsMinValue = floatData;
        }

        floatsSum = floatsSum.add(floatData);
        floatsAverage = floatsSum.divide(BigDecimal.valueOf(intsCount), RoundingMode.HALF_EVEN);
    }

    @Override
    public void updateStatistics(BigInteger intData) {
        intsCount++;

        if (intsMaxValue == null) {
            intsMaxValue = intData;
        }
        if (intsMaxValue.compareTo(intData) < 0) {
            intsMaxValue = intData;
        }
        if (intsMinValue == null) {
            intsMinValue = intData;
        }
        if (intsMinValue.compareTo(intData) > 0) {
            intsMinValue = intData;
        }

        intsSum = intsSum.add(intData);
        intsAverage = intsSum.divide(BigInteger.valueOf(intsCount));
    }

    @Override
    public void updateStatistics(String stringData) {
        stringsCount++;

        BigInteger lineSize = BigInteger.valueOf(stringData.length());
        if (stringsMaxLength == null) {
            stringsMaxLength = lineSize;
        }
        if (stringsMaxLength.compareTo(lineSize) < 0) {
            stringsMaxLength = lineSize;
        }
        if (stringsMinLength == null) {
            stringsMinLength = lineSize;
        }
        if (stringsMinLength.compareTo(lineSize) > 0) {
            stringsMinLength = lineSize;
        }
    }

    @Override
    public void showStatistics() {
        System.out.printf("integers stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", intsCount, intsMinValue, intsMaxValue, intsAverage, intsSum);

        System.out.printf("floats stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", floatsCount, floatsMinValue, floatsMaxValue, floatsAverage, floatsSum);

        System.out.printf("strings stats - count - %S, minLength - %S, maxLength - %S \n", stringsCount, stringsMinLength, stringsMaxLength);
    }
}
