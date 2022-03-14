package com.softkali.admin.dashboard.fragment;



import static com.softkali.admin.dashboard.DashboardActivity.location;
import static com.softkali.admin.util.Constant.cancelOrderBySeller;
import static com.softkali.admin.util.Constant.delet_product;
import static com.softkali.admin.util.Constant.get_all_product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.admin.R;
import com.softkali.admin.dashboard.activity.AddProductActivity;
import com.softkali.admin.dashboard.activity.OrderDetailActivity;
import com.softkali.admin.dashboard.activity.UpdateActivity;
import com.softkali.admin.dashboard.adapter.OrderAdapter;
import com.softkali.admin.dashboard.adapter.ProductAdapter;
import com.softkali.admin.dashboard.model.Product;
import com.softkali.admin.util.OrderAction;
import com.softkali.admin.util.ProductOnclick;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProductFragment extends Fragment {

    private static View view;
    private static RecyclerView all_product_list;
    ImageView add_product;
    private static ProgressDialog progressDialog;

    private static ArrayList<Product> productArrayList = new ArrayList<>();

    public static Context context;
    public AllProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;
        context=getContext();
        initView();
    }

    private void initView() {
        all_product_list = view.findViewById(R.id.all_product_list);
        add_product = view.findViewById(R.id.add_product);

        all_product_list.setLayoutManager(new GridLayoutManager(getContext(),1));

        add_product.setOnClickListener(view1 -> {
            Intent intent=new Intent(getContext(), AddProductActivity.class);
            getActivity().startActivityForResult(intent,2);
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        loadProduct(getActivity());
    }

    public static void loadProduct(Activity activity) {
        RequestQueue queue = Volley.newRequestQueue(activity);

        progressDialog.show();
        String URL = get_all_product;

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("abcd", response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            Log.e("abcd",jsonArray.toString());
                            productArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Product>>() {}.getType());
                            setProdduct(activity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd", error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

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

    private static void setProdduct(Activity activity) {
        ProductAdapter productAdapter=new ProductAdapter(activity, productArrayList, new ProductOnclick() {
            @Override
            public void onEdit(Product product) {
                Intent intent=new Intent(activity, UpdateActivity.class);
                intent.putExtra("product",new Gson().toJson(product));
                activity.startActivityForResult(intent,2);

            }

            @Override
            public void onDelet(Product product) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.bottom_shee_dailog_theam);
                View v = LayoutInflater.from(activity).
                        inflate(R.layout.delete_bottomsheet, (ConstraintLayout) view.findViewById(R.id.bottom_sheet_layout));
                bottomSheetDialog.setContentView(v);

                ImageView productImageLink = v.findViewById(R.id.productImageLink);
                TextView productName = v.findViewById(R.id.productName);
                TextView productDescription = v.findViewById(R.id.productDescription);
                TextView productRate = v.findViewById(R.id.productRate);

                Button cancelBtn=v.findViewById(R.id.cancelBtn);
                Button deletBtn=v.findViewById(R.id.deletBtn);

                Glide.with(context).load(product.getProductImageLink()).into(productImageLink);

                productName.setText(product.getProductName());
                productDescription.setText(product.getProductDescription());
                productRate.setText(product.getProductRate());

                cancelBtn.setOnClickListener(view1 -> {
                    bottomSheetDialog.dismiss();
                });
                deletBtn.setOnClickListener(view1 -> {
                    deletProduct(new Gson().toJson(product),activity);
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.show();

            }
        });
        all_product_list.setAdapter(productAdapter);
    }

    private static void deletProduct(String object,Activity activity) {
        progressDialog.show();
        final String mRequestBody = object;
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String URL=delet_product;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        Toast.makeText(activity, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(activity, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }
                    loadProduct(activity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd",error.toString());
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

}