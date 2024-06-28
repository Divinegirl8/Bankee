package com.Avy.bank.utils;

import java.util.UUID;


public class AccountNumberGenerator {
    private static final int COUNTER_START = 1;
    private static int counter = COUNTER_START;

    public static String generateAccountNumber() {
        int baseAccountNumber = counter++;
        int checksumDigit = calculateLuhnChecksum(baseAccountNumber);
        return String.format("%09d%d", baseAccountNumber, checksumDigit);
    }

    private static int calculateLuhnChecksum(int baseAccountNumber) {
        String accountNumberStr = String.valueOf(baseAccountNumber);
        int sum = 0;
        boolean alternate = false;

        for (int i = accountNumberStr.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(accountNumberStr.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        int checksumDigit = (10 - (sum % 10)) % 10;
        return checksumDigit;
    }

}

