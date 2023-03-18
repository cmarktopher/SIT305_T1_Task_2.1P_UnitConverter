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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //---------------------------------------------------------------------------------------------
    // UI Element Properties
    //---------------------------------------------------------------------------------------------

    private Spinner sourceSpinner;
    private Spinner destinationSpinner;

    //---------------------------------------------------------------------------------------------
    // State Properties (Basically representing current choices of source/destinations unit etc)
    //---------------------------------------------------------------------------------------------

    private String chosenSourceUnits;
    private String chosenDestinationUnits;

    //---------------------------------------------------------------------------------------------
    // Initialization
    //---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HandleSpinnerInitialization();
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

    //---------------------------------------------------------------------------------------------
    // Spinner related callbacks
    //---------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // Here, we are getting the chosen text from the adapter
        String chosenOptionText = adapterView.getItemAtPosition(i).toString();

        // Toast message
        String toastMessage = "";

        // After some logging, I realised that the adapter view is just the spinner objects
        // Meaning, I can check if they are equal to the cached references, allowing me to differentiate when I selected a source or destination unit.
        if (adapterView == sourceSpinner){

            chosenSourceUnits = chosenOptionText;
            toastMessage = "Source units are: " + chosenSourceUnits;

        } else if (adapterView == destinationSpinner) {

            chosenDestinationUnits = chosenOptionText;
            toastMessage = "Destination units are: " + chosenDestinationUnits;
        }

        // Use a toast to show the unit selected - may remove later
        Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}