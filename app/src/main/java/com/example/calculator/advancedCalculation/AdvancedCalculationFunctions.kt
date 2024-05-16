package com.example.calculator.advancedCalculation

import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import com.example.calculator.AdvancedCalculator
import com.example.calculator.simpleCalculatorFunctions.SimpleCalculatorFunctions
import com.example.calculator.AdvancedCalculator.Companion.equationTextAdvanced
import com.example.calculator.AdvancedCalculator.Companion.isParenthesesOpened
import com.example.calculator.AdvancedCalculator.Companion.isResultedAsked
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.sqrt

class AdvancedCalculationFunctions(private val context: Context) {
    private val calculatorFunctions = SimpleCalculatorFunctions(context)

    fun addTextToEquationAdvanced(
        currentEquation: String,
        text: String,
    ): String {
        val signs = listOf('+', '-', '*', '/')
        if (text.first() in '0'..'9' || text[0] == '.') {
            if(text[0]=='.'){
                if(currentEquation.contains('.'))return currentEquation
            }
            if (isResultedAsked) {
                isResultedAsked = false
                return text[0].toString()
            }
            return currentEquation + text[0]
        } else if (text[0] == '-' || text[0] == '+' || text[0] == '*' || text[0] == '/') {
            if (!isResultedAsked) {
                if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {

                    return currentEquation + text[0]
                } else if (currentEquation.last() == '-' || currentEquation.last() == '+' || currentEquation.last() == '*' || currentEquation.last() == '/') {
                    return currentEquation.dropLast(1) + text[0]
                }
            }

        } else if (text[0] == '=') {

            if (calculatorFunctions.validateOperation(currentEquation) > 0) {
                if (calculatorFunctions.validateOperation(currentEquation) == 1) {
                    calculatorFunctions.toastMessage("ERROR CANNOT DIVIDE BY 0")

                }
                if (calculatorFunctions.validateOperation(currentEquation) == 2) {
                    calculatorFunctions.toastMessage("ERROR CANNOT TAKE SQUARE ROOT OF NEGATIVE NUMBER")
                }
                if (calculatorFunctions.validateOperation(currentEquation) == 3) {
                    calculatorFunctions.toastMessage("ERROR CANNOT OPERATE LOG10 OF 0 AND NEGATIVE NUMBER ")
                }
                if (calculatorFunctions.validateOperation(currentEquation) == 4) {
                    calculatorFunctions.toastMessage("ERROR CANNOT OPERATE TAN(PI/2) ")
                }
                if (calculatorFunctions.validateOperation(currentEquation) == 5) {
                    calculatorFunctions.toastMessage("ERROR CHECK YOUR OPERATION ")
                    return equationTextAdvanced
                }
                if (calculatorFunctions.validateOperation(currentEquation) == 6) {
                    calculatorFunctions.toastMessage("ERROR NATURAL LOGARITHM IS DEFINED OF POSITIVE NUMBER ")

                }
                equationTextAdvanced = ""
            } else {
                var result = 0.0
                isParenthesesOpened = 0
                isResultedAsked = true
                // to follow the rules of documentation
                val replaceLogToLo10 = currentEquation.replace("log", "log10")
                var replaceLnToLog = replaceLogToLo10.replace("ln", "log")
                if (areParenthesesBalanced(replaceLnToLog)) {
                    if (replaceLnToLog.isNotEmpty()) {
                        result = if (replaceLnToLog.last() in signs) {
                            ExpressionBuilder(replaceLnToLog.dropLast(1)).build().evaluate()
                        } else ExpressionBuilder(replaceLnToLog).build().evaluate()
                    }
                } else if (!areParenthesesBalanced(replaceLnToLog)) {
                    val fixParentheses = replaceLnToLog.count{it == '('}-replaceLnToLog.count{it==')'}
                    for(i in 1..fixParentheses){
                        replaceLnToLog+=')'
                    }
                    result = ExpressionBuilder(replaceLnToLog).build().evaluate()

                }
                equationTextAdvanced = if (result % 1.0 == 0.0) result.toInt().toString()
                else result.toString()

            }

            return equationTextAdvanced

        } else if (text[0] == '±') {
            if(currentEquation.isEmpty() || currentEquation.first()=='-')return currentEquation
            return  calculatorFunctions.changeSignOfNumber(currentEquation)


        } else if (text[0] == 'C') {
            if (isResultedAsked) {
                isResultedAsked = false
                isParenthesesOpened=0
                return ""
            }
            return currentEquation.dropLast(1)


        } else if (text[0] == 'A') {
            isResultedAsked = false
            isParenthesesOpened=0
            return ""
        } else if (text[0] == 's') {
            isParenthesesOpened=openParentheses(isParenthesesOpened)
            if (text[1] == 'i') {
                if (!isResultedAsked) {
                    if (currentEquation.isNotEmpty()) {
                        if (currentEquation.last() in signs || currentEquation.last()=='(') {

                            return currentEquation + "sin("
                        }
                        if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                            return "$currentEquation*sin("
                        }
                    }
                }

                isResultedAsked = false
                return "sin("
            }
            if (text[1] == 'q') {
                if (!isResultedAsked) {
                    if (currentEquation.isNotEmpty()) {
                        if (currentEquation.last() in signs || currentEquation.last()=='(') {

                            return currentEquation + "sqrt("
                        }
                        if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                            return "$currentEquation*sqrt("
                        }
                    }
                }
                isResultedAsked = false
                return "sqrt("
            }
        } else if (text[0] == 'c' && text[1] == 'o') {
            isParenthesesOpened=openParentheses(isParenthesesOpened)
            if (!isResultedAsked) {
                if (currentEquation.isNotEmpty()) {
                    if (currentEquation.last() in signs || currentEquation.last()=='(') {

                        return currentEquation + "cos("
                    }
                    if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                        return "$currentEquation*cos("
                    }
                }
            }

            isResultedAsked = false
            return "cos("
        } else if (text[0] == 't' && text[1] == 'a') {
            isParenthesesOpened=openParentheses(isParenthesesOpened)
            if (!isResultedAsked) {
                if (currentEquation.isNotEmpty()) {
                    if (currentEquation.last() in signs || currentEquation.last()=='(' ) {

                        return currentEquation + "tan("
                    }
                    if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                        return "$currentEquation*tan("
                    }
                }
            }

            isResultedAsked = false
            return "tan("
        } else if (text[0] == 'l' && text[1] == 'n') {
            isParenthesesOpened=openParentheses(isParenthesesOpened)
            if (!isResultedAsked) {
                if (currentEquation.isNotEmpty()) {
                    if (currentEquation.last() in signs || currentEquation.last()=='(') {

                        return currentEquation + "ln("
                    }
                    if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                        return "$currentEquation*ln("
                    }
                }
            }

            isResultedAsked = false
            return "ln("
        } else if (text[0] == 'l' && text[1] == 'o') {
            isParenthesesOpened=openParentheses(isParenthesesOpened)
            if (!isResultedAsked) {
                if (currentEquation.isNotEmpty()) {
                    if (currentEquation.last() in signs || currentEquation.last()=='(') {

                        return currentEquation + "log("
                    }
                    if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                        return "$currentEquation*log("
                    }
                }
            }
            isResultedAsked = false
            return "log("
        } else if (text[0] == 'x') {
            if(currentEquation.isNotEmpty() && currentEquation.last() in '1'..'9'){
                if (text[2] == '2') {
                    if (!isResultedAsked) {
                        if (currentEquation.isNotEmpty()) {
                            if (currentEquation.last() in signs) {

                                return "$currentEquation^(2)"
                            }
                            if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                                return "$currentEquation^(2)"
                            }
                        }
                    }

                    isResultedAsked = false
                    return "^(2)"
                }

                if (text[2] == 'y') {
                    isParenthesesOpened=openParentheses(isParenthesesOpened)
                    if (!isResultedAsked) {
                        if (currentEquation.isNotEmpty()) {
                            if (currentEquation.last() in signs) {

                                return "$currentEquation^("
                            }
                            if (currentEquation.last() in '0'..'9' || currentEquation.last() == ')') {
                                return "$currentEquation^("
                            }
                        }
                    }

                    isResultedAsked = false
                    return "^("
                }
            }
            return currentEquation


        }
        else if (text[0] == ')'){
            if(currentEquation.isNotEmpty()){
                if(isParenthesesOpened>0 && currentEquation.last()!='('){
                    isParenthesesOpened = closeParentheses(isParenthesesOpened)
                    return "$currentEquation)"
                }
            }

            return currentEquation
        }
        else if (text[0] == '('){
            isParenthesesOpened = openParentheses(isParenthesesOpened)
            if(isResultedAsked){
              return "("
            }
            return "$currentEquation("
        }
        return currentEquation
    }
    enum class ScreenSize {
        PHONE,
        TABLET
    }



    fun removeButtonTints(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            println("okay $child.id")
            if (child is ViewGroup) {

                removeButtonTints(child)
            } else if (child is Button) {
                child.backgroundTintList = null
                AdvancedCalculator.buttonIdsAdvanced.add(child.id)
            }
        }
    }

    private fun openParentheses(isParenthesesOpened : Int):Int{
        return isParenthesesOpened+1
    }

    private fun closeParentheses(isParenthesesOpened : Int):Int{
        if(isParenthesesOpened-1<0)return 0;
        return isParenthesesOpened-1;
    }

    private fun areParenthesesBalanced(expression: String): Boolean {
        val stack = ArrayDeque<Char>()

        for (char in expression) {
            when (char) {
                '(' -> stack.addLast(char)
                ')' -> {
                    if (stack.isEmpty()) {
                        return false
                    }
                    stack.removeLast()
                }
            }
        }

        return stack.isEmpty()
    }

}