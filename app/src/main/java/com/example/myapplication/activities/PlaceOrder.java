package com.example.myapplication.activities;

import static com.example.myapplication.activities.CategoriesDetail.TAG_MAIN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapters.CheckoutAdapter;
import com.example.myapplication.Model.Address;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.ProductListItem;
import com.example.myapplication.databinding.ActivityPlaceOrderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class PlaceOrder extends AppCompatActivity {
private ActivityPlaceOrderBinding binding;
private String addressId;
private ProgressDialog pd;
private CheckoutAdapter adapter;
private ArrayList<ProductListItem> productList;
private LinearLayoutManager llm;
private int cartTotal = 0;
private String completeAddress;
private String paymentMode;
int GOOGLE_PAY_REQUEST_CODE = 123;
public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
// payment code
    String amount;
    String name = "Yasier";
    String upiId = "yasirmohammed122002@okicici";
    String transactionNote = "Order purchase";
    String status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addressId = getIntent().getStringExtra("address");
        paymentMode = getIntent().getStringExtra("type");
        pd = new ProgressDialog(this);
        pd.setMessage("Wait a minute");
        productList = new ArrayList<>();
        llm = new LinearLayoutManager(this);


        // G Pay
        // configuring the project.

        getAddress();
        getProduct();

        if(paymentMode.equals("prepaid")){
            binding.placeOrderButton.setText("Pay with GPay");
        }

        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // placing the order on pressing place order button.
        binding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paymentMode.equals("cod")){
                    completeOrder();
                }else{
                    // implement Gpay
                    uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
                    payWithGpay();
                }
            }
        });
    }


    //  <- payment code starts ->
    private void payWithGpay() {
        if(appIsInstalled(this, GOOGLE_PAY_PACKAGE_NAME)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(PlaceOrder.this, "Please Install GPay.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == requestCode) && status.equals("success")){
            completeOrder();
            Toast.makeText(PlaceOrder.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PlaceOrder.this, "Transaction unsuccessful", Toast.LENGTH_SHORT).show();

        }
    }

    private static boolean appIsInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException exception) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }
    // Payment code ends.

    private void getProduct() {
        FirebaseDatabase.getInstance().getReference().
                child("Carts").
                child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    ProductListItem cartItem = snapshot1.getValue(ProductListItem.class);
                    cartTotal += cartItem.getProductPrice();
                    productList.add(cartItem);
                }
                // Setting total prices.
                binding.subTotalNumber.setText(String.valueOf("₹ " + cartTotal));
                amount = Integer.toString(cartTotal);
                binding.totalNumber.setText(String.valueOf("₹ " + cartTotal));
                // Setting up recyclerView
                adapter = new CheckoutAdapter(PlaceOrder.this, productList);
                binding.allItemRecyclerView.setAdapter(adapter);
                binding.allItemRecyclerView.setLayoutManager(llm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAddress() {
        pd.show();
        FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getUid())
                .child(addressId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Address address = snapshot.getValue(Address.class);
                binding.nameAddressCheckout.setText(address.getName());
                binding.cityNameAddress.setText(address.getCity());
                binding.phoneNumberAddressCheckout.setText(address.getPhoneNo());
                binding.addressCheckout.setText(address.getAddress());
                completeAddress = address.getName() + "  " + address.getPhoneNo() + "  " + address.getAddress() + "  " + address.getName();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void completeOrder() {
        String id = String.valueOf(System.currentTimeMillis()) + FirebaseAuth.getInstance().getUid();
        Order order = new Order(completeAddress, productList, String.valueOf(cartTotal), "placed", FirebaseAuth.getInstance().getUid(), id);
        FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child(id)
                .setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.w(TAG_MAIN, "Order added to database.");
                FirebaseDatabase.getInstance().getReference()
                        .child("Carts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .removeValue().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.w(TAG_MAIN, "Cart emptied.");
                                Toast.makeText(PlaceOrder.this, "Order Placed.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PlaceOrder.this, Checkout.class);
                                startActivity(intent);
                            }
                        });
            }
        });
    }
}