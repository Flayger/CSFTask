package org.flayger;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FileFilterShortStatistics implements FileFilterStatistics{

    private static Integer intsCount = 0;
    private static Integer floatsCount = 0;
    private static Integer stringsCount = 0;

    @Override
    public void updateStatistics(BigDecimal floatData) {
        intsCount++;
    }

    @Override
    public void updateStatistics(BigInteger intData) {
        floatsCount++;
    }

    @Override
    public void updateStatistics(String stringData) {
        stringsCount++;
    }

    @Override
    public void showStatistics() {
        System.out.printf("integers stats - count - %S\n", intsCount);

        System.out.printf("floats stats - count - %S\n", floatsCount);

        System.out.printf("strings stats - count - %S\n", stringsCount);
    }
}
