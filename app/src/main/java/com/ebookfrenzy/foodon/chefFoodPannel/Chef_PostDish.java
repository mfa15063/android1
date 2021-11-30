package com.ebookfrenzy.foodon.chefFoodPannel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ebookfrenzy.foodon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Chef_PostDish extends AppCompatActivity {
    
    ImageButton imageButton;
    Button post_dish;
    Spinner Dishes;
    TextInputLayout desc, qty, pri;
    String description, quantity, price, dishes;
    Uri imageuri;
    private Uri mcropimageuri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataaa;
    FirebaseAuth FAuth;
    StorageReference ref;
    String ChefId;
    String RandomUId;
    String State, City, Sub;
    private Object CropImage;
    Uri uri;
    private static final int PERMISSION_REQUEST_READ = 900, PERMISSION_REQUEST_CODE = 101, REQUEST_IMAGE_CAPTURE = 100, REQUEST_PERMISSION_SETTING = 50, CHOSE_FILE = 900;
    private Bitmap imageBitmap;
    private String imageString;
    private Uri picUri;
    private RelativeLayout select_option;
    private Button chose_file, use_camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef__post_dish);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Dishes = (Spinner) findViewById(R.id.dishes);
        desc = (TextInputLayout) findViewById(R.id.description);
        qty = (TextInputLayout) findViewById(R.id.quantity);
        pri = (TextInputLayout) findViewById(R.id.price);
        post_dish = (Button) findViewById(R.id.post);
        FAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("FoodDetails");
        imageButton = (ImageButton) findViewById(R.id.imageupload);
        select_option = findViewById(R.id.select_option);
        select_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_option.setVisibility(View.GONE);
            }
        });
        chose_file = findViewById(R.id.chose_file);
        use_camera = findViewById(R.id.use_camera);
        chose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    );
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    intent.setType("image/*");
                    try {
                        startActivityForResult(intent, CHOSE_FILE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                } else {
                    // You can directly ask for the permission.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(Chef_PostDish.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                PERMISSION_REQUEST_READ);
                    }
                }
            }
        });
        use_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    try {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                } else {
                    // You can directly ask for the permission.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(Chef_PostDish.this, new String[] { Manifest.permission.CAMERA },
                                PERMISSION_REQUEST_CODE);
                    }
                }
            }
        });

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataaa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Chef chef = snapshot.getValue(Chef.class);
                    State = chef.getState();
                    City = chef.getArea();
                    post_dish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dishes = Dishes.getSelectedItem().toString().trim();
                            description = desc.getEditText().getText().toString().trim();
                            price = pri.getEditText().getText().toString().trim();
                            quantity = qty.getEditText().getText().toString().trim();

                            if (isValid()) {
                                uploadImage();
                            }

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

            Log.e("Errrrrr: ", e.getMessage());
        }
    }

    private void uploadImage() {
        if (imageuri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(Chef_PostDish.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            RandomUId = UUID.randomUUID().toString();
//            ref = storageReference.child(RandomUId);
            ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FoodDetails info = new FoodDetails(dishes, quantity, price, description, String.valueOf(uri), RandomUId, ChefId);
            firebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId)
                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    Toast.makeText(Chef_PostDish.this, "Dish posted successfully", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void onSelectImageClick(View view) {

        select_option.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                }  else {
                    Toast.makeText(getBaseContext(), "Allow Camera to take Picture", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                }
                break;
            case PERMISSION_REQUEST_READ:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOSE_FILE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                }  else {
                    Toast.makeText(getBaseContext(), "Allow Fies to select Picture", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOSE_FILE:
            case REQUEST_IMAGE_CAPTURE:
                imageuri = data.getData();
                if (imageuri != null)
                    imageButton.setImageURI(imageuri);
                else {
                    Bundle extras = data.getExtras();
                    imageBitmap = extras.getParcelable("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(Chef_PostDish.this.getContentResolver(), imageBitmap, "Title", null);
                    imageuri = Uri.parse(path);
                    imageButton.setImageURI(imageuri);
                }
                select_option.setVisibility(View.GONE);
                break;
        }
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValiDescription = false, isValidPrice = false, isvalidQuantity = false, isvalid = false;
        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");

        } else {

            desc.setError(null);
            isValiDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            qty.setErrorEnabled(true);
            qty.setError("Quantity is Required");
        } else {
            isvalidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Price is Required");
        } else {
            isValidPrice = true;
        }
        isvalid = (isValiDescription && isvalidQuantity && isValidPrice) ? true : false;

        return isvalid;
    }

}
