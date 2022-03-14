package com.softkali.buddhasuddha.checkout.fragment;

import static com.softkali.buddhasuddha.checkout.activity.CartActivity.address;
import static com.softkali.buddhasuddha.checkout.activity.CartActivity.totalAmount;
import static com.softkali.buddhasuddha.checkout.activity.CartActivity.totalItem;
import static com.softkali.buddhasuddha.checkout.activity.CartActivity.taskForOrder;
import static com.softkali.buddhasuddha.utils.Constant.addOrder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.adapter.PaymentListAdapter;
import com.softkali.buddhasuddha.dashboard.DashboardActivity;
import com.softkali.buddhasuddha.database.DatabaseClient;
import com.softkali.buddhasuddha.database.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class CartPaymentFragment extends Fragment {

    TextView total;
    RecyclerView payment_recyclerView;
    Button cash_on_delivery;
    private static List<Task> taskList = new ArrayList<>();
    private static ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String userId, name;
    String location;


    public CartPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_payment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        try {
            JSONObject jsonObject = new JSONObject(sharedpreferences.getString("authUser", ""));
            userId = jsonObject.getString("userId");
            name = jsonObject.getString("name");
            location=jsonObject.getString("location");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        total = view.findViewById(R.id.total);
        cash_on_delivery = view.findViewById(R.id.cash_on_delivery);
        payment_recyclerView = view.findViewById(R.id.payment_recyclerView);
        payment_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");
        total.setText(String.valueOf(totalAmount));

        getTasks();
        cash_on_delivery.setOnClickListener(v -> {
            progressDialog.show();
            String url = addOrder;


            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("userId", userId);
                jsonObject.put("name", name);
                jsonObject.put("phoneNumber", address.getUserPhoneNumber());
                jsonObject.put("productDeliverAddress", address.getProductDeliverAddress());
                jsonObject.put("location", location);
                jsonObject.put("orderData", new Gson().toJson(taskList));
                jsonObject.put("totalRate", totalAmount);


            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Log.e("abcd", e.toString());
                Toast.makeText(getContext(), "something went worng error= " + e.toString(), Toast.LENGTH_SHORT).show();

            }

            Log.e("abcd", jsonObject.toString());
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            final String mRequestBody = jsonObject.toString();
            Log.e("abcd", mRequestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Log.e("abcd", response);
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        if (status) {
                            Toast.makeText(getContext(), responseObject.getString("messag"), Toast.LENGTH_SHORT).show();
                            deleteAllTask(getContext());

                        } else {
                            Toast.makeText(getContext(), responseObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "something went worng", Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("abcd", error.toString());
                    Toast.makeText(getContext(), "something went worng error= " + error.toString(), Toast.LENGTH_SHORT).show();

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


        });
    }

    private static void deleteAllTask(Context context) {
        progressDialog.show();
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .taskDao()
                        .deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private void getTasks() {
        progressDialog.show();
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                progressDialog.dismiss();
                totalAmount = 0;
                totalItem = 0;
                taskForOrder = tasks;
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    double rate = Double.parseDouble(task.getProductRate());
                    double quntity = task.getCount();

                    double amount = rate * quntity;
                    totalAmount = totalAmount + amount;
                    totalItem = totalItem + quntity;
                    Log.e("abcd", String.valueOf(tasks.get(i)));

                }
                taskList = tasks;
                PaymentListAdapter paymentListAdapter = new PaymentListAdapter(getContext(), taskList);
                payment_recyclerView.setAdapter(paymentListAdapter);
                paymentListAdapter.notifyDataSetChanged();


                total.setText(String.valueOf(totalAmount));


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
}