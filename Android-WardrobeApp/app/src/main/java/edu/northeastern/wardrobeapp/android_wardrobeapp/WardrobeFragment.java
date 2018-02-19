package edu.northeastern.wardrobeapp.android_wardrobeapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import edu.northeastern.wardrobeapp.android_wardrobeapp.utils.ImageAdapter;
import edu.northeastern.wardrobeapp.android_wardrobeapp.utils.ImageItem;


public class WardrobeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    ArrayList<ImageItem> imageItems;
    ImageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wardrobe, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        // Recycler view setup
        recyclerView = (RecyclerView)view.findViewById(R.id.wardrobe_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        // Images for User
        imageItems = new ArrayList<>();
        adapter = new ImageAdapter(view.getContext(), imageItems);
        recyclerView.setAdapter(adapter);
        // This will load images and add it to the Adapter
        getImagesForUser();
    }

    private void getImagesForUser() {
        DataAccess DA = new DataAccess();
        String uid = ((BaseActivity)getActivity()).getCurrentUserId();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String url = snapshot.child("url").getValue().toString();
                    Log.d("DA", "Found Clothing with URL " + url);

                    ImageItem image = new ImageItem(url);
                    image.setTitle(id);
                    adapter.addItem(image);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        DA.getWardrobe(uid, listener);
    }

}
