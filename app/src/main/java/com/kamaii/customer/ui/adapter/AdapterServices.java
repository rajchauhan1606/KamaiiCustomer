package com.kamaii.customer.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.kamaii.customer.ui.activity.Booking;
import com.kamaii.customer.ui.activity.ServiceSlider;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.activity.ViewServices;
import com.kamaii.customer.ui.models.CartModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kamaii.customer.utils.ProjectUtils.TAG;

public class AdapterServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private ViewServices context;
    private ArrayList<ProductDTO> productDTOList, searchBookingsList, productDTOListtemp;
    boolean select = true;
    private ArtistDetailsDTO artistDetailsDTO;
    Boolean isCheck = true;
    private Dialog dialogpriview;
    private SetonItemPlusListener setonItemPlusListener;

    EditText etquanity;
    CustomTextViewBold tvYesPass, llltxtamount;
    ImageView tvNoPass;
    int maxmimumvalue = 0;
    private HashMap<String, String> paramAddtoCart = new HashMap<>();
    UserDTO userDTO;
    SharedPrefrence prefrence;
    private ArrayList<CartModel> userBookingList;
    private ArrayList<String> Qty_array;
    String SHIP_charge = "";
    String SUB_TOTAL = "";
    String TOTAL = "";
    String item_count = "";
    int counter = 0;
    Double total_amount = 0.0;
    private String text_qty = "";

    public AdapterServices(ViewServices context, ArrayList<ProductDTO> productDTOList, ArtistDetailsDTO artistDetailsDTO, SetonItemPlusListener setonItemPlusListener) {
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

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services, parent, false);
        return new ServiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int position) {


        final ServiceViewHolder holder = (ServiceViewHolder) myViewHolder;
        final int pos = position;
        myViewHolder.setIsRecyclable(false);

        counter = Integer.parseInt(productDTOList.get(pos).getCart_quantity());
        String txt = holder.tvquantity.getText().toString();
        if (txt.equalsIgnoreCase("Add")) {
            holder.tvquantity.setEnabled(false);
            holder.tvquantity.setClickable(false);
        } else {
            holder.tvquantity.setEnabled(true);
            holder.tvquantity.setClickable(true);
        }

        holder.quentity_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_plus.performClick();
            }
        });

        if (counter == 0) {
            holder.img_minus.setVisibility(View.GONE);
            holder.tvquantity.setText("Add");
        } else {
            holder.img_minus.setVisibility(View.VISIBLE);
            holder.tvquantity.setText(String.valueOf(counter));
        }
        Qty_array.add(productDTOList.get(pos).getQuantitydays());

        if (checkarss(ViewAddressActivity.catid)) {
            if (ViewAddressActivity.sub_category_idd.equalsIgnoreCase("453")) {
                holder.tvService_charge.setVisibility(View.GONE);
            } else {
                if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                    holder.tvService_charge.setVisibility(View.VISIBLE);
                    holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOList.get(pos).getService_charge());
                } else {
                    holder.tvService_charge.setVisibility(View.GONE);
                }
            }

        } else {
            holder.tvService_charge.setVisibility(View.GONE);
        }

        holder.tvPrice.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(pos).getPrice()));
        holder.tvProductName.setText(productDTOList.get(pos).getProduct_name());

        if (productDTOList.get(pos).getFull_description().equals("") || productDTOList.get(pos).getFull_description().isEmpty()) {
            holder.descptionreadmore.setVisibility(View.GONE);
        } else {
            holder.descptionreadmore.setVisibility(View.VISIBLE);
        }

        if (Html.fromHtml(productDTOList.get(pos).getShort_description()).equals("NODATA")) {

            holder.short_desc.setVisibility(View.GONE);
            holder.description_linear.setVisibility(View.GONE);

        } else {
            holder.short_desc.setVisibility(View.VISIBLE);
            holder.description_linear.setVisibility(View.VISIBLE);
            holder.short_desc.setText(Html.fromHtml(productDTOList.get(pos).getShort_description()));
        }


        holder.service_rating_digit.setText(Html.fromHtml(productDTOList.get(pos).getProduct_rating()) + "/5");

        if (!ViewAddressActivity.mainkm.equalsIgnoreCase("")) {
            if (ViewAddressActivity.sub_category_idd.equalsIgnoreCase("453")) {
                holder.tvroundtrip.setVisibility(View.GONE);
            } else {

                if (checkarss(ViewAddressActivity.catid)) {
                    if (productDTOList.get(pos).getRoundtrip().equalsIgnoreCase("1")) {
                        holder.tvroundtrip.setVisibility(View.VISIBLE);
                        holder.tvroundtrip.setText("Round Trip");
                    } else if (productDTOList.get(pos).getRoundtrip().equalsIgnoreCase("0")) {
                        holder.tvroundtrip.setVisibility(View.VISIBLE);
                        holder.tvroundtrip.setText("One Way");
                    } else {
                        holder.tvroundtrip.setVisibility(View.GONE);
                    }
                } else {
                    holder.tvroundtrip.setVisibility(View.GONE);
                }

            }


        } else {
            holder.tvroundtrip.setVisibility(View.GONE);
        }

        if (productDTOList.get(pos).getShort_description().equalsIgnoreCase("") || productDTOList.get(pos).getShort_description().equalsIgnoreCase(" ")) {
            holder.description_linear.setVisibility(View.GONE);
        } else {
            holder.description_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck) {
                        holder.second_linear.setVisibility(View.GONE);
                        holder.description_linear.setVisibility(View.GONE);
                        holder.tvdesc23.setText(Html.fromHtml(productDTOList.get(pos).getFull_description()));
                        holder.tvdesc23.setVisibility(View.VISIBLE);

                        isCheck = false;
                    } else {
                        holder.second_linear.setVisibility(View.VISIBLE);
                        holder.tvdesc23.setVisibility(View.GONE);
                        isCheck = true;
                    }
                }
            });

        }

        if (!productDTOList.get(pos).getPrice().equalsIgnoreCase("")) {

            double price = Double.parseDouble(productDTOList.get(pos).getPrice());
            //    int discountprice = (int) ((price * Consts.DISCOUNT_RATE / 100) + price);
            holder.tvDiscountPrice.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(pos).getDis_price()));
        }

        holder.tvDiscountPrice.setPaintFlags(holder.tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (productDTOList.get(pos).getProduct_image().equals("https://kamaii.in/app/")) {

            holder.iv_bottom_foster.setVisibility(View.GONE);
        }

        if (productDTOList.get(pos).isSelected()) {
            holder.cbSelect.setChecked(true);

        } else {
            holder.cbSelect.setChecked(false);
        }

        holder.tvquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (holder.tvquantity.getText().toString().equals("Add")) {
                    oldqty = Integer.parseInt("0");
                    etquanity.setText(String.valueOf(oldqty));

                } else {
                    oldqty = Integer.parseInt(holder.tvquantity.getText().toString());
                    etquanity.setText(String.valueOf(oldqty));
                }

                if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
                    maxmimumvalue = Integer.parseInt(productDTOList.get(pos).getMaxmiumvalue());
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

                        if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
                            maxmimumvalue = Integer.parseInt(productDTOList.get(pos).getMaxmiumvalue());
                        }
                        if (!text_qty.equalsIgnoreCase("") && !text_qty.equals("")) {
                            int quantity = Integer.parseInt(text_qty);
                            if (quantity <= maxmimumvalue) {
                                holder.img_minus.setVisibility(View.VISIBLE);
                                double price4 = finalOldqty * Double.parseDouble(productDTOList.get(pos).getPrice());
                                total_amount -= price4;

                                counter = quantity;

                                holder.tvquantity.setText(String.valueOf(quantity));
                                price4 = quantity * Double.parseDouble(productDTOList.get(pos).getPrice());
                                total_amount += price4;

                                paramAddtoCart.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                                paramAddtoCart.put(Consts.USER_ID, userDTO.getUser_id());
                                paramAddtoCart.put(Consts.SERVICE_ID, productDTOList.get(pos).getId());
                                paramAddtoCart.put("qty", String.valueOf(quantity));

                                if (quantity != 0) {
                                    productDTOList.get(pos).setSelected(true);
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
        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                if (holder.tvquantity.getText().toString().equals("Add")) {
                    quantity = Integer.parseInt("0");
                } else {
                    quantity = Integer.parseInt(holder.tvquantity.getText().toString());
                }
                if (quantity >= 1) {
                    double price = quantity * Double.parseDouble(productDTOList.get(pos).getPrice());
                    total_amount -= price;
                    quantity--;
                    holder.img_minus.setVisibility(View.VISIBLE);
                    holder.tvquantity.setText(String.valueOf(quantity));
                    for (int i = 0; i < productDTOList.size(); i++) {
                        paramAddtoCart.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                        paramAddtoCart.put(Consts.USER_ID, userDTO.getUser_id());
                        paramAddtoCart.put(Consts.SERVICE_ID, productDTOList.get(pos).getId());

                        productDTOList.get(i).setQuantityvalue(holder.tvquantity.getText().toString());
                        if (i == pos) {
                            Qty_array.set(i, holder.tvquantity.getText().toString());
                        } else {
                        }
                    }
                    int servicecharge = 0;
                    if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                        servicecharge = Integer.parseInt(productDTOList.get(pos).getService_charge());
                    }
                    price = quantity * Double.parseDouble(productDTOList.get(pos).getPrice());
                    total_amount += price;
                    if (quantity != 0) {
                        holder.tvquantity.setClickable(true);

                        productDTOList.get(pos).setSelected(true);

                    } else {
                        productDTOList.get(pos).setSelected(false);
                        holder.img_minus.setVisibility(View.GONE);
                        holder.tvquantity.setText("Add");
                        holder.tvquantity.setClickable(false);
                        holder.tvquantity.setEnabled(false);
                    }
                    removetocart();
                } else {
                    holder.img_minus.setVisibility(View.GONE);
                    holder.tvquantity.setText("Add");
                }
            }
        });
        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                if (holder.tvquantity.getText().toString().equals("Add")) {
                    quantity = Integer.parseInt("0");
                } else {
                    quantity = Integer.parseInt(holder.tvquantity.getText().toString());
                }

                if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
                    maxmimumvalue = Integer.parseInt(productDTOList.get(pos).getMaxmiumvalue());
                }
                holder.img_minus.setVisibility(View.VISIBLE);

                if (quantity < maxmimumvalue) {
                    double price = quantity * Double.parseDouble(productDTOList.get(pos).getPrice());
                    total_amount -= price;
                    quantity++;

                    holder.tvquantity.setText(String.valueOf(quantity));
                    for (int i = 0; i < productDTOList.size(); i++) {
                        paramAddtoCart.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
                        paramAddtoCart.put(Consts.USER_ID, userDTO.getUser_id());
                        paramAddtoCart.put(Consts.SERVICE_ID, productDTOList.get(pos).getId());


                        if (i == pos) {
                            Qty_array.set(i, holder.tvquantity.getText().toString());

                        } else {
                        }
                    }
                    int servicecharge = 0;
                    if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                        servicecharge = Integer.parseInt(productDTOList.get(pos).getService_charge());
                    }
                    price = quantity * Double.parseDouble(productDTOList.get(pos).getPrice());
                    total_amount += price;
                    if (quantity != 0) {

                        holder.tvquantity.setEnabled(true);
                        holder.tvquantity.setClickable(true);
                        productDTOList.get(pos).setSelected(true);

                    } else {
                        productDTOList.get(pos).setSelected(false);
                    }
                    addtocart();
                } else {
                    sickbar("Not Available");
                }
            }
        });
        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (productDTOList.get(pos).isSelected()) {
                    productDTOList.get(pos).setSelected(false);

                    double quantity = Double.parseDouble(context.tvPrice.getText().toString());
                    quantity = quantity - Double.parseDouble(productDTOList.get(pos).getPrice());
                    context.tvPrice.setText(String.valueOf(quantity));
                    holder.rel_view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    holder.rel_view.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_bg));
                    if (select) {
                        context.tvPrice.setText(productDTOList.get(pos).getPrice());
                        context.tvPriceType.setText(" " + "Rs.");
                        select = false;
                    } else {
                        double quantity = Double.parseDouble(context.tvPrice.getText().toString());
                        quantity = quantity + Double.parseDouble(productDTOList.get(pos).getPrice());
                        context.tvPrice.setText(String.valueOf(quantity));
                    }
                }
                notifyDataSetChanged();
            }
        });

    }



    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    @Override
    public long getItemId(int pos) {
        return super.getItemId(pos);
    }

    @Override
    public int getItemViewType(int pos) {
        return pos;
    }

    public void sickbar(String msg) {
        Snackbar snackbar = Snackbar.make(context.findViewById(R.id.RRsncbarr), msg, Snackbar.LENGTH_LONG);
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
        context.total_product.setText(productDTOList.size() + " Items");
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

    public void dialogaddtocart() {
        new HttpsRequest(Consts.DIALOG_ADD_TO_CART_TEST_API_LIVE, paramAddtoCart, context).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CartModel>>() {
                        }.getType();
                        JSONObject jsonArray = response.getJSONObject("data");
                        SHIP_charge = jsonArray.getString("shipping_charges");
                        SUB_TOTAL = jsonArray.getString("subtotal");
                        TOTAL = jsonArray.getString("total_amount");
                        item_count = jsonArray.getString("item_count");
                        total_amount = Double.parseDouble(SUB_TOTAL);
                        ((ViewServices) context).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge, item_count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public void addtocart() {
        new HttpsRequest(Consts.ADD_TO_CART_TEST_API_LIVE, paramAddtoCart, context).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("WEBBYNIGHT", response.toString());
                    try {
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CartModel>>() {
                        }.getType();
                        JSONObject jsonArray = response.getJSONObject("data");
                        SHIP_charge = jsonArray.getString("shipping_charges");
                        SUB_TOTAL = jsonArray.getString("subtotal");
                        TOTAL = jsonArray.getString("total_amount");
                        item_count = jsonArray.getString("item_count");
                        total_amount = Double.parseDouble(SUB_TOTAL);
                        ((ViewServices) context).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge, item_count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void removetocart() {
        new HttpsRequest(Consts.REMOVE_FROM_CART_TEST_API_LIVE, paramAddtoCart, context).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CartModel>>() {
                        }.getType();
                        JSONObject jsonArray = response.getJSONObject("data");
                        SHIP_charge = jsonArray.getString("shipping_charges");
                        SUB_TOTAL = jsonArray.getString("subtotal");
                        TOTAL = jsonArray.getString("total_amount");
                        item_count = jsonArray.getString("item_count");
                        total_amount = Double.parseDouble(SUB_TOTAL);
                        ((ViewServices) context).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge, item_count);
                    } catch (Exception e) {
                        Log.e("SUB_TOTALa", "" + SUB_TOTAL);
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_bottom_foster;
        RelativeLayout rel_view;
        LinearLayout img_plus, descptionreadmore;
        CustomTextViewBold tvProductName;
        LinearLayout quentity_linear, description_linear, second_linear, img_minus, layquankm;
        CustomTextViewBold tvdesc, service_rating_digit, tvquantity, tvPrice, tvDiscountPrice, tvroundtrip, tvService_charge;
        CustomTextView short_desc, tvdesc23;
        ImageView arrow_desc;
        CheckBox cbSelect;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_bottom_foster = itemView.findViewById(R.id.iv_bottom_foster);
            img_plus = itemView.findViewById(R.id.img_plus);
            img_minus = itemView.findViewById(R.id.img_minus);
            layquankm = itemView.findViewById(R.id.layquankm);
            quentity_linear = itemView.findViewById(R.id.quentity_linear);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = (CustomTextViewBold) itemView.findViewById(R.id.tvPrice);
            tvquantity = (CustomTextViewBold) itemView.findViewById(R.id.tvservicequantity);
            tvDiscountPrice = (CustomTextViewBold) itemView.findViewById(R.id.tvDiscountPrice);
            description_linear = itemView.findViewById(R.id.read_more_txt);
            descptionreadmore = itemView.findViewById(R.id.descptionreadmore);
            second_linear = itemView.findViewById(R.id.second_linear);
            tvdesc = itemView.findViewById(R.id.tvdesc2);
            short_desc = itemView.findViewById(R.id.short_desc);
            service_rating_digit = itemView.findViewById(R.id.service_rating_digit);
            tvdesc23 = itemView.findViewById(R.id.tvdesc23);
            arrow_desc = itemView.findViewById(R.id.arrow_desc);
            rel_view = itemView.findViewById(R.id.rel_view);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            tvroundtrip = itemView.findViewById(R.id.tvroundtrip);
            tvService_charge = itemView.findViewById(R.id.tvService_charge);

        }
    }
}