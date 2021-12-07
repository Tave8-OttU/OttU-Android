package com.tave8.ottu.data;

import java.time.LocalDate;

public class PaymentInfo {
    private Long paymentId;     //teamIdx와 동일함
    private int platformId;
    private int headCount;
    private LocalDate paymentDate;

    public PaymentInfo(Long paymentId, int platformId, int headCount, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.platformId = platformId;
        this.headCount = headCount;
        this.paymentDate = paymentDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public int getHeadCount() {
        return headCount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
