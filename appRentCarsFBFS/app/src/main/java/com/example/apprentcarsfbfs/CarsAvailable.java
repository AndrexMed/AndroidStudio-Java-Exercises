    package com.example.apprentcarsfbfs;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.os.Bundle;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.firebase.firestore.QuerySnapshot;

    import java.util.ArrayList;
    import java.util.List;

    public class CarsAvailable extends AppCompatActivity {

        RecyclerView recycler;
        ArrayList<Cars> carsList;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cars_available);

            recycler = findViewById(R.id.recycler);

            carsList = new ArrayList<>();
            recycler.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
            recycler.setHasFixedSize(true);

                LoadCarsAvailable();
        }

        private void LoadCarsAvailable(){
        db.collection("Cars").whereEqualTo("State", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){

                        Cars car = new Cars();
                        car.setPlateNumber(document.getString("PlateNumber"));
                        car.setBrand(document.getString("Brand"));
                        //car.setDailyValue(document.getDouble("DailyValue"));
                        //car.setState(document.getBoolean("State"));

                        carsList.add(car);
                    }

                    RentAdapter rentAdapter = new RentAdapter(carsList);
                    recycler.setAdapter(rentAdapter);
                }else{
                    Toast.makeText(CarsAvailable.this, "Internal Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }


    }