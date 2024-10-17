package com.example.transaction_service.enums;


public enum OperationType {
    NORMAL_PURCHASE(1, "Normal Purchase"),
    PURCHASE_INSTALLMENTS(2, "Purchase with Installments"),
    WITHDRAWAL(3, "Withdrawal"),
    CREDIT_VOUCHER(4, "Credit Voucher"),
    INSTALLMENT_PAYMENT(5, "Installment payment");

    private final int id;
    private final String description;

    OperationType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static OperationType fromId(int id) {
        for (OperationType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid OperationType ID: " + id);
    }

    public int getId()
    {
        return id;
    }
}
