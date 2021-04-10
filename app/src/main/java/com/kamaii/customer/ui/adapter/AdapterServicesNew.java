package com.kamaii.customer.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.ArtistDetailsDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.SetonItemPlusListener;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.activity.BookingProduct;
import com.kamaii.customer.ui.activity.ViewServices;
import com.kamaii.customer.ui.models.CartModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.kamaii.customer.utils.ProjectUtils.TAG;

public class AdapterServicesNew extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private BookingProduct context;
    private ArrayList<ProductDTO> productDTOList, searchBookingsList, productDTOListtemp;
    boolean select = true;
    private ArtistDetailsDTO artistDetailsDTO;
    Boolean isCheck = true;
    private Dialog dialogpriview;
    private SetonItemPlusListener setonItemPlusListener;
    private HashMap<String, String> paramAddtoCart = new HashMap<>();
    EditText etquanity;
    CustomTextViewBold tvYesPass, llltxtamount;
    ImageView tvNoPass;
    int maxmimumvalue = 0;
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    UserDTO userDTO;
    SharedPrefrence prefrence;
    private ArrayList<CartModel> userBookingList;
    private ArrayList<String> Qty_array;
    String SHIP_charge = "";
    String SUB_TOTAL = "";
    String TOTAL = "";
    int counter = 0;
    Double total_amount = 0.0;
    private String text_qty = "";

    public AdapterServicesNew(BookingProduct context, ArrayList<ProductDTO> productDTOList, ArtistDetailsDTO artistDetailsDTO, SetonItemPlusListener setonItemPlusListener) {
        this.context = context;
        this.productDTOList = productDTOList;
        this.searchBookingsList = new ArrayList<ProductDTO>();
        this.searchBookingsList.addAll(productDTOList);
        this.artistDetailsDTO = artistDetailsDTO;
        this.setonItemPlusListener = setonItemPlusListener;
        Qty_array = new ArrayList<>();
        prefrence = SharedPrefrence.getInstance(context);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return productDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services_cart, null);

        ImageView iv_bottom_foster;
        RelativeLayout rel_view;
        LinearLayout img_plus;
        CustomTextViewBold tvProductName;
        LinearLayout quentity_linear, quentity_linear_child, second_linear, img_minus, layquankm;
        CustomTextViewBold tvdesc, service_rating_digit, tvquantity, tvPrice, tvroundtrip, total_count;
        CustomTextView short_desc, tvPriceDescription, tvDiscountPrice;
        ImageView arrow_desc;
        CheckBox cbSelect;
        CustomTextView stripe_price;
        View viewchck;

        iv_bottom_foster = itemView.findViewById(R.id.iv_bottom_foster);
        img_plus = itemView.findViewById(R.id.img_plus);
        img_minus = itemView.findViewById(R.id.img_minus);
        layquankm = itemView.findViewById(R.id.layquankm);
        stripe_price = itemView.findViewById(R.id.price_stripe);
        quentity_linear = itemView.findViewById(R.id.quentity_linear);
        tvProductName = itemView.findViewById(R.id.tvProductName);
        tvPrice = (CustomTextViewBold) itemView.findViewById(R.id.tvPrice);
        tvPriceDescription = (CustomTextView) itemView.findViewById(R.id.tvPriceDescription);
        tvquantity = (CustomTextViewBold) itemView.findViewById(R.id.tvservicequantity);
        quentity_linear_child = itemView.findViewById(R.id.quentity_linear_child);
        total_count = (CustomTextViewBold) itemView.findViewById(R.id.total_count);
        tvDiscountPrice = (CustomTextView) itemView.findViewById(R.id.tvDiscountPrice);

        cbSelect = itemView.findViewById(R.id.cbSelect);
        viewchck = itemView.findViewById(R.id.viewchck);

        counter = Integer.parseInt(productDTOList.get(position).getCart_quantity());
        if (position == 0) {
            viewchck.setVisibility(View.INVISIBLE);
        } else {
            viewchck.setVisibility(View.VISIBLE);
        }
        String txt = tvquantity.getText().toString();
        if (txt.equalsIgnoreCase("Add")) {
            tvquantity.setEnabled(false);
            tvquantity.setClickable(false);
        } else {
            tvquantity.setEnabled(true);
            tvquantity.setClickable(true);
        }

       /* quentity_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvquantity.getText().toString().equals("Add")) {
                    img_plus.performClick();
                }
            }
        });
*/
        if (counter == 0) {
            img_minus.setVisibility(View.GONE);
            tvquantity.setText("Add");
        } else {
            img_minus.setVisibility(View.VISIBLE);
            tvquantity.setText(String.valueOf(counter));
        }
        Qty_array.add(productDTOList.get(position).getQuantitydays());

        tvPrice.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(position).getPrice()));
        tvProductName.setText(productDTOList.get(position).getProduct_name());
        tvPriceDescription.setText(Html.fromHtml(productDTOList.get(position).getDescriptionType()));
        tvDiscountPrice.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(position).getDiscounted_price()));
        total_count.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(position).getProduct_final_amount()));

        tvDiscountPrice.setPaintFlags(tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (productDTOList.get(position).getProduct_image().equals("https://kamaii.in/app/")) {
            iv_bottom_foster.setVisibility(View.GONE);
        }

        if (productDTOList.get(position).isSelected()) {
            cbSelect.setChecked(true);
        } else {
            cbSelect.setChecked(false);
        }
        tvquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(context, "asasasas", Toast.LENGTH_SHORT).show();
                dialogpriview = new Dialog(context, R.style.Theme_Dialog);
                dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpriview.setContentView(R.layout.dailog_quantity);
                dialogpriview.setCancelable(false);
                etquanity = dialogpriview.findViewById(R.id.etquanity);
                tvYesPass = dialogpriview.findViewById(R.id.tvYesPass);
                tvNoPass = dialogpriview.findViewById(R.id.tvNoPass);
                llltxtamount = dialogpriview.findViewById(R.id.llltxtamount);
                dialogpriview.show();
                etquanity.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etquanity, InputMethodManager.SHOW_IMPLICIT);
                int oldqty = 0;
                if (tvquantity.getText().toString().equals("Add")) {
                    oldqty = Integer.parseInt("0");
                    etquanity.setText(String.valueOf(oldqty));

                } else {
                    oldqty = Integer.parseInt(tvquantity.getText().toString());
                    etquanity.setText(String.valueOf(oldqty));
                }

                if (!productDTOList.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
                    maxmimumvalue = Integer.parseInt(productDTOList.get(position).getMaxmiumvalue());
                    llltxtamount.setText("Maximum Qty:" + " " + String.valueOf(maxmimumvalue));
                }

                etquanity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        text_qty = s.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                int finalOldqty = oldqty;
                tvYesPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!productDTOList.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
                            maxmimumvalue = Integer.parseInt(productDTOList.get(position).getMaxmiumvalue());
                        }
                        if (!text_qty.equalsIgnoreCase("") && !text_qty.equals("")) {
                            int quantity = Integer.parseInt(text_qty);
                            if (quantity <= maxmimumvalue) {
                                img_minus.setVisibility(View.VISIBLE);
                                double price4 = finalOldqty * Double.parseDouble(productDTOList.get(position).getPrice());
                                total_amount -= price4;

                                counter = quantity;

                                tvquantity.setText(String.valueOf(quantity));
                                price4 = quantity * Double.parseDouble(productDTOList.get(position).getPrice());
                                total_amount += price4;

                                paramAddtoCart.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                                paramAddtoCart.put(Consts.USER_ID, userDTO.getUser_id());
                                paramAddtoCart.put(Consts.SERVICE_ID, productDTOList.get(position).getId());
                                paramAddtoCart.put("qty", String.valueOf(quantity));

                                if (quantity != 0) {
                                    productDTOList.get(position).setSelected(true);
                                }
                                dialogaddtocart();
                                dialogpriview.dismiss();
                            } else {
                                sickbar("Not Available");
                            }
                        } else {
                            sickbar("Enter quantity properly");
                        }

                    }
                });

                tvNoPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogpriview.dismiss();
                        dialogpriview.cancel();
                    }
                });
            }
        });
        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                if (tvquantity.getText().toString().equals("Add")) {
                    quantity = Integer.parseInt("0");
                } else {
                    quantity = Integer.parseInt(tvquantity.getText().toString());
                }
                if (quantity >= 1) {
                    double price = quantity * Double.parseDouble(productDTOList.get(position).getPrice());
                    total_amount -= price;
                    quantity--;
                    img_minus.setVisibility(View.VISIBLE);
                    tvquantity.setText(String.valueOf(quantity));
                    for (int i = 0; i < productDTOList.size(); i++) {
                        paramAddtoCart.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                        paramAddtoCart.put(Consts.USER_ID, userDTO.getUser_id());
                        paramAddtoCart.put(Consts.SERVICE_ID, productDTOList.get(position).getId());

                        productDTOList.get(i).setQuantityvalue(tvquantity.getText().toString());
                        if (i == position) {
                            Qty_array.set(i, tvquantity.getText().toString());
                        } else {
                        }
                    }
                    int servicecharge = 0;
                    if (!productDTOList.get(position).getService_charge().equalsIgnoreCase("")) {
                        servicecharge = Integer.parseInt(productDTOList.get(position).getService_charge());
                    }
                    price = quantity * Double.parseDouble(productDTOList.get(position).getPrice());
                    total_amount += price;
                    if (quantity != 0) {
                        tvquantity.setClickable(true);

                        productDTOList.get(position).setSelected(true);

                    } else {
                        productDTOList.get(position).setSelected(false);
                        img_minus.setVisibility(View.GONE);
                        tvquantity.setText("Add");
                        // tvquantity.setClickable(false);
                        // tvquantity.setEnabled(false);
                    }
                    removetocart();
                } else {
                    Log.e(TAG, "onClick: dhaval " + productDTOList.size());
                    img_minus.setVisibility(View.GONE);
                    tvquantity.setText("Add");

                }
            }
        });
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                if (tvquantity.getText().toString().equals("Add")) {
                    quantity = Integer.parseInt("0");
                } else {
                    quantity = Integer.parseInt(tvquantity.getText().toString());
                }

                if (!productDTOList.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
                    maxmimumvalue = Integer.parseInt(productDTOList.get(position).getMaxmiumvalue());
                }
                img_minus.setVisibility(View.VISIBLE);

                if (quantity < maxmimumvalue) {
                    double price = quantity * Double.parseDouble(productDTOList.get(position).getPrice());
                    total_amount -= price;
                    quantity++;

                    tvquantity.setText(String.valueOf(quantity));
                    for (int i = 0; i < productDTOList.size(); i++) {
                        paramAddtoCart.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                        paramAddtoCart.put(Consts.USER_ID, userDTO.getUser_id());
                        paramAddtoCart.put(Consts.SERVICE_ID, productDTOList.get(position).getId());

                        if (i == position) {
                            Qty_array.set(i, tvquantity.getText().toString());

                        } else {
                        }
                    }
                    int servicecharge = 0;
                    if (!productDTOList.get(position).getService_charge().equalsIgnoreCase("")) {
                        servicecharge = Integer.parseInt(productDTOList.get(position).getService_charge());
                    }
                    price = quantity * Double.parseDouble(productDTOList.get(position).getPrice());
                    total_amount += price;
                    if (quantity != 0) {

                        tvquantity.setEnabled(true);
                        tvquantity.setClickable(true);
                        productDTOList.get(position).setSelected(true);

                    } else {
                        productDTOList.get(position).setSelected(false);
                    }
                    addtocart();
                } else {
                    sickbar("Not Available");
                }
            }
        });
        return itemView;
    }

    public void addtocart() {
        new HttpsRequest(Consts.ADD_TO_CART_TEST_API_LIVE, paramAddtoCart, context).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("LODULALITING", response.toString());
                if (flag) {
                    context.getArtist();
                } else {
                }
            }
        });
    }

    public void dialogaddtocart() {
        new HttpsRequest(Consts.DIALOG_ADD_TO_CART_TEST_API_LIVE, paramAddtoCart, context).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    context.getArtist();
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public void removetocart() {
        new HttpsRequest(Consts.REMOVE_FROM_CART_TEST_API_LIVE, paramAddtoCart, context).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    context.getArtist();
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public void sickbar(String msg) {
        Snackbar snackbar = Snackbar.make(context.findViewById(R.id.RRsncbarrr), msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productDTOList.clear();
        if (charText.length() == 0) {
            productDTOList.addAll(searchBookingsList);
        } else {
            for (ProductDTO historyDTO : searchBookingsList) {
                if (historyDTO.getProduct_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    productDTOList.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;
            } else {
                value = false;
            }
        }
        return value;
    }

}