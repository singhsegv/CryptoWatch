package io.github.rajdeep1008.cryptofolio.extras;

import java.util.Comparator;

import io.github.rajdeep1008.cryptofolio.data.Crypto;

/**
 * Created by rajdeep1008 on 31/01/18.
 */

public enum CryptoComparator implements Comparator<Crypto> {
    RANK {
        public int compare(Crypto c1, Crypto c2) {
            return Integer.valueOf(c1.getRank()).compareTo(Integer.valueOf(c2.getRank()));
        }
    },
    CHTL24 {
        public int compare(Crypto c1, Crypto c2) {
            return Double.compare(Double.valueOf(c1.getPercentChange24h()), Double.valueOf(c2.getPercentChange24h()));
        }
    },
    CLTH24 {
        public int compare(Crypto c1, Crypto c2) {
            return Double.compare(Double.valueOf(c2.getPercentChange24h()), Double.valueOf(c1.getPercentChange24h()));
        }
    },
    CHTL1 {
        public int compare(Crypto c1, Crypto c2) {
            return Double.compare(Double.valueOf(c1.getPercentChange1h()), Double.valueOf(c2.getPercentChange1h()));
        }
    },
    CLTH1 {
        public int compare(Crypto c1, Crypto c2) {
            return Double.compare(Double.valueOf(c2.getPercentChange1h()), Double.valueOf(c1.getPercentChange1h()));
        }
    }
}
