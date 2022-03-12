package com.kamaii.customer.ui.activity;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.AllAtristListDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.models.ProductSearchModel;
import com.kamaii.customer.utils.CustomButton;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductSearchActivity extends AppCompatActivity {

    ImageView img_back;
    public static AutoCompleteTextView searchView;
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    RecyclerView rvDiscover;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private LayoutInflater myInflater;
    ArrayList<String> productNaming = new ArrayList<>();
    private CustomTextViewBold tvSorry;
    public static LinearLayout tvNotFound;
    CustomButton backing;
    RelativeLayout speeching;
    androidx.appcompat.widget.SearchView productsearch;
    List<ProductSearchModel> productSearchResult;
    boolean search_product_flag = false;
    ProductSearchAdapter productSearchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        search_product_flag = getIntent().getBooleanExtra("search_product_flag", false);
        img_back = findViewById(R.id.img_back);
        productsearch = findViewById(R.id.productsearch);
        speeching = findViewById(R.id.speeching);
        rvDiscover = findViewById(R.id.rvDiscover);
        tvSorry = findViewById(R.id.tvSorry);
        backing = findViewById(R.id.backing);
        tvNotFound = findViewById(R.id.tvNotFound);
        tvNotFound.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(ProductSearchActivity.this);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        myInflater = LayoutInflater.from(this);



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        speeching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(ProductSearchActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        findViewById(R.id.myLayout).getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        });
     

        productsearch.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equalsIgnoreCase(null)){

                    getProductSearchList(newText);
                }
                return false;
            }
        });
    }

    public void getProductSearchList(String stext) {

      //  ProjectUtils.showProgressDialog(ProductSearchActivity.this, false, "Loading...");

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        tvNotFound.setVisibility(View.GONE);
        tvSorry.setText("Your search - " + stext + " - did not match any service at your location.");
        Call<ResponseBody> callone = api.getProductSearch(stext);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          //      ProjectUtils.pauseProgressDialog();

                Log.e("SEARCH_PRD_RES_DISCOVER", response.message());
                try {
                    if (response.isSuccessful()) {

                        rvDiscover.setVisibility(View.VISIBLE);

                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("SEARCH_PRD_RES_DISCOVER", s);

                        JSONObject object = new JSONObject(s);
                        // JSONArray jsonArray = object.getJSONArray("data");


                        Log.e("objectaayu", object.toString());
                        try {
                            Log.e("SEARCH_PRD_RES_DISCOVER"," tracker 1 ");

                            productSearchResult = new ArrayList<>();
                            Log.e("SEARCH_PRD_RES_DISCOVER"," tracker 2 ");

                            Type getpetDTO = new TypeToken<List<ProductSearchModel>>() {
                            }.getType();

                            Log.e("SEARCH_PRD_RES_DISCOVER"," tracker 3 ");

                            productSearchResult = (ArrayList<ProductSearchModel>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                            Log.e("SEARCH_PRD_RES_DISCOVER"," tracker 4 ");

                            Log.e("SEARCH_PRD_RES_DISCOVER"," list_size"+productSearchResult.toString());

                            // JSONArray jsonArray = object.getJSONArray("data");
                            Log.e("SEARCH_PRD_RES_DISCOVER"," tracker 5 ");

                            if (productSearchResult.size() != 0) {

                                rvDiscover.setVisibility(View.VISIBLE);
                                tvNotFound.setVisibility(View.GONE);

                                productSearchAdapter = new ProductSearchAdapter(ProductSearchActivity.this, productSearchResult);
                                rvDiscover.setLayoutManager(new LinearLayoutManager(ProductSearchActivity.this));
                                rvDiscover.setAdapter(productSearchAdapter);
                            }
                            else {
                                rvDiscover.setVisibility(View.GONE);
                                tvNotFound.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                      //  tvNotFound.setVisibility(View.VISIBLE);
                        // rvDiscover.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tvNotFound.setVisibility(View.VISIBLE);
                // rvDiscover.setVisibility(View.GONE);
            }
        });

    }
  


    public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductSearchViewHolder> {

        Context context;
        List<ProductSearchModel> arraylist;

        public ProductSearchAdapter(Context context, List<ProductSearchModel> arraylist) {
            this.context = context;
            this.arraylist = arraylist;
            Log.e("SEARCH_PRD_RES_DISCOVER"," adapter "+arraylist.toString());
        }

        @NonNull
        @Override
        public ProductSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.searck_item_list,parent,false);
            return new ProductSearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductSearchViewHolder holder, int position) {
            holder.name.setText(arraylist.get(position).getUser_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DiscoverActivity.class);
                    intent.putExtra(Consts.SUB_CATEGORY_ID, arraylist.get(position).getSubcat_id());
                    intent.putExtra(Consts.Sub_CAT_NAME, arraylist.get(position).getSubcat_name());
                    intent.putExtra(Consts.CATEGORY_ID, arraylist.get(position).getCat_id());
                    context.startActivity(intent);

                   /* Intent intent = new Intent(ProductSearchActivity.this, ArtistProfile.class);
                    intent.putExtra(Consts.ARTIST_ID, arraylist.get(position).getArtist_id());
                    startActivity(intent);
                    finish();*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return arraylist.size();
        }

        class ProductSearchViewHolder extends RecyclerView.ViewHolder {

            CustomTextView name;
            public ProductSearchViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.text1);
            }
        }
    }

}