package com.example.nailzip;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nailzip.model.NailshopData;
import com.example.nailzip.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private String TAG = "PostAdapter";
    public Context mContext;
    public List<Post> mPost = new ArrayList<>();

    private FirebaseUser firebaseUser;
    private String saveShopname;

    //스크랩
    private String scrap_design_id;
    private String scrap_post_shop_name;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_detail, parent, false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);

        Glide.with(mContext).load(post.getPostimage()).into(holder.img_design);

        if(post.getDescription().equals("")){
            holder.txt_description.setVisibility(View.GONE);
        }
        else{
            holder.txt_description.setVisibility(View.VISIBLE);
            holder.txt_description.setText(post.getDescription());
        }

        Log.d(TAG, "게시글 작성자 정보 : " + post.getPublisher());
        shopInfo(holder.img_profile, holder.txt_shopname, holder.txt_shopname_inPost, post.getPublisher());

        isScrap(post.getPostid(), holder.btn_post_scrap);

        // 스크랩 버튼
        FirebaseDatabase.getInstance().getReference().child("ScrapDesign").child(firebaseUser.getUid()).child("scraping").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
//                    saveFollowingUid.add(item.getValue().toString());
                    String saveScrapPostUid = item.getKey();
                    Log.d(TAG, "포스트 아이디 : " + saveScrapPostUid);

                    if (post.getPostid().equals(saveScrapPostUid)){
                        holder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_fill);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "error");

            }
        });

        holder.btn_post_scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_post_scrap.getText().toString().equals("scrap")){
                    firebaseDatabase.getReference().child("ScrapDesign").child(firebaseAuth.getUid())
                            .child("scraping").child(post.getPostid()).setValue(post.getPublisher());
//                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
//                            .child("followers").child(firebaseAuth.getUid()).setValue(true);
                    holder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_fill);
                }
                else{
                    firebaseDatabase.getReference().child("ScrapDesign").child(firebaseAuth.getUid())
                            .child("scraping").child(post.getPostid()).removeValue();
//                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
//                            .child("followers").child(firebaseAuth.getUid()).removeValue();
                    holder.btn_post_scrap.setBackgroundResource(R.drawable.bookmark_border);
                }
            }
        });

        // TODO : 예약버튼 추가
        holder.btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chattingroomActivity = new Intent(v.getContext(), ChattingroomActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chattingroomActivity.putExtra("chatUid", post.getPublisher());
                Log.d(TAG, "chatUid2 : " + post.getPublisher());
                v.getContext().startActivity(chattingroomActivity);
            }
        });

    }

    private void isScrap (final String postid, final Button button){


        DatabaseReference reference = firebaseDatabase.getReference()
                .child("ScrapDesign").child(firebaseAuth.getCurrentUser().getUid()).child("scraping");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists()){
                    button.setText("scraping");
                }
                else{
                    button.setText("scrap");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img_profile, img_design;
        public Button btn_post_scrap, btn_reservation;
        public TextView txt_shopname, txt_shopname_inPost, txt_description;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            img_profile = itemView.findViewById(R.id.post_img_profile);
            img_design = itemView.findViewById(R.id.img_design);
            btn_post_scrap = itemView.findViewById(R.id.btn_post_scrap);
            btn_reservation = itemView.findViewById(R.id.btn_reservation);
            txt_shopname = itemView.findViewById(R.id.txt_shopname);
            txt_shopname_inPost = itemView.findViewById(R.id.txt_shopname_inPost);
            txt_description = itemView.findViewById(R.id.txt_description);

        }
    }

    private void shopInfo(ImageView img_profile, TextView shopname, TextView shopname_inPost, String shopId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users")
                        .document(shopId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        NailshopData nailshopData = value.toObject(NailshopData.class);
//                Glide.with(mContext).load(nailshopData.getImg_shop()).into(img_profile);
                        shopname.setText(nailshopData.getShopname());
                        shopname_inPost.setText(nailshopData.getShopname());
                    }
                });

//        reference.child("ShopList").child(shopId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                NailshopData nailshopData = snapshot.getValue(NailshopData.class);
////                Glide.with(mContext).load(nailshopData.getImg_shop()).into(img_profile);
//                shopname.setText(nailshopData.getShopname());
//                shopname_inPost.setText(nailshopData.getShopname());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}
