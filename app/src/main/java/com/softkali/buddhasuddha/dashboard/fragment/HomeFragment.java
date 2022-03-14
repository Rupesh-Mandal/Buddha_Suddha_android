package com.softkali.buddhasuddha.dashboard.fragment;

import static com.softkali.buddhasuddha.utils.Constant.getAllCategory;
import static com.softkali.buddhasuddha.utils.Constant.get_all_product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.dashboard.activity.CategoryActivity;
import com.softkali.buddhasuddha.dashboard.activity.ProductDetailActivity;
import com.softkali.buddhasuddha.dashboard.activity.SearchActivity;
import com.softkali.buddhasuddha.dashboard.adapter.CategoryAdapter;
import com.softkali.buddhasuddha.dashboard.adapter.ProductAdapter;
import com.softkali.buddhasuddha.dashboard.model.CategoriesModel;
import com.softkali.buddhasuddha.dashboard.model.Product;
import com.softkali.buddhasuddha.database.DatabaseClient;
import com.softkali.buddhasuddha.database.Task;
import com.softkali.buddhasuddha.utils.CategoryOnClick;
import com.softkali.buddhasuddha.utils.UserProductOnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
    View view;
    RecyclerView categories_recyclerView, all_product_recyclerView;
    SearchView search_view;
    SharedPreferences sharedpreferences;
    private static ProgressDialog progressDialog;

    String location;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        try {
            String obj=sharedpreferences.getString("authUser","");
            JSONObject userObject=new JSONObject(obj);
            location=userObject.getString("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initView();
    }

    private void initView() {
        categories_recyclerView = view.findViewById(R.id.categories_recyclerView);
        all_product_recyclerView = view.findViewById(R.id.all_product_recyclerView);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        search_view = view.findViewById(R.id.search_view);
        categories_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        all_product_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("q", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        loadCategory();
        loadAllProduct();
    }

    private void loadCategory() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getAllCategory;
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        ArrayList<CategoriesModel> categoriesModelArrayList=new Gson().fromJson(response, new TypeToken<List<CategoriesModel>>() {}.getType());

                        CategoryAdapter categoryAdapter=new CategoryAdapter(getContext(), categoriesModelArrayList, new CategoryOnClick() {
                            @Override
                            public void onClick(CategoriesModel categoriesModel) {
                                Intent intent=new Intent(getContext(), CategoryActivity.class);
                                intent.putExtra("productCategory",categoriesModel.getName());
                                startActivity(intent);
                            }
                        });
                        categories_recyclerView.setAdapter(categoryAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(sr);
    }

    private void loadAllProduct() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = get_all_product;
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        ArrayList<Product> productArrayList=new Gson().fromJson(response, new TypeToken<List<Product>>() {}.getType());
                        ProductAdapter productAdapter=new ProductAdapter(getContext(), productArrayList, new UserProductOnClick() {
                            @Override
                            public void addToCartOnclick(Product product) {
                                showAddToCartDailog(product);

                            }

                            @Override
                            public void onclick(Product product) {
                                Intent intent=new Intent(getContext(), ProductDetailActivity.class);
                                intent.putExtra("product",new Gson().toJson(product));
                                startActivity(intent);
                            }
                        });
                        all_product_recyclerView.setAdapter(productAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location", location);
                return params;
            }
        };
        queue.add(sr);
    }
    private void showAddToCartDailog(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(getContext()).
                inflate(R.layout.add_to_cart_bottomsheet, (ConstraintLayout) view.findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView imageView = v.findViewById(R.id.img);
        TextView count_negative,count_positive;
        TextView  count ,item_name,item_rate;
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
                DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

}