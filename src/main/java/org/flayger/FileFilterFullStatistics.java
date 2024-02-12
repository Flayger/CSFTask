package org.flayger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class FileFilterFullStatistics implements FileFilterStatistics {

    private Integer intsCount = 0;
    private Integer floatsCount = 0;
    private Integer stringsCount = 0;

    private BigInteger intsMaxValue;
    private BigInteger intsMinValue;
    private BigInteger intsAverage = BigInteger.ZERO;
    private BigInteger intsSum = BigInteger.ZERO;

    private BigDecimal floatsMaxValue;
    private BigDecimal floatsMinValue;
    private BigDecimal floatsAverage = BigDecimal.ZERO;
    private BigDecimal floatsSum = BigDecimal.ZERO;

    private BigInteger stringsMaxLength;
    private BigInteger stringsMinLength;

    @Override
    public void updateStatistics(BigDecimal floatData) {
        floatsCount++;
        checkMaxMin(floatData);

        floatsSum = floatsSum.add(floatData);
        floatsAverage = floatsSum.divide(BigDecimal.valueOf(intsCount), RoundingMode.HALF_EVEN);
    }

    @Override
    public void updateStatistics(BigInteger intData) {
        intsCount++;
        checkMaxMin(intData);

        intsSum = intsSum.add(intData);
        intsAverage = intsSum.divide(BigInteger.valueOf(intsCount));
    }

    @Override
    public void updateStatistics(String stringData) {
        stringsCount++;
        checkMaxMin(stringData);
    }

    public void checkMaxMin(Object data) {
        if (data instanceof BigDecimal castData) {
            if (floatsMaxValue == null || (castData).compareTo(floatsMaxValue) > 0) {
                floatsMaxValue = castData;
            }
            if (floatsMinValue == null || (castData).compareTo(floatsMinValue) < 0) {
                floatsMinValue = castData;
            }
        }
        if (data instanceof BigInteger castData) {
            if (intsMaxValue == null || (castData).compareTo(intsMaxValue) > 0) {
                intsMaxValue = castData;
            }
            if (intsMinValue == null || (castData).compareTo(intsMinValue) < 0) {
                intsMinValue = castData;
            }
        }
        if (data instanceof String castData) {
            BigInteger lineSize = BigInteger.valueOf(castData.length());
            if (stringsMaxLength == null || (lineSize).compareTo(stringsMaxLength) > 0) {
                stringsMaxLength = lineSize;
            }
            if (stringsMinLength == null || (lineSize).compareTo(stringsMinLength) < 0) {
                stringsMinLength = lineSize;
            }
        }
    }

    @Override
    public void showStatistics() {
        System.out.printf("integers stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", intsCount, intsMinValue, intsMaxValue, intsAverage, intsSum);

        System.out.printf("floats stats - count - %S, min - %S, max - %S, average - %S, sum - %S \n", floatsCount, floatsMinValue, floatsMaxValue, floatsAverage, floatsSum);

        System.out.printf("strings stats - count - %S, minLength - %S, maxLength - %S \n", stringsCount, stringsMinLength, stringsMaxLength);
    }
}
