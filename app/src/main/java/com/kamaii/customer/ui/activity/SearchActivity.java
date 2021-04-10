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
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
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
import com.kamaii.customer.ui.adapter.SearchAdapter;
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

public class SearchActivity extends AppCompatActivity {
    ImageView img_back;
    public static AutoCompleteTextView searchView;
    private ArrayList<AllAtristListDTO> allAtristListDTOList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private RecyclerView rvDiscover;
    SearchAdapter discoverAdapter;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private LayoutInflater myInflater;
    ArrayList<String> productNaming = new ArrayList<>();
    private CustomTextViewBold tvSorry;
    public static LinearLayout tvNotFound;
    CustomButton backing;
    RelativeLayout speeching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        img_back = findViewById(R.id.img_back);
        speeching = findViewById(R.id.speeching);
        searchView = findViewById(R.id.svSearch);
        rvDiscover = findViewById(R.id.rvDiscover);
        tvSorry = findViewById(R.id.tvSorry);
        backing = findViewById(R.id.backing);
        tvNotFound = findViewById(R.id.tvNotFound);
        tvNotFound.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(SearchActivity.this);
        rvDiscover.setLayoutManager(mLayoutManager);
        searchView.setThreshold(1);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        myInflater = LayoutInflater.from(this);

        getProduct();

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
                    Toast.makeText(SearchActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
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
        searchView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    tvSorry.setText("Your search - " + searchView.getText().toString() + " - did not match any service at your location.");
                    tvNotFound.setVisibility(View.VISIBLE);
                    rvDiscover.setVisibility(View.GONE);
                    searchView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    searchView.dismissDropDown();
                    return true;
                }
                return false;
            }
        });
    }

    public void getProduct() {
        ProjectUtils.showProgressDialog(SearchActivity.this, false, "Loading...");
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getSearch(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtils.pauseProgressDialog();
                Log.e("RES_DISCOVER", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("RES_DISCOVER", s);

                        JSONObject object = new JSONObject(s);

                        Log.e("objectaayu", object.toString());
                        try {
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                productNaming.add(jsonObject.getString("service_name"));
                            }
                            showData();

                        } catch (Exception e) {
                            rvDiscover.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    } else {
                        rvDiscover.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                rvDiscover.setVisibility(View.GONE);
            }
        });
    }

    public void showData() {
        AutoCompleteAdapter<String> adapter = new AutoCompleteAdapter<>(this,
                R.layout.searck_item_list, R.id.text1, productNaming);
        searchView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchView.setText(result.get(0));
                    getArtist(result.get(0));
                }
                break;
        }
    }

    public class AutoCompleteAdapter<T> extends ArrayAdapter<T> implements Filterable {

        private Context context;
        @LayoutRes
        private int layoutRes;
        @IdRes
        private int textViewResId;
        private ArrayList<T> fullList;
        private ArrayList<T> originalValues;
        private ArrayFilter filter;
        private LayoutInflater inflater;
        private String query = "";

        public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<T> objects) {
            super(context, resource, textViewResourceId, objects);
            this.context = context;
            layoutRes = resource;
            textViewResId = textViewResourceId;
            fullList = (ArrayList<T>) objects;
            originalValues = new ArrayList<>(fullList);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return fullList.size();
        }

        @Override
        public T getItem(int position) {
            return fullList.get(position);
        }

        @Override
        public @NonNull
        View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createViewFromResource(inflater, position, convertView, parent, layoutRes);
        }

        private @NonNull
        View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                    @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
            final View view;
            final CustomTextView text;

            if (convertView == null) {
                view = inflater.inflate(resource, parent, false);
            } else {
                view = convertView;
            }

            try {
                if (textViewResId == 0) {
                    text = (CustomTextView) view;
                } else {
                    text = view.findViewById(textViewResId);

                    if (text == null) {
                        throw new RuntimeException("Failed to find view with ID "
                                + context.getResources().getResourceName(textViewResId)
                                + " in item layout");
                    }
                }
            } catch (ClassCastException e) {
                Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException(
                        "ArrayAdapter requires the resource ID to be a TextView", e);
            }

            final T item = getItem(position);
            text.setText(highlight(query, item.toString()));

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvNotFound.setVisibility(View.GONE);
                    searchView.setText(item.toString());
                    searchView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    searchView.dismissDropDown();
                    getArtist(item.toString());
                }
            });
            return view;
        }

        @Override
        public @NonNull
        Filter getFilter() {
            if (filter == null) {
                filter = new ArrayFilter();
            }
            return filter;
        }

        private class ArrayFilter extends Filter {
            private final Object lock = new Object();

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                if (prefix == null) {
                    query = "";
                } else {
                    query = prefix.toString();
                }
                if (originalValues == null) {
                    synchronized (lock) {
                        originalValues = new ArrayList<>(fullList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        ArrayList<T> list = new ArrayList<>(originalValues);
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
                    final String prefixString = prefix.toString().toLowerCase();
                    ArrayList<T> values = originalValues;
                    int count = values.size();

                    ArrayList<T> newValues = new ArrayList<>(count);

                    for (int i = 0; i < count; i++) {
                        T item = values.get(i);
                        if (item.toString().toLowerCase().contains(prefixString)) {
                            newValues.add(item);
                        }

                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    fullList = (ArrayList<T>) results.values;
                } else {
                    fullList = new ArrayList<>();
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        }

        private CharSequence highlight(@NonNull String search, @NonNull CharSequence originalText) {
            if (search.isEmpty())
                return originalText;

            // ignore case and accents
            // the same thing should have been done for the search text
            String normalizedText = Normalizer
                    .normalize(originalText, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase(Locale.ENGLISH);

            int start = normalizedText.indexOf(search.toLowerCase(Locale.ENGLISH));
            if (start < 0) {
                // not found, nothing to do
                return originalText;
            } else {
                // highlight each appearance in the original text
                // while searching in normalized text
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(),
                            originalText.length());

                    highlighted.setSpan(new StyleSpan(Typeface.BOLD), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    start = normalizedText.indexOf(search, spanEnd);
                }

                return highlighted;
            }
        }
    }

    public void getArtist(String stext) {
        ProjectUtils.showProgressDialog(SearchActivity.this, false, "Loading...");

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        tvNotFound.setVisibility(View.GONE);
        tvSorry.setText("Your search - " + stext + " - did not match any service at your location.");
        Call<ResponseBody> callone = api.getSearchProvider(stext, userDTO.getUser_id(), prefrence.getValue(Consts.LONGITUDE), prefrence.getValue(Consts.LATITUDE));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtils.pauseProgressDialog();

                Log.e("RES_DISCOVER", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("RES_DISCOVER", s);

                        JSONObject object = new JSONObject(s);

                        Log.e("objectaayu", object.toString());
                        try {
                            JSONObject v = object.getJSONObject("data");
                            String str = v.getString("artist_id").toString();

                            Intent intent = new Intent(SearchActivity.this, ArtistProfile.class);
                            intent.putExtra(Consts.ARTIST_ID, str);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            rvDiscover.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    } else {
                        tvNotFound.setVisibility(View.VISIBLE);
                        rvDiscover.setVisibility(View.GONE);
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
                rvDiscover.setVisibility(View.GONE);
            }
        });
    }

    private void showDataProvider() {
        discoverAdapter = new SearchAdapter(SearchActivity.this, allAtristListDTOList, myInflater, searchView.getText().toString());
        rvDiscover.setAdapter(discoverAdapter);
    }


}