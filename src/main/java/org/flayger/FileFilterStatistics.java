package org.flayger;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface FileFilterStatistics {

    void updateStatistics(BigDecimal floatData);

    void updateStatistics(BigInteger intData);

    void updateStatistics(String stringData);

    void showStatistics();
}
