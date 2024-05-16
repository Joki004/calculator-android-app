package com.example.calculator


import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log;
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.calculator.simpleCalculatorFunctions.SimpleCalculatorFunctions
import com.example.calculator.advancedCalculation.AdvancedCalculationFunctions


class AdvancedCalculator : AppCompatActivity() {

    companion object {
        val buttonIdsAdvanced = mutableListOf<Int>()
        var equationTextAdvanced = ""
        var resultTextAdvanced = ""
        var isResultedAsked = false
        var isParenthesesOpened = 0

    }

    private lateinit var equationTextView: TextView
    private val calculatorFunctions = SimpleCalculatorFunctions(this)
    private val calculatorFunctionsAdvanced = AdvancedCalculationFunctions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_calculator)
        Log.d("advanced calculator", "onCreate called")
        equationTextView = findViewById(R.id.textViewResult)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        calculatorFunctionsAdvanced.removeButtonTints(rootView)

        Log.d("advanced calculator", "$buttonIdsAdvanced")
        setupButtonListeners()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val rootView = findViewById<ViewGroup>(android.R.id.content)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("SimpleCalculator", "Landscape orientation")

           // calculatorFunctionsAdvanced.adjustButtonTextSizeForPhone(rootView, 11)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("SimpleCalculator", "Portrait orientation")
           // calculatorFunctionsAdvanced.adjustButtonTextSizeForPhone(rootView, 15)
        }

        // Assuming equationText holds the current equation string
        equationTextView.text = equationTextAdvanced
    }

    private fun setupButtonListeners() {


        buttonIdsAdvanced.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener { v ->
                val button = v as Button
                Log.d("ButtonText", "Text: ${button.text}")
                equationTextAdvanced =
                    calculatorFunctionsAdvanced.addTextToEquationAdvanced(
                        equationTextAdvanced,
                        button.text.toString()
                    )

                Log.d("ButtonText", "current equation : ${equationTextAdvanced}")
                calculatorFunctions.updateEquationText(
                    equationTextAdvanced,
                    equationTextView
                )

                if (calculatorFunctions.checkIfStringHasSignOperators(equationTextAdvanced)) {
                    if (equationTextAdvanced.last() in '0'..'9' || equationTextAdvanced.last() == ')') {
                        val result =
                            calculatorFunctions.evaluateExpression(equationTextAdvanced)
                        resultTextAdvanced =
                            if (result % 1.0 == 0.0) result.toInt().toString()
                            else result.toString()

                    }
                }
            }
        }
    }

}