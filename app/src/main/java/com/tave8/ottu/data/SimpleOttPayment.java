package com.tave8.ottu.data;

import java.util.Date;

public class SimpleOttPayment {
    private int platformId;
    private Date paymentDate;

    public SimpleOttPayment(int platformId, Date paymentDate) {
        this.platformId = platformId;
        this.paymentDate = paymentDate;
    }

    public int getPlatformId() {
        return platformId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }
}
