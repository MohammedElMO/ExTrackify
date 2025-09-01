package com.example.extrackify.models

import java.util.Date

data class ExpenseModel(
    val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val date: Date = Date(),
    val paymentMethod: String = "",
    val isRecurring: Boolean = false,
    val tags: List<String> = emptyList(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class CategoryModel(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val color: String = "",
    val isDefault: Boolean = false
)

data class ExpenseSummary(
    val totalAmount: Double = 0.0,
    val categoryBreakdown: Map<String, Double> = emptyMap(),
    val period: String = "",
    val transactionCount: Int = 0
)

enum class ExpenseCategory(val displayName: String, val colorRes: Int) {
    FOOD("Food & Dining", android.R.color.holo_red_light),
    TRANSPORT("Transportation", android.R.color.holo_blue_light),
    ENTERTAINMENT("Entertainment", android.R.color.holo_purple),
    SHOPPING("Shopping", android.R.color.holo_green_light),
    BILLS("Bills & Utilities", android.R.color.holo_orange_light),
    HEALTH("Healthcare", android.R.color.holo_red_dark),
    EDUCATION("Education", android.R.color.holo_blue_dark),
    OTHER("Other", android.R.color.darker_gray)
}

enum class PaymentMethod(val displayName: String) {
    CASH("Cash"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    DIGITAL_WALLET("Digital Wallet"),
    BANK_TRANSFER("Bank Transfer"),
    OTHER("Other")
}