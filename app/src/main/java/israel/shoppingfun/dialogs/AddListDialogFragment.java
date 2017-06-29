package israel.shoppingfun.dialogs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import israel.shoppingfun.R;
import israel.shoppingfun.models.User;
import israel.shoppingfun.models.UserList;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddListDialogFragment extends DialogFragment {

    @BindView(R.id.etListName)
    EditText etListName;
    @BindView(R.id.btnAddUserList)
    FancyButton btnAddUserList;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_list_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnAddUserList)
    public void onAddListClicked() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser == null) {
            Log.e("Tomer", "No User");
            return;
        }

        if (etListName.getText().length() == 0) {
            etListName.setError("Empty list name...");
            return;
        }

        //ref to a new row in the userList Table.
        DatabaseReference newRowRef = FirebaseDatabase.
                getInstance().
                getReference("UserLists").
                child(currentUser.getUid()).
                push();

        User u = new User(currentUser);
        //init the model:
        UserList userList = new UserList(
                etListName.getText().toString(), //the list Name (Taken from the edit text)
                currentUser.getUid(), //tomerID
                u.getProfileImage(), //got it from the user (if the image is null, the image is set to default)
                newRowRef.getKey() //List ID (The id of the newly created row.
        );

        newRowRef.setValue(userList).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });


    }
}

