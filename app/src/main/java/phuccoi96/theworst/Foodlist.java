package phuccoi96.theworst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import phuccoi96.theworst.Interface.ItemClickListener;
import phuccoi96.theworst.Model.Food;
import phuccoi96.theworst.ViewHolder.FoodViewHolder;

public class Foodlist extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String foodId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        //firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food_In_Detail");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        //get Intent here
        if(getIntent()!=null)
            foodId =getIntent().getStringExtra("Food");
        if(!foodId.isEmpty()&&foodId != null)
        {
            loadListFood(foodId);
        }
    }

    private void loadListFood(String foodId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(foodId)) //giong nh select * from Food_In_Detail where MenuID=MenuID
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {


                      //  Toast.makeText(Foodlist.this, ""+local.getName(), Toast.LENGTH_SHORT).show();

                        Intent foodList = new Intent(Foodlist.this, FoodDetail.class);
                        //Food o day la key cua food_in_detail
                        foodList.putExtra("FoodId",adapter.getRef(position).getKey());

                        startActivity(foodList);




                    }
                });
            }
        };
        //set Adapter
        recyclerView.setAdapter(adapter);
    }
}
