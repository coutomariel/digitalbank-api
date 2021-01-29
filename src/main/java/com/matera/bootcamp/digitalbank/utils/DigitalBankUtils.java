package com.matera.bootcamp.digitalbank.utils;

import com.matera.bootcamp.digitalbank.enumerator.Natureza;

import java.math.BigDecimal;

public class DigitalBankUtils {

    public static BigDecimal calculaSaldo(Natureza natureza, BigDecimal valor, BigDecimal saldoAtual) {
        BigDecimal saldoFinal;

        if (Natureza.DEBITO.equals(natureza)) {
            saldoFinal = saldoAtual.subtract(valor);
        } else {
            saldoFinal = saldoAtual.add(valor);
        }

        return saldoFinal;
    }
}
