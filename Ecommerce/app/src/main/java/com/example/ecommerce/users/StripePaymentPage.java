package com.example.ecommerce.users;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecommerce.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class StripePaymentPage extends AppCompatActivity {



    private String name, price, description, productImageUrl,productId;

    private  String quantity;
    private Button payment;
    private String PublishableKey = "pk_test_51QfwnlFTdqp1BVfOlcBaG0SJftyxo4zULXt6Tb2FrKwvTvgUaTAAdxRhH9qh5mmqXySE3LeDeVg7SeKOCt6TqeqC00sH1k2SD4";
    private String SecretKey = "sk_test_51QfwnlFTdqp1BVfOCnwqUZnmOr1YBOTR6Huz4x2g6UUE80LP3NBBDCI6HtydFQV5iadBWKJvFoVmgCqE5dx1jd4s00a7bnJiKA";

    private String CustomersURL = "https://api.stripe.com/v1/customers";
    private String EphericalKeyURL = "https://api.stripe.com/v1/ephemeral_keys";
    private String ClientSecretURL = "https://api.stripe.com/v1/payment_intents";

    private String CustomerId = null;
    private String EphericalKey;
    private String ClientSecret;
    private PaymentSheet paymentSheet;
//    private String Amount = "200";  //
    private String Currency = "usd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stripe_payment_page);




        Intent intent = getIntent();
        name = intent.getStringExtra("productName");
         price = intent.getStringExtra("productPrice");
         description = intent.getStringExtra("productDescription");
         productImageUrl = intent.getStringExtra("productImageUrl");
        quantity = intent.getStringExtra("productQuantity");
        productId = intent.getStringExtra("productId");

        payment = findViewById(R.id.payment);

        PaymentConfiguration.init(this, PublishableKey);

        paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        payment.setOnClickListener(view -> {
            if (CustomerId != null && !CustomerId.isEmpty()) {
                paymentFlow();
            } else {
                Toast.makeText(StripePaymentPage.this, "Customer ID is not available", Toast.LENGTH_SHORT).show();
            }
        });


        createCustomer();
    }

    private void createCustomer() {
        StringRequest request = new StringRequest(Request.Method.POST, CustomersURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    CustomerId = object.getString("id");
                    Log.d("Stripe", "Customer created: " + CustomerId);
                    Toast.makeText(StripePaymentPage.this, "Customer ID: " + CustomerId, Toast.LENGTH_SHORT).show();


                    if (CustomerId != null && !CustomerId.isEmpty()) {
                        getEphericalKey();
                    } else {
                        Toast.makeText(StripePaymentPage.this, "Failed to create customer", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StripePaymentPage.this, "Error creating customer: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StripePaymentPage.this, "Error creating customer: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getEphericalKey() {
        StringRequest request = new StringRequest(Request.Method.POST, EphericalKeyURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    EphericalKey = object.getString("id");
                    Log.d("Stripe", "Ephemeral Key created: " + EphericalKey);

                    // Now get the Client Secret after the Ephemeral Key is fetched
                    if (EphericalKey != null && !EphericalKey.isEmpty()) {
                        getClientSecret(CustomerId, EphericalKey);
                    } else {
                        Toast.makeText(StripePaymentPage.this, "Failed to fetch ephemeral key", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StripePaymentPage.this, "Error fetching ephemeral key: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StripePaymentPage.this, "Error fetching ephemeral key: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                headers.put("Stripe-Version", "2022-11-15");
                return headers;
            }

            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getClientSecret(String customerId, String ephemeralKey) {
        StringRequest request = new StringRequest(Request.Method.POST, ClientSecretURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ClientSecret = object.getString("client_secret");
                    Log.d("Stripe", "Client Secret created: " + ClientSecret);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StripePaymentPage.this, "Error fetching client secret: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StripePaymentPage.this, "Error fetching client secret: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }

            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                String priceWithoutDollarSign = price.replace("$", "").trim();
                double priceInDouble = Double.parseDouble(priceWithoutDollarSign);

                int amountInCents = (int) (priceInDouble * 100);


                params.put("customer", customerId);
                params.put("amount", String.valueOf(amountInCents));
                params.put("currency", Currency);
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void paymentFlow() {
        if (ClientSecret != null && !ClientSecret.isEmpty()) {
            paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Stripe", new PaymentSheet.CustomerConfiguration(
                    CustomerId, EphericalKey
            )));
        } else {
            Toast.makeText(StripePaymentPage.this, "Client Secret not available", Toast.LENGTH_SHORT).show();
        }
    }
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(StripePaymentPage.this,OrderConfirmedPage.class);
            intent.putExtra("productName", name);
            intent.putExtra("productPrice", price);
            intent.putExtra("productDescription", description);
            intent.putExtra("productImageUrl", productImageUrl);
            intent.putExtra("productQuantity",quantity);
            intent.putExtra("productId",productId);

            startActivity(intent);

        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
