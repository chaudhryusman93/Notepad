package com.example.notepad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{



    ArrayList<FirebaseModel> firebaseModel;

    public NotesAdapter(ArrayList<FirebaseModel> firebaseModel) {
        this.firebaseModel = firebaseModel;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,null);
        return new NoteViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {


        FirebaseFirestore firebaseFirestore;
        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

//        FirebaseModel firebaseModel1 = firebaseModel.get(position);
        holder.noteTitle.setText(firebaseModel.get(position).getTitle());
        holder.noteContent.setText(firebaseModel.get(position).getContent());


        holder.menuPopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popupMenu.setGravity(Gravity.END);
                }
                CollectionReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("my notes");
//
                popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        documentReference.whereEqualTo("content",firebaseModel.get(position).getContent()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                        String documentid=documentSnapshot.getId();
                                        Intent intent = new Intent(view.getContext(),editnoteActivity.class);
                                        intent.putExtra("title",firebaseModel.get(position).getTitle());
                                        intent.putExtra("content",firebaseModel.get(position).getContent());
                                        intent.putExtra("documentid",documentid);
                                        view.getContext().startActivity(intent);
                                    }
                                }
                            }
                        });



                        return false;
                    }
                });

                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        CollectionReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("my notes");
//                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(view.getContext(), "This note is Deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(view.getContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    documentReference.whereEqualTo("content",firebaseModel.get(position).getContent()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                    String documentid=documentSnapshot.getId();
                                    documentReference.document(documentid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "This note is Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(view.getContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });




                        return false;
                    }
                });
                popupMenu.show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return firebaseModel.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteContent;
        LinearLayout note;
        ImageView menuPopBtn;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
//            note = itemView.findViewById(R.id.note);
            menuPopBtn = itemView.findViewById(R.id.menuPopBtn);

        }
    }
}
