package com.tave8.ottu.data;

import java.time.LocalDate;

public class PaymentInfo {
    private Long paymentIdx;     //teamIdx와 동일함
    private int platformIdx;
    private int headCount;
    private int paymentDay;
    private LocalDate paymentDate;

    public PaymentInfo(Long paymentIdx, int platformIdx, int headCount, int paymentDay, LocalDate paymentDate) {
        this.paymentIdx = paymentIdx;
        this.platformIdx = platformIdx;
        this.headCount = headCount;
        this.paymentDay = paymentDay;
        this.paymentDate = paymentDate;
    }

    public Long getPaymentIdx() {
        return paymentIdx;
    }

    public int getPlatformIdx() {
        return platformIdx;
    }

    public int getHeadCount() {
        return headCount;
    }

    public int getPaymentDay() {
        return paymentDay;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
