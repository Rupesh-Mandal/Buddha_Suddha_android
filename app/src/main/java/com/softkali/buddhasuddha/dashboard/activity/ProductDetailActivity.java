package com.softkali.buddhasuddha.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.DashboardActivity;
import com.softkali.buddhasuddha.dashboard.model.Product;
import com.softkali.buddhasuddha.database.DatabaseClient;
import com.softkali.buddhasuddha.database.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView productImageLink;
    TextView productName,productDescription,productRate,productType,productDeliverCharge,location;

    Product product;

    JSONObject useerObject;
    JSONObject productObject;
    private TextView item_count_in_cart,add_to_cart_btn;

    SharedPreferences sharedPreferences;

    private static ProgressDialog progressDialog;

    boolean isOtherPhone=false;
    String otherNo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);
        String object=getIntent().getStringExtra("product");
        product=new Gson().fromJson(object, new TypeToken<Product>() {}.getType());
        sharedPreferences = getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);

        String user = sharedPreferences.getString("user", "");

        try {
            productObject=new JSONObject(object);
            useerObject=new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initView();
    }

    private void initView() {
        productImageLink=findViewById(R.id.productImageLink);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        productRate=findViewById(R.id.productRate);
        productType=findViewById(R.id.productType);
        productDeliverCharge=findViewById(R.id.productDeliverCharge);
        location=findViewById(R.id.location);
        item_count_in_cart=findViewById(R.id.item_count_in_cart);
        add_to_cart_btn=findViewById(R.id.add_to_cart_btn);

        Glide.with(this).load(product.getProductImageLink()).into(productImageLink);

        productName.setText(product.getProductName());
        productDescription.setText(product.getProductDescription());
        productRate.setText(product.getProductRate());
        productType.setText(product.getProductType().toString());
        productDeliverCharge.setText(product.getProductDeliverCharge());
        location.setText(product.getLocation().toString());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        loadCartCount();

        add_to_cart_btn.setOnClickListener(view -> {
            showAddToCartDailog(product);
        });
    }

    private void loadCartCount() {
        progressDialog.show();
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(ProductDetailActivity.this)
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                progressDialog.dismiss();
                item_count_in_cart.setText(tasks.size()+" Items");
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.add_to_cart_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView imageView = v.findViewById(R.id.img);
        TextView count_negative,count_positive;
        TextView  count,item_name,item_rate;
        AppCompatButton add_to_cart_btn;

        count_negative = v.findViewById(R.id.count_negative);
        count = v.findViewById(R.id.count);
        count_positive = v.findViewById(R.id.count_positive);
        item_name = v.findViewById(R.id.item_name);
        item_rate = v.findViewById(R.id.item_rate);
        add_to_cart_btn = v.findViewById(R.id.add_to_cart_btn);

        item_name.setText(product.getProductName());
        item_rate.setText(product.getProductRate());
        Glide.with(this).load(product.getProductImageLink()).into(imageView);

        count_negative.setOnClickListener(view1 -> {

            double r= Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(count.getText().toString());

            if (c>1){
                int i=c-1;
                count.setText(String.valueOf(i));
                item_rate.setText(String.valueOf((c-1)*r));
            }
        });
        count_positive.setOnClickListener(view1 -> {

            double r= Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(count.getText().toString());
            int i=c+1;

            count.setText(String.valueOf(i));
            item_rate.setText(String.valueOf((c+1)*r));

        });
        add_to_cart_btn.setOnClickListener(view1 -> {
            Task task=new Task(product.getProductId(), product.getProductName(), product.getProductDescription(),
                    product.getProductRate(), product.getProductDeliverCharge(), product.getProductImageLink(),
                    product.getCreatedTime(), product.getProductType(), product.getCategory(), product.getLocation(),
                    Integer.parseInt(count.getText().toString()));

            saveTask(task);

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void saveTask(Task task) {
        progressDialog.show();
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //adding to database
                DatabaseClient.getInstance(ProductDetailActivity.this).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                loadCartCount();
                Toast.makeText(ProductDetailActivity.this, "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

}