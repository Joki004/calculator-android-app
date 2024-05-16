package com.example.calculator.simpleCalculatorFunctions

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.calculator.SimpleCalculator
import com.example.calculator.SimpleCalculator.Companion.resultText
import com.example.calculator.SimpleCalculator.Companion.equationText

import java.util.Stack
import java.util.*
import kotlin.math.PI

class SimpleCalculatorFunctions(private val context: Context) {


    fun addTextToEquation(
        currentEquation: String,
        text: Char,
        resultTextView: TextView,
    ): String {

        if (text in '0'..'9' || text == '.') {
            if(text=='.'){
                if(currentEquation.contains('.'))return currentEquation
            }
            return currentEquation + text
        } else if (text == '-' || text == '+' || text == '*' || text == '/') {
            if (currentEquation.isNotEmpty()) {
                if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                    return currentEquation + text
                } else if (currentEquation.last() == '-' || currentEquation.last() == '+' || currentEquation.last() == '*' || currentEquation.last() == '/') {
                    return currentEquation.dropLast(1) + text
                }
            }

        } else if (text == '=') {
            if (validateOperation(currentEquation) > 0) {
                if (validateOperation(currentEquation) == 1) {
                    toastMessage("ERROR CANNOT DIVIDE BY 0")
                    updateResultText("", resultTextView)
                }
            } else {
                val result = evaluateExpression(currentEquation)
                resultText = if (result % 1.0 == 0.0) result.toInt().toString()
                else result.toString()
                updateResultText(resultText, resultTextView)
            }
            equationText = ""
            return equationText

        } else if (text == '±') {
            return changeSignOfNumber(currentEquation)
        } else if (text == 'C') {

            return currentEquation.dropLast(1)


        } else if (text == 'A') {
            updateResultText("", resultTextView)
            return ""
        }
        return currentEquation
    }

    fun updateEquationText(currentEquation: String, equationTextView: TextView) {
        equationTextView.text = currentEquation
    }

    fun updateResultText(result: String, resultTextView: TextView) {
        resultTextView.text = result

    }

    fun evaluateExpression(expression: String): Double {
        val outputQueue: Queue<String> = LinkedList()
        val operatorStack: Stack<String> = Stack()
        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)


        val regex = Regex("([()*+/^-]|\\b-\\d+\\.?\\d*|\\d+\\.?\\d*)")
        val tokens = regex.findAll(expression.replace(" ", "")).map {
            val value = it.value
            if (value.startsWith("-") && (it.range.first == 0 || expression[it.range.first - 1] == '(')) {
                "u$value"
            } else {
                value
            }
        }.toList()


        tokens.forEach { token ->
            when {
                token.toDoubleOrNull() != null -> outputQueue.add(token)
                token.startsWith("u-") -> outputQueue.add(token)
                token == "(" -> operatorStack.push(token)
                token == ")" -> {
                    while (operatorStack.peek() != "(") {
                        outputQueue.add(operatorStack.pop())
                    }
                    operatorStack.pop()
                }

                token in precedence -> {
                    while (operatorStack.isNotEmpty() && (precedence[operatorStack.peek()]
                            ?: 0) >= precedence[token]!!
                    ) {
                        outputQueue.add(operatorStack.pop())
                    }
                    operatorStack.push(token)
                }
            }
        }


        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.pop())
        }


        val evaluationStack: Stack<Double> = Stack()
        val iterator = outputQueue.iterator()
        while (iterator.hasNext()) {
            val token = iterator.next()
            when {
                token.toDoubleOrNull() != null -> evaluationStack.push(token.toDouble())
                token.startsWith("u-") -> {

                    if (iterator.hasNext()) {
                        val nextNumber = iterator.next().toDoubleOrNull()
                            ?: throw IllegalArgumentException("Expected a number after unary minus, found: $token")
                        evaluationStack.push(-nextNumber)
                    } else {
                        throw IllegalArgumentException("Unary minus at the end of the expression without a number to follow")
                    }
                }

                token in precedence -> {
                    val right = evaluationStack.pop()
                    val left = if (evaluationStack.isNotEmpty()) evaluationStack.pop() else 0.0
                    val result = when (token) {
                        "+" -> left + right
                        "-" -> left - right
                        "*" -> left * right
                        "/" -> left / right
                        else -> throw IllegalArgumentException("Unknown operator: $token")
                    }
                    evaluationStack.push(result)
                }
            }
        }


        return evaluationStack.pop()
    }

    fun changeSignOfNumber(expression: String): String {

        if (expression.last() in '0'..'9') {
            val signs = listOf('+', '-', '*', '/')
            val advancedOperation = listOf('n', 's', 't', '^', 'g')
            var indexFromEnd = expression.indexOfLast { it in signs }
            val numberAfterLastSign: String
            val expressionWithoutLastNumber: String
            return if (indexFromEnd != -1 && indexFromEnd > expression.indexOfLast { it in advancedOperation }) {
                numberAfterLastSign = expression.substring(indexFromEnd + 1)
                expressionWithoutLastNumber = expression.substring(0, indexFromEnd + 1)
                "$expressionWithoutLastNumber(-$numberAfterLastSign)"
            } else {
                indexFromEnd = expression.indexOfLast { it in advancedOperation }
                if (indexFromEnd == -1 || ((expression.contains("^") && expression.last() == ')'))) {
                    "(-$expression)"
                } else {
                    numberAfterLastSign = expression.substring(indexFromEnd + 2)
                    expressionWithoutLastNumber = expression.substring(0, indexFromEnd + 2)
                    "$expressionWithoutLastNumber(-$numberAfterLastSign)"
                }
            }
        } else if (expression.last() == ')') {

            if (expression.takeLast(4).contains("^(2)")) return expression
            else {
                val signs = listOf('(')
                val indexFromEnd = expression.indexOfLast { it in signs }
                if (indexFromEnd != -1 && indexFromEnd>0) {

                        if (expression[indexFromEnd - 1] == '(') {
                            toastMessage("ERROR CANNOT CHANGE THE SIGN JUST DELETE INPUTS")
                            return expression


                    }
                }
                else {
                    val numberAfterLastSign = expression.substring(indexFromEnd)
                    val expressionWithoutLastNumber = expression.substring(0, indexFromEnd)
                    val stringWithoutParens = numberAfterLastSign.replace(Regex("[()-]"), "")
                   return "$expressionWithoutLastNumber$stringWithoutParens"
                }
            }

        }
        return expression
    }

    fun checkIfStringHasSignOperators(expression: String): Boolean {

        val signOperators = listOf('+', '-', '*', '/')
        for (char in expression) {

            if (char in signOperators) {
                return true
            }
        }
        return false
    }

    fun removeButtonTints(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            println("okay $child.id")
            if (child is ViewGroup) {

                removeButtonTints(child)
            } else if (child is Button) {
                child.backgroundTintList = null
                SimpleCalculator.buttonIds.add(child.id)
            }
        }


    }

    fun validateOperation(expression: String): Int {
        if (expression.contains("/0")) return 1
        if (expression.contains("sqrt(-")) return 2
        if (expression.contains("log(0")) return 3
        if (expression.contains("tan(${PI / 2}")) return 4
        if(expression.contains("ln((-"))return 6
        return if (expression.last() in '0'..'9' || expression.last() == ')') 0
        else 5

    }

    fun toastMessage(message: String) {
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(context, message, duration).show()
    }
}