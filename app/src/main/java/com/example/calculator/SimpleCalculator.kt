package com.example.calculator

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.simpleCalculatorFunctions.SimpleCalculatorFunctions
import com.example.calculator.advancedCalculation.AdvancedCalculationFunctions
import android.util.Log;
import android.widget.TextView
import android.widget.Toast

class SimpleCalculator : AppCompatActivity() {
    companion object {
        val buttonIds = mutableListOf<Int>()
        var equationText = ""
        var resultText = ""
    }

    private val calculatorFunctions = SimpleCalculatorFunctions(this)
    private val  calculatorFunctionsAdvanced = AdvancedCalculationFunctions(this)
    private lateinit var equationTextView: TextView
    private lateinit var resultTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calculator)
        Log.d("SimpleCalculator", "onCreate called")

        equationTextView = findViewById(R.id.equation)
        resultTextView = findViewById(R.id.textViewResult)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        calculatorFunctions.removeButtonTints(rootView)
        //creating listeners
        setupButtonListeners()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("SimpleCalculator", "Landscape orientation")
           //calculatorFunctionsAdvanced.adjustButtonTextSizeForPhone(rootView, 11)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("SimpleCalculator", "Portrait orientation")
            //calculatorFunctionsAdvanced.adjustButtonTextSizeForPhone(rootView, 11)
        }

        // Assuming equationText holds the current equation string
        equationTextView.text = equationText
    }

    private fun setupButtonListeners() {


        buttonIds.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener { v ->
                val button = v as Button
                Log.d("ButtonText", "Text: ${button.text}")
                equationText = calculatorFunctions.addTextToEquation(
                    equationText,
                    button.text.first(),
                    resultTextView
                );
                calculatorFunctions.updateEquationText(equationText, equationTextView)
                if(calculatorFunctions.checkIfStringHasSignOperators(equationText)){
                    if(equationText.last() in '0'..'9' || equationText.last()==')'){
                        if (calculatorFunctions.validateOperation(equationText) > 0) {
                            if (calculatorFunctions.validateOperation(equationText) == 1) {
                                calculatorFunctions.toastMessage("ERROR CANNOT DIVIDE BY 0")
                                calculatorFunctions.updateResultText("", resultTextView)
                                equationText = ""
                                equationTextView.text = equationText
                            }
                        }
                        else{
                            val result = calculatorFunctions.evaluateExpression(equationText)
                            resultText = if(result % 1.0 == 0.0) result.toInt().toString()
                            else result.toString()
                            calculatorFunctions.updateResultText(resultText, resultTextView)
                        }

                    }
                }
            }
        }
    }

}