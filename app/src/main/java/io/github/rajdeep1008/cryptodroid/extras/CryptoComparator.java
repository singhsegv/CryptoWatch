package io.github.rajdeep1008.cryptodroid.extras;

import java.util.Comparator;

import io.github.rajdeep1008.cryptodroid.data.Crypto;

/**
 * Created by rajdeep1008 on 31/01/18.
 */

public enum CryptoComparator implements Comparator<Crypto> {
    RANK {
        public int compare(Crypto c1, Crypto c2) {
            return Integer.valueOf(c1.getRank()).compareTo(Integer.valueOf(c2.getRank()));
        }
    },
    PHTL {
        public int compare(Crypto c1, Crypto c2) {
            if (c1.getPriceUsd() != null && c2.getPriceUsd() != null) {
                if (Double.valueOf(c1.getPriceUsd()) > Double.valueOf(c2.getPriceUsd())) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 1;
        }
    },
    PLTH {
        public int compare(Crypto c1, Crypto c2) {
            if (c1.getPriceUsd() != null && c2.getPriceUsd() != null) {
                if (Double.valueOf(c1.getPriceUsd()) > Double.valueOf(c2.getPriceUsd())) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 1;
        }
    },
    CHTL24 {
        public int compare(Crypto c1, Crypto c2) {
            if (c1.getPercentChange24h() != null && c2.getPercentChange24h() != null) {
                if (Double.valueOf(c1.getPercentChange24h()) > Double.valueOf(c2.getPercentChange24h())) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 1;
        }
    },
    CLTH24 {
        public int compare(Crypto c1, Crypto c2) {
            if (c1.getPercentChange24h() != null && c2.getPercentChange24h() != null) {
                if (Double.valueOf(c1.getPercentChange24h()) > Double.valueOf(c2.getPercentChange24h())) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 1;
        }
    },
    CHTL1 {
        public int compare(Crypto c1, Crypto c2) {
            if (c1.getPercentChange1h() != null && c2.getPercentChange1h() != null) {
                if (Double.valueOf(c1.getPercentChange1h()) > Double.valueOf(c2.getPercentChange1h())) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 1;
        }
    },
    CLTH1 {
        public int compare(Crypto c1, Crypto c2) {
            if (c1.getPriceUsd() != null && c2.getPriceUsd() != null) {
                if (Double.valueOf(c1.getPriceUsd()) > Double.valueOf(c2.getPriceUsd())) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 1;
        }
    };

    public static Comparator<Crypto> getComparator(final CryptoComparator comparator) {
        return new Comparator<Crypto>() {
            public int compare(Crypto o1, Crypto o2) {
                int result = comparator.compare(o1, o2);
                if (result != 0) {
                    return result;
                }
                return 0;
            }
        };
    }
}
