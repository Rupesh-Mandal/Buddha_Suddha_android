package com.softkali.buddhasuddha.checkout.fragment;


import static com.softkali.buddhasuddha.checkout.activity.CartActivity.totalAmount;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.softkali.buddhasuddha.R;
import com.softkali.buddhasuddha.checkout.adapter.CartAdapter;
import com.softkali.buddhasuddha.database.DatabaseClient;
import com.softkali.buddhasuddha.database.Task;
import com.softkali.buddhasuddha.database.TaskOpration;

import java.util.ArrayList;
import java.util.List;


public class CartListFragment extends Fragment implements TaskOpration {

    View view;
    RecyclerView cart_list_recyclr;
    private static List<Task> taskList=new ArrayList<>();
    CartAdapter cartAdapter;
    TextView total,check_out_btn;
    NavController navigate;
    private  static ProgressDialog progressDialog;

    public CartListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view=v;
        initView();
    }

    private void initView() {
        cart_list_recyclr=view.findViewById(R.id.cart_list_recyclr);
        total=view.findViewById(R.id.total);
        check_out_btn=view.findViewById(R.id.check_out_btn);
        cart_list_recyclr.setLayoutManager(new LinearLayoutManager(getContext()));
        navigate= Navigation.findNavController(view);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        getTasks(false);
        check_out_btn.setOnClickListener(v -> {
            if (totalAmount>0){
                navigate.navigate(R.id.cartAddressFragment);
            }else {
                Toast.makeText(getContext(), "Please Add Some Item in Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTasks(boolean isUpdate) {
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
                totalAmount=0;
                if (isUpdate){
                    for (int i=0;i<tasks.size();i++){
                        Task task=tasks.get(i);
                        double rate= Double.parseDouble(task.getProductRate());
                        double quntity=task.getCount();

                        double amount=rate*quntity;
                        totalAmount=totalAmount+amount;

                        Log.e("abcd", String.valueOf(tasks.get(i)));

                    }
                }else {
                    for (int i=0;i<tasks.size();i++){
                        Task task=tasks.get(i);
                        double rate= Double.parseDouble(task.getProductRate());
                        double quntity=task.getCount();

                        double amount=rate*quntity;
                        totalAmount=totalAmount+amount;

                        Log.e("abcd", String.valueOf(tasks.get(i)));

                    }
                    taskList=tasks;
                    cartAdapter=new CartAdapter(getContext(),taskList,CartListFragment.this);
                    cart_list_recyclr.setAdapter(cartAdapter);
                    cartAdapter.notifyDataSetChanged();
                }


                total.setText(String.valueOf(totalAmount));


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void updateTask(final Task task) {
        progressDialog.show();
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                getTasks(true);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(final Task task) {
        progressDialog.show();
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                getTasks(false);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }


    @Override
    public void upDate(Task task) {
        updateTask(task);
        Toast.makeText(getContext(), "upDate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Delet(Task task) {
        deleteTask(task);
    }

}