package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class notesActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    NotesAdapter notesAdapter;
    ArrayList<FirebaseModel> dataList = new ArrayList<>();


//    FirestoreRecyclerAdapter<FirebaseModel,NotesViewHolder> notesAdapter;



    FloatingActionButton btnFab;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        btnFab = findViewById(R.id.btnFab);
        recyclerView = findViewById(R.id.recyclerView);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        CollectionReference fireStore = FirebaseFirestore.getInstance().collection("notes").document(firebaseUser.getUid()).collection("my notes");
        fireStore.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                for (DocumentSnapshot snapshot: Objects.requireNonNull(value).getDocuments()){
                    String title = (String) snapshot.get("title");
                    String content = (String) snapshot.get("content");
                    dataList.add(new FirebaseModel(title,content));

                    notesAdapter = new NotesAdapter(dataList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(notesAdapter);
                    notesAdapter.notifyDataSetChanged();

                }
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("All Notes");

        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(notesActivity.this,CreatenoteActivity.class));

            }
        });

 //       Query query =firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("my notes").orderBy("title",Query.Direction.ASCENDING);

//        FirestoreRecyclerOptions<FirebaseModel> AllNotes = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();
//
//        notesAdapter= new FirestoreRecyclerAdapter<FirebaseModel, NotesViewHolder>(AllNotes) {
//            @Override
//            protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull FirebaseModel model) {
//
//                holder.noteTitle.setText(model.getTitle());
//                holder.noteContent.setText(model.getContent());
//
//            }
//
//            @NonNull
//            @Override
//            public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
//
//                return new NotesViewHolder(view);
//            }
//        };






    }

//    public class NotesViewHolder extends RecyclerView.ViewHolder {
//
//        TextView noteTitle,noteContent;
//        LinearLayout note;
//
//        public NotesViewHolder(@NonNull View itemView) {
//            super(itemView);
//            noteTitle = itemView.findViewById(R.id.noteTitle);
//            noteContent = itemView.findViewById(R.id.noteContent);
//            note = itemView.findViewById(R.id.note);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesActivity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}