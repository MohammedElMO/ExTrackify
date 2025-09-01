package com.example.extrackify.models

import android.util.Log
import com.example.extrackify.appwrite.AppWriteService
import com.example.extrackify.utils.SessionManager
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val appWriteService: AppWriteService,
    private val sessionManager: SessionManager
) {
    
    companion object {
        private const val DATABASE_ID = "extrackify_db"
        private const val EXPENSES_COLLECTION_ID = "expenses"
        private const val CATEGORIES_COLLECTION_ID = "categories"
    }

    suspend fun addExpense(expense: ExpenseModel): Document<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            val userId = sessionManager.getUserId() ?: throw Exception("User not authenticated")
            
            val data = mapOf(
                "userId" to userId,
                "amount" to expense.amount,
                "category" to expense.category,
                "description" to expense.description,
                "date" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(expense.date),
                "paymentMethod" to expense.paymentMethod,
                "isRecurring" to expense.isRecurring,
                "tags" to expense.tags,
                "createdAt" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date()),
                "updatedAt" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
            )
            
            appWriteService.db.createDocument(
                databaseId = DATABASE_ID,
                collectionId = EXPENSES_COLLECTION_ID,
                documentId = ID.unique(),
                data = data
            )
        }
    }

    suspend fun getExpenses(limit: Int = 50, offset: Int = 0): List<ExpenseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = sessionManager.getUserId() ?: throw Exception("User not authenticated")
                
                val response = appWriteService.db.listDocuments(
                    databaseId = DATABASE_ID,
                    collectionId = EXPENSES_COLLECTION_ID,
                    queries = listOf(
                        Query.equal("userId", userId),
                        Query.orderDesc("createdAt"),
                        Query.limit(limit),
                        Query.offset(offset)
                    )
                )
                
                response.documents.map { doc ->
                    documentToExpense(doc)
                }
            } catch (e: Exception) {
                Log.e("ExpenseRepository", "Error fetching expenses: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun getExpensesByDateRange(startDate: Date, endDate: Date): List<ExpenseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = sessionManager.getUserId() ?: throw Exception("User not authenticated")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                
                val response = appWriteService.db.listDocuments(
                    databaseId = DATABASE_ID,
                    collectionId = EXPENSES_COLLECTION_ID,
                    queries = listOf(
                        Query.equal("userId", userId),
                        Query.greaterThanEqual("date", dateFormat.format(startDate)),
                        Query.lessThanEqual("date", dateFormat.format(endDate)),
                        Query.orderDesc("date")
                    )
                )
                
                response.documents.map { doc ->
                    documentToExpense(doc)
                }
            } catch (e: Exception) {
                Log.e("ExpenseRepository", "Error fetching expenses by date: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun getExpenseSummary(period: String): ExpenseSummary {
        return withContext(Dispatchers.IO) {
            try {
                val expenses = when (period) {
                    "daily" -> getTodayExpenses()
                    "monthly" -> getCurrentMonthExpenses()
                    "yearly" -> getCurrentYearExpenses()
                    else -> getExpenses()
                }
                
                val totalAmount = expenses.sumOf { it.amount }
                val categoryBreakdown = expenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
                
                ExpenseSummary(
                    totalAmount = totalAmount,
                    categoryBreakdown = categoryBreakdown,
                    period = period,
                    transactionCount = expenses.size
                )
            } catch (e: Exception) {
                Log.e("ExpenseRepository", "Error getting summary: ${e.message}")
                ExpenseSummary()
            }
        }
    }

    private suspend fun getTodayExpenses(): List<ExpenseModel> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfDay = calendar.time
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.time
        
        return getExpensesByDateRange(startOfDay, endOfDay)
    }

    private suspend fun getCurrentMonthExpenses(): List<ExpenseModel> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfMonth = calendar.time
        
        calendar.add(Calendar.MONTH, 1)
        val endOfMonth = calendar.time
        
        return getExpensesByDateRange(startOfMonth, endOfMonth)
    }

    private suspend fun getCurrentYearExpenses(): List<ExpenseModel> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfYear = calendar.time
        
        calendar.add(Calendar.YEAR, 1)
        val endOfYear = calendar.time
        
        return getExpensesByDateRange(startOfYear, endOfYear)
    }

    suspend fun deleteExpense(expenseId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                appWriteService.db.deleteDocument(
                    databaseId = DATABASE_ID,
                    collectionId = EXPENSES_COLLECTION_ID,
                    documentId = expenseId
                )
                true
            } catch (e: Exception) {
                Log.e("ExpenseRepository", "Error deleting expense: ${e.message}")
                false
            }
        }
    }

    private fun documentToExpense(doc: Document<Map<String, Any>>): ExpenseModel {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        
        return ExpenseModel(
            id = doc.id,
            userId = doc.data["userId"] as? String ?: "",
            amount = (doc.data["amount"] as? Number)?.toDouble() ?: 0.0,
            category = doc.data["category"] as? String ?: "",
            description = doc.data["description"] as? String ?: "",
            date = try {
                dateFormat.parse(doc.data["date"] as? String ?: "") ?: Date()
            } catch (e: Exception) {
                Date()
            },
            paymentMethod = doc.data["paymentMethod"] as? String ?: "",
            isRecurring = doc.data["isRecurring"] as? Boolean ?: false,
            tags = (doc.data["tags"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            createdAt = try {
                dateFormat.parse(doc.data["createdAt"] as? String ?: "") ?: Date()
            } catch (e: Exception) {
                Date()
            },
            updatedAt = try {
                dateFormat.parse(doc.data["updatedAt"] as? String ?: "") ?: Date()
            } catch (e: Exception) {
                Date()
            }
        )
    }
}