package israel.shoppingfun.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import israel.shoppingfun.R;
import israel.shoppingfun.dialogs.AddListDialogFragment;
import israel.shoppingfun.dialogs.ShareFragment;
import israel.shoppingfun.models.UserList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {

    @BindView(R.id.fabAddUserList)
    FloatingActionButton fabAddUserList;
    @BindView(R.id.rvUserLists)
    RecyclerView rvUserLists;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("Tomer", "Null user");
            return view;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserLists").child(currentUser.getUid());//TODO: Handle nulls

        rvUserLists.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUserLists.setAdapter(new UserListAdapter(ref, this));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabAddUserList)
    public void onFabAddUserClicked() {
        AddListDialogFragment dialog = new AddListDialogFragment();
        dialog.show(getChildFragmentManager(), "AddUserListDialog");
    }

    //2)FirebaseRecyclerAdapter
    public static class UserListAdapter extends FirebaseRecyclerAdapter<UserList, UserListAdapter.UserListViewHolder> {
        Fragment fragment;

        public UserListAdapter(Query query, Fragment fragment) {
            super(UserList.class, R.layout.user_list_item, UserListViewHolder.class, query);
            this.fragment = fragment;
        }


        @Override
        public UserListViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {

            UserListViewHolder vh = super.onCreateViewHolder(parent, ViewType);
            vh.userListsFragment = fragment;
            return vh;
        }

        @Override
        protected void populateViewHolder(UserListViewHolder viewHolder, UserList model, int position) {
            viewHolder.tvListName.setText(model.getName());
            Glide.with(fragment.getContext()).load(model.getOwnerImage()).into(viewHolder.ivProfile);
            viewHolder.model = model;
            viewHolder.userListsFragment = fragment;
        }

        //1)ViewHolder
        public static class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            //Properties:
            ImageView ivProfile;
            TextView tvListName;
            FloatingActionButton fabShare;
            Fragment userListsFragment;
            UserList model;

            public UserListViewHolder(View itemView) {
                super(itemView);
                ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
                tvListName = (TextView) itemView.findViewById(R.id.tvListName);
                fabShare = (FloatingActionButton) itemView.findViewById(R.id.fabShare);
                fabShare.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    ShareFragment dialog = new ShareFragment();
                    dialog.show(userListsFragment.getChildFragmentManager(), "shareDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
