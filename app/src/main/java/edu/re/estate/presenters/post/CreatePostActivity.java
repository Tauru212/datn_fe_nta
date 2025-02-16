package edu.re.estate.presenters.post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import edu.re.estate.R;
import edu.re.estate.components.ExCallback;
import edu.re.estate.components.NConstants;
import edu.re.estate.data.entity.City;
import edu.re.estate.data.entity.District;
import edu.re.estate.data.entity.Ward;
import edu.re.estate.data.models.Address;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Path;
import edu.re.estate.data.request.PostRequest;
import edu.re.estate.data.request.PutImageRequest;
import edu.re.estate.data.source.network.AuthService;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.ActivityCreatePostBinding;
import edu.re.estate.utils.FileUtils;
import edu.re.estate.utils.SessionManager;
import edu.re.estate.widgets.SimpleTextWatcher;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatePostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    private ActivityCreatePostBinding binding;
    private AlbumAdapter albumAdapter;

    private final ArrayList<Uri> files = new ArrayList<>();
    private final Address address = new Address();
    private final ArrayList<String> publicLink = new ArrayList<>();

    private String acreage;
    private String price;
    private String nameContract;
    private String emailContract;
    private String phoneContract;
    private String titlePost;
    private String descriptionPost;

    private String postType = "lease";
    private String reStateType;
    private String legalDocuments;
    private String interior;
    private int postId;

    private final Stack<Uri> uriStack = new Stack<>();

    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        albumAdapter = new AlbumAdapter(files, uri -> {
            files.remove(uri);
            albumAdapter.setFiles(files);
        });

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(
                this,
                R.array.type_bds,
                android.R.layout.simple_spinner_item
        );
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(adapterType);
        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    reStateType = "house";
                } else if (i == 1) {
                    reStateType = "villa";
                } else if (i == 2) {
                    reStateType = "apartment";
                } else if (i == 3) {
                    reStateType = "homestay";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterLegalDocuments = ArrayAdapter.createFromResource(
                this,
                R.array.legal_documents,
                android.R.layout.simple_spinner_item
        );
        adapterLegalDocuments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLegalDocuments.setAdapter(adapterLegalDocuments);
        binding.spinnerLegalDocuments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    legalDocuments = null;
                } else if (i == 1) {
                    legalDocuments = "owner_book";
                } else if (i == 2) {
                    legalDocuments = "sales_contract";
                } else if (i == 3) {
                    legalDocuments = "waiting_for_registration";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterInterior = ArrayAdapter.createFromResource(
                this,
                R.array.interior,
                android.R.layout.simple_spinner_item
        );
        adapterInterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerInterior.setAdapter(adapterInterior);
        binding.spinnerInterior.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    interior = null;
                } else if (i == 1) {
                    interior = "full";
                } else if (i == 2) {
                    interior = "basic";
                } else if (i == 3) {
                    interior = "nothing";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setAdapter(albumAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.tvSell.setOnClickListener(v -> {
            postType = "sell";
            updateTotalPriceByTransitionType(binding.edtPrice.getText().toString().trim());
            binding.tvSell.setBackgroundResource(R.drawable.bg_select_type_post);
            binding.tvLease.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        });

        binding.tvLease.setOnClickListener(v -> {
            postType = "lease";
            updateTotalPriceByTransitionType(binding.edtPrice.getText().toString().trim());
            binding.tvLease.setBackgroundResource(R.drawable.bg_select_type_post);
            binding.tvSell.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        });
        binding.edtAcreage.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                binding.edtAcreage.setBackgroundResource(R.drawable.bg_input_address);
                binding.edtAcreage.removeTextChangedListener(this);
                try {
                    String data = convertToVietnameseCurrency(Long.parseLong(editable.toString().replace(".", "")));
                    binding.edtAcreage.setText(data);
                    binding.edtAcreage.setSelection(data.length());
                } catch (Exception e) {
                    Log.e("CheckAcreage", "Error: " + e.getMessage());
                }
                binding.edtAcreage.addTextChangedListener(this);
            }
        });
        binding.edtPrice.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
//                binding.edtPrice.setBackgroundResource(R.drawable.bg_input_address);
//                binding.edtPrice.removeTextChangedListener(this);
//                try {
//                    String data = convertToVietnameseCurrency(Long.parseLong(editable.toString().replace(".", "")));
//                    binding.edtPrice.setText(data);
//                    binding.edtPrice.setSelection(data.length());

                updateTotalPriceByTransitionType(editable.toString());
