package israel.shoppingfun.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import israel.shoppingfun.R;
import israel.shoppingfun.models.Product;
import israel.shoppingfun.models.UserList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListProductsFragment extends Fragment {
    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.fabAddProduct)
    FloatingActionButton fabAddProduct;
    @BindView(R.id.etProductName)
    EditText etProductName;

    Unbinder unbinder;
    //properties:
    private UserList userList;

    public static ListProductsFragment newInstance(UserList userList) {
        Bundle args = new Bundle();
        args.putParcelable("list", userList);
        ListProductsFragment fragment = new ListProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userList = getArguments().getParcelable("list");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);
        unbinder = ButterKnife.bind(this, view);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ListProducts").
                child(userList.getListID());

        rvProducts.setAdapter(new ProductAdapter(ref));
        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @OnTextChanged(R.id.etProductName)
    public void textChanged(CharSequence c){

    }

    @OnEditorAction(R.id.etProductName)
    public boolean doneClicked(){
        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabAddProduct)
    public void onAddProductClicked() {
        //Current user?
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        //get the product name from the edit text:
        String productName = etProductName.getText().toString();
        Product product = new Product(productName, user.getDisplayName(), false);

        //Save the product to the list:
        //ref to the table:
        FirebaseDatabase.getInstance().getReference("ListProducts").
                child(userList.getListID()).
                push().
                setValue(product).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    etProductName.setText(null);
                } else {
                    Exception e = task.getException();
                    if (e != null)
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.ProductViewHolder>{
        public ProductAdapter(Query query) {
            super(Product.class, R.layout.product_item, ProductViewHolder.class, query);
        }

        @Override
        protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
            viewHolder.tvProductName.setText(model.getName());
            if (model.isPurchased()){
                viewHolder.tvProductName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        //1)ViewHolder
        public static class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName;

            public ProductViewHolder(View itemView) {
                super(itemView);
                tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            }
        }
    }
    //2) FireBaseRecyclerAdapter
}