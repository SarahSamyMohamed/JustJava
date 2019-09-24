
package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prevents Android on-screen keyboard auto popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {

        if (quantity < 100) {
            quantity = quantity + 1;
            // show an error message as a toast
        } else {
            Toast.makeText(this, getString(R.string.increment_toast), Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {

        if (quantity > 1) {
            quantity = quantity - 1;
            // show an error message as a toast
        } else {
            Toast.makeText(this, getString(R.string.decrement_toast), Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get the user name
        EditText nameField = findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        // Figure out if the user wants Whipped Cream topping
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummery(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_to_subject_value) + " " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        //only if there is an app that can handle this use it to start the activity otherwise do nothing
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate    is whether or not the user wants Chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        // price of 1 cup of coffee
        int basePrice = 5;

        // 2 if statements because they are 2 separate cases and independent of each other
        // add $1 if the user wants Whipped Cream
        if (hasWhippedCream)
            basePrice += 1;

        // add $2 if the user wants Chocolate
        if (hasChocolate)
            basePrice += 2;

        // calculate the total order price by multiplying by quantity
        return basePrice * quantity;
    }

    /**
     * create summary of the order
     *
     * @param name            of the customer
     * @param price           of the order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not the user wants Chocolate topping
     * @return text summery
     */
    private String createOrderSummery(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String summery = getString(R.string.order_summary_name, name);
        //TODO: true or false arabic translation
        summery += "\n" + getString(R.string.order_summary_whipped_cream) + " " + addWhippedCream;
        //TODO: true or false arabic translation
        summery += "\n" + getString(R.string.order_summary_chocolate) + " " + addChocolate;
        //TODO: arabic number formatting
        summery += "\n" + getString(R.string.order_summary_quantity) + " " + quantity;
        summery += "\n" + getString(R.string.order_summary_price, price);
        NumberFormat.getCurrencyInstance().format(price);
        summery += "\n" + getString(R.string.thank_you);
        return summery;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        //TODO: arabic number formatting
        quantityTextView.setText("" + number);
    }

}