package com.example.ecommerce.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class UsersOrdersFragment extends Fragment {

    private RecyclerView recycler_view_orders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private DatabaseReference ordersDatabase;

    public UsersOrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_orders, container, false);

        recycler_view_orders = view.findViewById(R.id.recycler_view_orders);
        recycler_view_orders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderList);
        recycler_view_orders.setAdapter(orderAdapter);


        ordersDatabase = FirebaseDatabase.getInstance().getReference("orders");


        String userId = FirebaseAuth.getInstance().getUid();

        loadUserOrders(userId);

        return view;
    }

    private void loadUserOrders(String userId) {
        ordersDatabase.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear(); // Clear existing list to avoid duplicates

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    orderList.add(order);
                }

                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("UsersOrdersFragment", "Error loading orders", databaseError.toException());
            }
        });
    }
}
