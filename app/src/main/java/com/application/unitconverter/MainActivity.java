/**
 * Some resources I used to learn how to do certain things such as populating a spinner menu and using interfaces
 * Spinner:
 * https://developer.android.com/develop/ui/views/components/spinner
 * https://www.tutorialspoint.com/how-to-get-spinner-value-in-android
 * Interfaces:
 * https://www.w3schools.com/java/java_lambda.asp#:~:text=Lambda%20expressions%20can%20be%20stored,return%20type%20as%20that%20method
 */

package com.application.unitconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;

/**
 * Required to store lambda expressions which will be the main way I do the conversion logic.
 */
interface IConversion{

    Float Convert(Float a);
}

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //---------------------------------------------------------------------------------------------
    // UI Element Properties
    //---------------------------------------------------------------------------------------------

    private Spinner sourceSpinner;
    private Spinner destinationSpinner;
    private EditText inputTextView;
    private TextView outputTextView;
    private Button convertButton;

    //---------------------------------------------------------------------------------------------
    // State Properties (Basically representing current choices of source/destinations unit etc)
    //---------------------------------------------------------------------------------------------

    private String chosenSourceUnits;
    private String chosenDestinationUnits;

    //---------------------------------------------------------------------------------------------
    // Conversion Properties
    //---------------------------------------------------------------------------------------------

    private HashMap<String, IConversion> conversionFactors = new HashMap<String, IConversion>();

    //---------------------------------------------------------------------------------------------
    // Initialization
    //---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HandleSpinnerInitialization();
        HandleInputAndOutputInitialization();
        HandleButtonInitialization();
        HandleConversionInitialization();
    }

    private void HandleSpinnerInitialization(){

        sourceSpinner = findViewById(R.id.source_unit_spinner);
        destinationSpinner = findViewById(R.id.destination_unit_spinner);

        // The following lines of code are from the android documentation
        // From my understanding, we are essentially creating a menu of selectable based on the xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.length_conversion_options,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the created adapter for both source and destination spinners since they will share the same options anyway
        sourceSpinner.setAdapter(adapter);
        destinationSpinner.setAdapter(adapter);

        // Also, we want to bind a callback whenever we change an option in the dropdown menus
        sourceSpinner.setOnItemSelectedListener(this);
        destinationSpinner.setOnItemSelectedListener(this);
    }

    private void HandleInputAndOutputInitialization(){

        inputTextView = findViewById(R.id.input_text);
        outputTextView = findViewById(R.id.output_text);
    }

    private void HandleButtonInitialization(){

        convertButton = findViewById(R.id.convert_button);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandeConversion(chosenSourceUnits, chosenDestinationUnits, inputTextView.getText().toString());
            }
        });
    }

    /**
     * Create lambda expressions and place them into a dictionary/hashmap.
     * We can then use the source and destination selections as our keys and call the interface method.
     * My understanding of this is that it's basically like how interfaces are used with classes in c#.
     * We define methods within the interface, and classes using it has to implement said methods which can be different between different class implementations
     * This is similar here, except it is used for methods.
     * We are defining the implementation differently in each lambda expressions (difference in constant values or multiplication/division)
     * and when we call the interface method later, the relevant implementation will run.
     */
    private void HandleConversionInitialization(){

        // Length conversion factors
        conversionFactors.put("inchcm", (a) -> { return a * 2.54f; });
        conversionFactors.put("cminch", (a) -> { return a / 2.54f; });

        // Weight Conversion
        conversionFactors.put("poundkg", (a) -> { return a * 0.453592f; });
        conversionFactors.put("kgpound", (a) -> { return  a / 0.453592f; });

        // Temperature Conversion
        conversionFactors.put("celsiusfahrenheit", (a) -> { return (a * 1.8f) + 32; });
        conversionFactors.put("fahrenheitcelsius", (a) -> { return (a - 32) / 1.8f; });
    }


    //---------------------------------------------------------------------------------------------
    // Spinner related callbacks
    //---------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // Here, we are getting the chosen text from the adapter
        String chosenOptionText = adapterView.getItemAtPosition(i).toString();

        // After some logging, I realised that the adapter view is just the spinner objects
        // Meaning, I can check if they are equal to the cached references, allowing me to differentiate when I selected a source or destination unit.
        if (adapterView == sourceSpinner){

            chosenSourceUnits = chosenOptionText;

        } else if (adapterView == destinationSpinner) {

            chosenDestinationUnits = chosenOptionText;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        // Nothing to add here but needs to exist due to AdapterView.OnItemSelectedListener being implemented

    }

    //---------------------------------------------------------------------------------------------
    // Conversion Logic
    //---------------------------------------------------------------------------------------------

    /**
     * Handles conversion of source to destination value.
     * Only adding params because of task requirements.
     * Not really needed because I am caching these anyways in their own class variables.
     * However, perhaps there is a better way to do this compared to the way I did it that would benefit/rely on these method params.
     */
    private void HandeConversion(String sourceUnits, String destinationUnits, String inputText){

        // First, we need to convert the input into a numerical type - will just use float for now
        Float convertedInput = Float.parseFloat(inputText.toString());

        // Append the two unit strings together so that we can get our key for the conversion map
        String key = sourceUnits + destinationUnits;

        // Check if key exits, if it does, perform the conversion
        if (conversionFactors.containsKey(key)) {

            IConversion conversionValue = conversionFactors.get(key);

            // This is where the interface usefulness comes in.
            // At this point, we don't need to care how the interface implements the conversion since that was defined earlier.
            // Saves me having to do a conditional check for every type of conversion
            Float convertedValue = conversionValue.Convert(convertedInput);

            // Set the display text to the new value
            outputTextView.setText(convertedValue.toString());
        }

    }
}