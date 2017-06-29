package israel.shoppingfun.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import israel.shoppingfun.R;
import israel.shoppingfun.models.User;
import israel.shoppingfun.models.UserList;


public class ShareFragment extends BottomSheetDialogFragment   {

    //find the recycler by id
    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;

    Unbinder unbinder;
    UserAdapter adapter;

    UserList model;

    public static ShareFragment newInstance(UserList model) {

        Bundle args = new Bundle();
        args.putSerializable("model", model);
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        unbinder = ButterKnife.bind(this, view);
        model = getArguments().getParcelable("model");
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseDatabase.getInstance().getReference("Users");
        adapter = new UserAdapter(getContext(), query, model);
        rvUsers.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserViewHolder> {
        private Context context;
        private UserList userlist;

        //Constructor that takes the Query and Context.
        public UserAdapter(Context context, Query query, UserList userlist) {
            super(User.class, R.layout.share_item, UserViewHolder.class, query);
            this.context = context;
            this.userlist = userlist;
        }

        @Override
        protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
            //bind the user name from the model to the TextView in VH:
            viewHolder.tvUserName.setText(model.getDisplayName());

            //bind the user image from the model to the ImageView in VH:
            Glide.with(context).load(model.getProfileImage()).into(viewHolder.ivProfile);
            viewHolder.user = model;
            viewHolder.userlist = userlist;
        }

        //ViewHolder
        public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CircleImageView ivProfile;
            TextView tvUserName;
            UserList userlist;
            User user;

            public UserViewHolder(View itemView) {
                super(itemView);
                ivProfile = (CircleImageView) itemView.findViewById(R.id.ivProfile);
                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserLists").
                        child(user.getUid()).
                        child(userlist.getListID());
                ref.setValue(userlist);
            }
        }
    }

}