//                } catch (Exception e) {
//                    updateTotalPriceByTransitionType("");
//                }
//                binding.edtPrice.addTextChangedListener(this);
            }
        });
        binding.edtName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                binding.edtName.setBackgroundResource(R.drawable.bg_input_address);
            }
        });
        binding.edtPhone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                binding.edtPhone.setBackgroundResource(R.drawable.bg_input_address);
            }
        });

        binding.tvAddImage.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.edtEmail.getWindowToken(), 0);

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        binding.tvClosePost.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có muốn thoát không?")
                    .setPositiveButton("Ở lại", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNegativeButton("Thoát", (dialog, which) -> {
                        finish();
                    })
                    .create();
            alertDialog.show();
        });

        List<String> cityNames = new ArrayList<>();
        cityNames.add("-- Chọn Tỉnh/Thành --");
        for (City city : SessionManager.cities) {
            cityNames.add(city.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCity.setAdapter(adapter);
        onDistrict(null);
        onWard(null, null);

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 <= 0) {
                    address.setCityId(null);
                    onDistrict(null);
                    onWard(null, null);
                    return;
                }
                address.setCityId(SessionManager.cities.get(arg2 - 1).getId());
                onDistrict(SessionManager.cities.get(arg2 - 1).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
            }
        });
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 <= 0) {
                    address.setDistrictId(null);
                    onWard(null, null);
                    return;
                }
                String cityName = SessionManager.cities.get(binding.spinnerCity.getSelectedItemPosition() - 1).getName();
                String districtName = SessionManager.cities.get(binding.spinnerCity.getSelectedItemPosition() - 1).getDistricts().get(arg2 - 1).getName();

                address.setDistrictId(SessionManager.cities.get(binding.spinnerCity.getSelectedItemPosition() - 1).getDistricts().get(arg2 - 1).getId());
                onWard(cityName, districtName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
            }
        });
        binding.spinnerCommunes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    address.setWardId(null);
                    return;
                }
                address.setWardId(
                        SessionManager.cities.get(binding.spinnerCity.getSelectedItemPosition() - 1)
                                .getDistricts().get(binding.spinnerDistrict.getSelectedItemPosition() - 1)
                                .getWards().get(i - 1).getId()
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
        binding.btnContinue.setOnClickListener(v -> {
            if (address.getCityId() == null || address.getDistrictId() == null || address.getWardId() == null) {
                Toast.makeText(this, "Vui lòng chọn địa chỉ BĐS", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(binding.edtAcreage.getText())) {
                binding.layoutAcreage.setBackgroundResource(R.drawable.bg_input_address_error);
                binding.edtAcreage.setError("Vui lòng nhập diện tích BĐS");
                binding.edtAcreage.requestFocus();
                return;
            }
            acreage = binding.edtAcreage.getText().toString().trim();
            if (TextUtils.isEmpty(binding.edtPrice.getText())) {
                binding.layoutPrice.setBackgroundResource(R.drawable.bg_input_address_error);
                binding.edtPrice.setError("Vui lòng nhập Mức giá BĐS");
                binding.edtPrice.requestFocus();
                return;
            }
            price = binding.edtPrice.getText().toString().trim();
            if (TextUtils.isEmpty(binding.edtName.getText())) {
                binding.edtName.setBackgroundResource(R.drawable.bg_input_address_error);
                binding.edtName.setError("Vui lòng nhập diện tích BĐS");
                binding.edtName.requestFocus();
                return;
            }
            nameContract = binding.edtName.getText().toString().trim();
            emailContract = binding.edtEmail.getText().toString().trim();
            if (TextUtils.isEmpty(binding.edtPhone.getText())) {
                binding.edtPhone.setBackgroundResource(R.drawable.bg_input_address_error);
                binding.edtPhone.setError("Vui lòng nhập diện tích BĐS");
                binding.edtPhone.requestFocus();
                return;
            }
            phoneContract = binding.edtPhone.getText().toString().trim();
            if (TextUtils.isEmpty(binding.edtTitle.getText())) {
                binding.edtTitle.setError("Vui lòng nhập diện tích BĐS");
                binding.edtTitle.requestFocus();
                return;
            }
            titlePost = binding.edtTitle.getText().toString().trim();
            if (TextUtils.isEmpty(binding.edtDescribe.getText())) {
                binding.edtDescribe.setError("Vui lòng nhập diện tích BĐS");
                binding.edtDescribe.requestFocus();
                return;
            }
            descriptionPost = binding.edtDescribe.getText().toString().trim();
            if (files.size() < 3) {
                Toast.makeText(this, "Vui lòng chọn ít nhất 3 ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            uriStack.addAll(files);
            showLoading();

            int numBedrooms = 0;
            try {
                numBedrooms = Integer.parseInt(binding.edtBedrooms.getText().toString());
            } catch (Exception exception) {
                exception.getMessage();
            }
            int numBathrooms = 0;
            try {
                numBathrooms = Integer.parseInt(binding.edtBathrooms.getText().toString());
            } catch (Exception exception) {
                exception.getMessage();
            }
            PostRequest request = new PostRequest(
                    postType,
                    address.getCityId(),
                    address.getDistrictId(),
                    address.getWardId(),
                    binding.edtAddress.getText().toString(),
                    reStateType,
                    acreage.replace(".", ""),
                    price.replace(".", ""),
                    legalDocuments,
                    interior,
                    numBedrooms,
                    numBathrooms,
                    binding.edtDirection.getText().toString().trim(),
                    nameContract,
                    emailContract,
                    phoneContract,
                    titlePost,
                    descriptionPost
            );
            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);

            PostRepositoryImpl.getInstance().post(accessToken, request, new ExCallback<Integer>() {
                @Override
                public void onResponse(Integer data) {
                    if (data != null) {
                        postId = data;
                        uploadFiles();
                    } else {
                        hideLoading();
                        Toast.makeText(CreatePostActivity.this, "Đăng tin thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable var2) {
                    hideLoading();
                    Toast.makeText(CreatePostActivity.this, "Đăng tin thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showLoading() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.layoutLoading.setVisibility(View.GONE);
    }

    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(NConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private final AuthService service = retrofit.create(AuthService.class);

    private void uploadFiles() {
        try {
            if (uriStack.isEmpty()) {
                if (SessionManager.currentUser != null) {
                    if ("admin".equals(SessionManager.currentUser.getRole())) {
                        // duyệt tin
                        SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
                        String accessToken = sharedPreferences.getString("access_token", null);

                        PostRepositoryImpl.getInstance().approvedPost(accessToken, postId, new ExCallback<String>() {
                            @Override
                            public void onResponse(String data) {
                                // đẩy lên tin nổi bật
                                PostRepositoryImpl.getInstance().highLightMarkPost(accessToken, postId, new ExCallback<String>() {
                                    @Override
                                    public void onResponse(String data) {
                                        doneTask();
                                    }

                                    @Override
                                    public void onFailure(Throwable var2) {
                                        doneTask();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable var2) {
                                doneTask();
                            }
                        });
                    } else {
                        doneTask();
                    }
                } else {
                    doneTask();
                }
            } else {
                Uri uri = uriStack.pop();
                File f = FileUtils.getFile(this, uri);

                RequestBody requestFile = RequestBody.create(MultipartBody.FORM, f);

                MultipartBody.Part filePartImage = MultipartBody.Part.createFormData("file", "image.png", requestFile);

                service.uploadFile(filePartImage).enqueue(new Callback<BaseResult<Path>>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResult<Path>> call, @NonNull Response<BaseResult<Path>> response) {
                        if (response.body() != null && !TextUtils.isEmpty(response.body().getData().getPath())) {
                            PostRepositoryImpl.getInstance().putImages(new PutImageRequest(postId, response.body().getData().getPath()), new ExCallback<String>() {
                                @Override
                                public void onResponse(String data) {
                                    uploadFiles();
                                }

                                @Override
                                public void onFailure(Throwable var2) {
                                    uploadFiles();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResult<edu.re.estate.data.models.Path>> call, @NonNull Throwable throwable) {
                        hideLoading();
                        Toast.makeText(CreatePostActivity.this, "Upload ảnh thất bại: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doneTask() {
        hideLoading();
        // xong tiến trình
        Toast.makeText(this, "Đăng tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onDistrict(String cityName) {
        List<String> districtNames = new ArrayList<>();
        districtNames.add("-- Chọn Quận/Huyện --");
        if (!TextUtils.isEmpty(cityName)) {
            for (City city : SessionManager.cities) {
                if (city.getName().equals(cityName)) {
                    for (District district : city.getDistricts()) {
                        districtNames.add(district.getName());
                    }
                    break;
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDistrict.setAdapter(adapter);
    }

    private void onWard(String cityName, String districtName) {
        List<String> wardNames = new ArrayList<>();
        wardNames.add("-- Chọn Phường/Xã --");
        if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(districtName)) {
            for (City city : SessionManager.cities) {
                if (city.getName().equals(cityName)) {
                    for (District district : city.getDistricts()) {
                        if (district.getName().equals(districtName)) {
                            for (Ward ward : district.getWards()) {
                                wardNames.add(ward.getName());
                            }
                            break;
                        }
                    }
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wardNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCommunes.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            assert data != null;

//            String imageType = getContentResolver().getType(Objects.requireNonNull(data.getData()));
//            assert imageType != null;
//            String extension = imageType.substring(imageType.indexOf("/") + 1);
//
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
//            assert cursor != null;
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String filePath = cursor.getString(columnIndex);
//            cursor.close();
//            File file = new File(filePath);
//
            files.add(data.getData());
            albumAdapter.setFiles(files);
        }
    }

    private void updateTotalPriceByTransitionType(String data) {
        if (TextUtils.isEmpty(data)) {
            binding.tvTotal.setVisibility(View.GONE);
        } else {
            String result = convertToVietnameseCurrency(Long.parseLong(data));
            if (!TextUtils.isEmpty(postType) && postType.equals("sell")) {
                binding.tvTotal.setText("Tổng giá trị: " + result + " đ");
            } else {
                binding.tvTotal.setText("Tổng giá trị: " + result + " đ/tháng");
            }
            binding.tvTotal.setVisibility(View.VISIBLE);
        }
    }

    public String convertToVietnameseCurrency(long number) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        return numberFormat.format(number);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
