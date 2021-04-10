package com.kamaii.customer.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserBooking;
import com.kamaii.customer.R;
import com.kamaii.customer.api.apiClient;
import com.kamaii.customer.api.apiRest;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.interfacess.OnSelectedItemListener;
import com.kamaii.customer.interfacess.SetonItemPlusListener;
import com.kamaii.customer.ui.activity.BaseActivity;
import com.kamaii.customer.ui.activity.Booking;
import com.kamaii.customer.ui.activity.ViewAddressActivity;
import com.kamaii.customer.ui.models.CartModel;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.kamaii.customer.interfacess.Consts.ROUTE_PATH;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Booking booking;
    private Context context;
    ArrayList<String> productIdList;
    private ArrayList<ProductDTO> productDTOList;
    private ArrayList<String> Qty_array;
    private OnSelectedItemListener onSelectedItemListener;
    private SetonItemPlusListener setonItemPlusListener;
    private String TAG = Booking.class.getSimpleName();
    private ArrayList<CartModel> userBookingList;
    String SHIP_charge = "";
    String SUB_TOTAL = "";
    String TOTAL = "";
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    int maxmimumvalue = 0;
    int servalue = 0, servalueminus = 0;
    private Dialog dialogpriview;
    EditText etquanity;
    CustomTextViewBold tvYesPass, llltxtamount;
    Boolean isCheck = true;
    ImageView tvNoPass;
    String uid = "";
    HashMap<String, String> cartHashmap;
    double discounted_total = 0.0;
    double sub_total = 0.0;

    public AdapterCart(Context context, ArrayList<ProductDTO> productDTOList, OnSelectedItemListener onSelectedItemListener, SetonItemPlusListener setonItemPlusListener, String uid, HashMap<String, String> cartHashmap) {
        this.context = context;
        booking = (Booking) context;
        this.productDTOList = productDTOList;
        this.uid = uid;
        this.cartHashmap = cartHashmap;
        productIdList = new ArrayList<>();
        Qty_array = new ArrayList<>();
        this.onSelectedItemListener = onSelectedItemListener;
        this.setonItemPlusListener = setonItemPlusListener;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);


        return new MyViewHolder(itemView);
    }

    public void NotifyAll(ArrayList<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
        notifyDataSetChanged();
    }

    public void NotifyAll_qty(ArrayList<ProductDTO> productDTOList, HashMap<String, String> cartHashmap) {
        this.productDTOList = productDTOList;
        this.cartHashmap = cartHashmap;
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


    public void bookArtist(String quentity) {

        new HttpsRequest(Consts.ADD_TO_CART_TEST_API, paramsBookingOp, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {
                        String s = response.toString();
                        Log.e("GETDATA_RES2_2", "" + s);
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CartModel>>() {
                        }.getType();
                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        SHIP_charge = jsonObject.getString("shipping_charges");
                        SUB_TOTAL = jsonObject.getString("subtotal");
                        TOTAL = jsonObject.getString("total_amount");


                        double discountprice = (Double.parseDouble(SUB_TOTAL) * Consts.DISCOUNT_RATE / 100) + Double.parseDouble(SUB_TOTAL);

                        booking.discounted_total_digits.setText(String.valueOf(discountprice));
                        booking.discounted_total_digits.setPaintFlags(booking.discounted_total_digits.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        ((Booking) context).changetotalprice(TOTAL, SUB_TOTAL, SHIP_charge);


                        Log.e("SUB_TOTAL", "" + SUB_TOTAL);
                        Log.d("userBookingList_TOTAL", "" + jsonArray.toString());
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


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        dialogpriview = new Dialog(context, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_quantity);
        dialogpriview.setCancelable(false);
        etquanity = dialogpriview.findViewById(R.id.etquanity);
        tvYesPass = dialogpriview.findViewById(R.id.tvYesPass);
        tvNoPass = dialogpriview.findViewById(R.id.tvNoPass);
        llltxtamount = dialogpriview.findViewById(R.id.llltxtamount);

        productIdList.add(productDTOList.get(position).getId());
        if (cartHashmap != null) {

            Qty_array.add(cartHashmap.get(productDTOList.get(position).getId()));
            holder.tvquantity.setText(cartHashmap.get(productDTOList.get(position).getId()));
        }

        final int pos = position;
        holder.tvPrice.setText(Html.fromHtml("&#x20B9;" + productDTOList.get(pos).getPrice()));

        holder.tvProductName.setText(productDTOList.get(pos).getProduct_name());
        holder.tvProductName.setText(productDTOList.get(pos).getProduct_name());
        if (productDTOList.get(pos).getVehicle_number().equalsIgnoreCase("")) {
            holder.tvvehiclenumber.setVisibility(View.GONE);
        } else {
            holder.tvvehiclenumber.setVisibility(View.VISIBLE);
            holder.tvvehiclenumber.setText(productDTOList.get(pos).getVehicle_number());
        }


        if (checkarss(ViewAddressActivity.catid)) {


            if (ViewAddressActivity.sub_category_idd.equalsIgnoreCase("453") || ViewAddressActivity.sub_category_idd.equalsIgnoreCase("455")) {
                RelativeLayout relativeLayout1, relativeLayout2;
                relativeLayout1 = booking.findViewById(R.id.service_charge_relative);
                relativeLayout2 = booking.findViewById(R.id.subtotal_relative);
                relativeLayout1.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.GONE);
                holder.tmp_recycle_txtview.setVisibility(View.GONE);

            } else if (ViewAddressActivity.sub_category_idd.equals("242") || ViewAddressActivity.sub_category_idd.equals("434")) {
                CustomTextView tx = booking.findViewById(R.id.service_txt);
                holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOList.get(position).getService_charge());
            } else {

                if (!productDTOList.get(position).getService_charge().equalsIgnoreCase("")) {
                    holder.tvService_charge.setVisibility(View.GONE);

                    holder.tmp_recycle_txtview.setVisibility(View.GONE);

                } else {

                    holder.tvService_charge.setVisibility(View.GONE);
                }

            }

            holder.laydelete.setVisibility(View.GONE);
            holder.minus_linear_child.setVisibility(View.GONE);
            holder.price_relative.setVisibility(View.GONE);
            holder.plus_linear_child.setVisibility(View.GONE);
            holder.tvquantitydays.setVisibility(View.VISIBLE);
            holder.tvquantity.setVisibility(View.VISIBLE);
            holder.quentity_linear_child.setBackground(context.getResources().getDrawable(R.drawable.set_km_background));
            holder.tvService_charge.setVisibility(View.GONE);
        } else {

            if (position == 0) {
                Log.e("POSITION", "" + pos);
                holder.tmp_recycle_txtview.setVisibility(View.GONE);
            }
            if (!productDTOList.get(position).getService_charge().equalsIgnoreCase("")) {
                holder.tvService_charge.setText("Service Charge:" + " " + productDTOList.get(pos).getService_charge());
            }
            if (!productDTOList.get(position).getQuantitydays().equalsIgnoreCase("")) {
                holder.tvquantitydays.setText(cartHashmap.get(productDTOList.get(position).getId()));

                if (cartHashmap.containsKey(productDTOList.get(position).getId())) {
                    double total_a = Double.parseDouble(cartHashmap.get(productDTOList.get(position).getId())) * Double.parseDouble(productDTOList.get(position).getPrice());
                    sub_total = sub_total += total_a;

                    holder.total_count.setText(Html.fromHtml("&#x20B9;" + total_a));
                    double discountprice = (double) ((total_a * Consts.DISCOUNT_RATE / 100) + total_a);
                    discounted_total = discounted_total += discountprice;
                    booking.discounted_rupee_symbol.setText(Html.fromHtml("&#x20B9;"));
                    booking.discounted_total_digits.setText(String.valueOf(discounted_total));
                    booking.discounted_total_digits.setPaintFlags(holder.stripe_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    holder.stripe_price.setText(Html.fromHtml("&#x20B9;" + String.valueOf(discountprice)));
                    holder.stripe_price.setPaintFlags(holder.stripe_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }

        }
        holder.tvService_charge.setVisibility(View.GONE);
        holder.tvdesc.setVisibility(View.GONE);
        if (productDTOList.get(position).getDescription() == null || productDTOList.get(position).getDescription().isEmpty() || productDTOList.get(position).getDescription().equals("")) {
            holder.tvdesc.setVisibility(View.GONE);
        } else {
            holder.tvdesc.setText(Html.fromHtml(productDTOList.get(position).getDescription()));
        }

        if (!productDTOList.get(position).getQuantitydays().equalsIgnoreCase("")) {

            if (productDTOList.get(position).getQuantitydays().equalsIgnoreCase("0")) {
                holder.tvquantitydays.setText("Qty");
            } else {
                holder.tvquantitydays.setVisibility(View.INVISIBLE);
            }

        }

        holder.img_delete_cart_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItemListener.setOnClick("", pos);
            }
        });


        tvNoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogpriview.dismiss();

            }
        });
        holder.tvquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogpriview.show();
                etquanity.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etquanity, InputMethodManager.SHOW_IMPLICIT);
                etquanity.setText(holder.tvquantity.getText().toString());

                if (!productDTOList.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
                    maxmimumvalue = Integer.parseInt(productDTOList.get(position).getMaxmiumvalue());
                    llltxtamount.setText("Maximum Qty:" + " " + String.valueOf(maxmimumvalue));
                }

                tvYesPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!productDTOList.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
                            maxmimumvalue = Integer.parseInt(productDTOList.get(position).getMaxmiumvalue());
                        }


                        if (!etquanity.getText().toString().equalsIgnoreCase("")) {
                            int quantity = Integer.parseInt(etquanity.getText().toString());


                            if (quantity <= maxmimumvalue) {
                                double price = Double.parseDouble(productDTOList.get(position).getPrice());
                                int prevousqty = Integer.parseInt(holder.tvquantity.getText().toString());
                                double totalprivous = price * prevousqty;

                                double discountprice = (Double.parseDouble(productDTOList.get(pos).getPrice()) * Consts.DISCOUNT_RATE / 100) + Double.parseDouble(productDTOList.get(pos).getPrice());
                                double disc_total = quantity * discountprice;

                                int servicecharge = 0;
                                if (!productDTOList.get(position).getService_charge().equalsIgnoreCase("")) {
                                    servicecharge = Integer.parseInt(productDTOList.get(position).getService_charge());
                                }

                                double total = price * quantity;
                                double maintotal = total - totalprivous;
                                holder.total_count.setText(Html.fromHtml("&#x20B9;" + String.valueOf(total)));

                                booking.discounted_rupee_symbol.setText(Html.fromHtml("&#x20B9;"));
                              holder.stripe_price.setText(Html.fromHtml("&#x20B9;" + String.valueOf(disc_total)));

                                holder.stripe_price.setPaintFlags(holder.stripe_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                holder.tvquantity.setText(String.valueOf(quantity));
                                Log.e("TOTAL123", "" + TOTAL);

                                paramsBookingOp.clear();
                                paramsBookingOp.put("ARTIST_ID", productDTOList.get(pos).getUser_id());
                                paramsBookingOp.put("PRICE", productDTOList.get(pos).getPrice());
                                paramsBookingOp.put("SERVICE_ID", productDTOList.get(pos).getId());
                                paramsBookingOp.put("UID", uid);
                                paramsBookingOp.put("QUANTITYDAYS", holder.tvquantity.getText().toString());

                                bookArtist(String.valueOf(quantity));
                                booking.update_qty_booking(String.valueOf(quantity), productDTOList.get(pos).getId());

                                dialogpriview.dismiss();


                            } else {
                                showSickbar("Not Available");
                            }
                        } else {
                            showSickbar("Not Available");
                        }

                    }
                });
            }
        });


        holder.minus_linear_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(holder.tvquantity.getText().toString());
                if (quantity == 1) {

                    paramsBookingOp.clear();

                    for (int i = 0; i < productDTOList.size(); i++) {
                        if (i != pos) {
                            paramsBookingOp.put("ARTIST_ID" + i, productDTOList.get(i).getUser_id());
                            paramsBookingOp.put("PRICE" + i, productDTOList.get(i).getPrice());
                            paramsBookingOp.put("SERVICE_ID" + i, productDTOList.get(i).getId());
                            paramsBookingOp.put("UID" + i, uid);

                            productDTOList.get(i).setQuantityvalue(holder.tvquantity.getText().toString());
                            Log.d(TAG, "onClick: " + cartHashmap.get(productDTOList.get(i).getId()));
                            paramsBookingOp.put("QUANTITYDAYS" + i, cartHashmap.get(productDTOList.get(i).getId()));
                        }
                    }
                    Log.d(TAG, "onClick: shivam " + paramsBookingOp.toString());
                    booking.remove_qty_booking(String.valueOf(quantity), productDTOList.get(pos).getId(), pos);
                    bookArtist(String.valueOf(quantity));
                }
                if (quantity > 1) {
                    quantity--;

                    double price1 = Double.parseDouble(productDTOList.get(pos).getPrice());
                    double total = price1 * quantity;

                    holder.total_count.setText(Html.fromHtml("&#x20B9;" + total));

                    double discountprice = (Double.parseDouble(productDTOList.get(pos).getPrice()) * Consts.DISCOUNT_RATE / 100) + Double.parseDouble(productDTOList.get(pos).getPrice());
                    double disc_total = quantity * discountprice;

                    booking.discounted_rupee_symbol.setText(Html.fromHtml("&#x20B9;"));

                    holder.stripe_price.setText(Html.fromHtml("&#x20B9;" + String.valueOf(disc_total)));
                    holder.stripe_price.setPaintFlags(holder.stripe_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    holder.tvquantity.setText(String.valueOf(quantity));
                    paramsBookingOp.clear();

                    for (int i = 0; i < productDTOList.size(); i++) {

                        paramsBookingOp.put("ARTIST_ID" + i, productDTOList.get(i).getUser_id());
                        paramsBookingOp.put("PRICE" + i, productDTOList.get(i).getPrice());
                        paramsBookingOp.put("SERVICE_ID" + i, productDTOList.get(i).getId());
                        paramsBookingOp.put("UID" + i, uid);
                        productDTOList.get(i).setQuantityvalue(holder.tvquantity.getText().toString());
                        if (i == position) {
                            Qty_array.set(i, holder.tvquantity.getText().toString());
                            paramsBookingOp.put("QUANTITYDAYS" + i, holder.tvquantity.getText().toString());

                        } else {
                            paramsBookingOp.put("QUANTITYDAYS" + i, Qty_array.get(i));

                        }
                    }

                    bookArtist(String.valueOf(quantity));
                    booking.update_qty_booking(String.valueOf(quantity), productDTOList.get(pos).getId());
                    int servicecharge = 0;
                    if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                        servicecharge = Integer.parseInt(productDTOList.get(pos).getService_charge());
                    } else {

                    }
                    double price = Double.parseDouble(productDTOList.get(pos).getPrice());


                    setonItemPlusListener.Click(0, 0, 0, pos, quantity);

                } else {

                }


            }
        });


        holder.plus_linear_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.tvquantity.getText().toString());

                if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
                    maxmimumvalue = Integer.parseInt(productDTOList.get(pos).getMaxmiumvalue());
                }
                if (quantity < maxmimumvalue) {
                    quantity++;

                    holder.tvquantity.setText(String.valueOf(quantity));
                    double price1 = Double.parseDouble(productDTOList.get(pos).getPrice());

                    double total = price1 * quantity;
                    holder.total_count.setText(Html.fromHtml("&#x20B9;" + total));

                    double discountprice = (Double.parseDouble(productDTOList.get(pos).getPrice()) * Consts.DISCOUNT_RATE / 100) + Double.parseDouble(productDTOList.get(pos).getPrice());
                    double disc_total = quantity * discountprice;
                    booking.discounted_rupee_symbol.setText(Html.fromHtml("&#x20B9;"));

                    holder.stripe_price.setText(Html.fromHtml("&#x20B9;" + String.valueOf(disc_total)));
                    holder.stripe_price.setPaintFlags(holder.stripe_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    Log.e("artist_id", "2->" + productDTOList.get(position).getUser_id());
                    Log.e("price", "3->" + productDTOList.get(position).getPrice());
                    Log.e("service_id", "4->" + productDTOList.get(position).getId());
                    paramsBookingOp.clear();

                    for (int i = 0; i < productDTOList.size(); i++) {
                        paramsBookingOp.put("ARTIST_ID" + i, productDTOList.get(i).getUser_id());

                        paramsBookingOp.put("PRICE" + i, productDTOList.get(i).getPrice());

                        paramsBookingOp.put("SERVICE_ID" + i, productDTOList.get(i).getId());
                        paramsBookingOp.put("UID" + i, uid);

                        if (i == position) {
                            Qty_array.set(i, holder.tvquantity.getText().toString());
                            paramsBookingOp.put("QUANTITYDAYS" + i, holder.tvquantity.getText().toString());

                        } else {
                            paramsBookingOp.put("QUANTITYDAYS" + i, Qty_array.get(i));

                        }
                    }

                    bookArtist(String.valueOf(quantity));

                    booking.update_qty_booking(String.valueOf(quantity), productDTOList.get(pos).getId());
                    int servicecharge = 0;
                    if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
                        servicecharge = Integer.parseInt(productDTOList.get(pos).getService_charge());
                    }
                    double price = Double.parseDouble(productDTOList.get(pos).getPrice());
                                    } else {
                    showSickbar("Not Available");
                }

            }
        });

        if (!ViewAddressActivity.mainkm.equalsIgnoreCase("")) {
            holder.tvquantity.setText(productDTOList.get(pos).getQuantityvalue());
            holder.tvquantity.setEnabled(false);
            holder.tvquantity.setClickable(false);
            holder.plus_linear_child.setClickable(false);
            holder.plus_linear_child.setClickable(false);
            holder.minus_linear_child.setClickable(false);
            holder.minus_linear_child.setClickable(false);


            if (ViewAddressActivity.sub_category_idd.equalsIgnoreCase("453") || ViewAddressActivity.sub_category_idd.equalsIgnoreCase("242")) {
                holder.tvroundtrip.setVisibility(View.GONE);
            } else {

                if (checkarss(ViewAddressActivity.catid)) {
                    if (productDTOList.get(position).getRoundtrip().equalsIgnoreCase("1")) {
                        holder.tvroundtrip.setVisibility(View.INVISIBLE);
                        holder.tvroundtrip.setText("Round Trip");
                    } else if (productDTOList.get(position).getRoundtrip().equalsIgnoreCase("0")) {
                        holder.tvroundtrip.setVisibility(View.INVISIBLE);
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

    }


    public void showSickbar(String msg) {
        booking.sickbar(msg);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setvisiblity(TextView textView, boolean isvisible) {
        textView.setVisibility(isvisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout rel_view, layquankm, laydelete, minus_linear_child, quentity_linear_child;
        public RelativeLayout plus_linear_child, price_relative;
        CustomTextView stripe_price;
        public CustomTextViewBold tvPrice, tvtotal, total_count, tvProductName, tvService_charge, tvquantitydays, tvquantity, tvroundtrip, tvvehiclenumber;
        public ImageView img_delete_cart_data, img_plus, img_minus;
        TextView tmp_recycle_txtview;
        CustomTextView tvdesc, tvmore;


        public MyViewHolder(View view) {
            super(view);


            tvProductName = view.findViewById(R.id.tvProductName);
            total_count = view.findViewById(R.id.total_count);
            tmp_recycle_txtview = view.findViewById(R.id.tmp_recycle_txtview);
            stripe_price = view.findViewById(R.id.price_stripe);
            quentity_linear_child = view.findViewById(R.id.quentity_linear_child);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            rel_view = view.findViewById(R.id.rel_view);
            tvService_charge = view.findViewById(R.id.tvService_charge);
            img_delete_cart_data = view.findViewById(R.id.img_delete_cart_data);
            tvtotal = view.findViewById(R.id.tvtotal);
            tvquantity = view.findViewById(R.id.tvquantity);
            img_plus = view.findViewById(R.id.img_plus);
            minus_linear_child = view.findViewById(R.id.minus_linear_child);
            plus_linear_child = view.findViewById(R.id.plus_linear_child);
            price_relative = view.findViewById(R.id.price_relative);
            img_minus = view.findViewById(R.id.img_minus);
            tvquantitydays = view.findViewById(R.id.tvquantitydays);
            tvroundtrip = view.findViewById(R.id.tvroundtrip);
            layquankm = view.findViewById(R.id.layquankm);
            laydelete = view.findViewById(R.id.laydelete);
            tvdesc = view.findViewById(R.id.tvdesc);
            tvmore = view.findViewById(R.id.tvmore);
            tvvehiclenumber = view.findViewById(R.id.tvvehiclenumber);

        }
    }


}
