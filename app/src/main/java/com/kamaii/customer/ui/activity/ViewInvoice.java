package com.kamaii.customer.ui.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.kamaii.customer.DTO.HistoryDTO;
import com.kamaii.customer.DTO.ProductDTO;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.R;
import com.kamaii.customer.https.HttpsRequest;
import com.kamaii.customer.interfacess.Consts;
import com.kamaii.customer.interfacess.Helper;
import com.kamaii.customer.preferences.SharedPrefrence;
import com.kamaii.customer.ui.adapter.AdapterInvoiceService;
import com.kamaii.customer.ui.fragment.PaidFrag;
import com.kamaii.customer.utils.CustomTextView;
import com.kamaii.customer.utils.CustomTextViewBold;
import com.kamaii.customer.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewInvoice extends AppCompatActivity {

    private HistoryDTO historyDTO;
    private Context mContext;
    private Bitmap bitmap;
    double totalamount = 0, artistamount = 0, commisionamount = 0;
    private GridLayoutManager gridLayoutManager;
    AdapterInvoiceService adapterInvoiceService;
    private ArrayList<ProductDTO> productDTOArrayList;
    private String booking_id;
    private String TAG = PaidFrag.class.getSimpleName();
    private ArrayList<HistoryDTO> historyDTOList;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    CustomTextViewBold servicecharge;
    String s_charge = "";
    ImageView ivCross, ivProfile;
    RecyclerView rv_cart;
    CustomTextView tvDate, tvInvoiceId, tvServiceType, etAddressSelectsource, etAddressSelectdesti, tvptype, serviceTxt;
    CustomTextViewBold tvName, tvPrice, tvSubtotal, discount_txt, discount_digit_txt, tvTotal, serviceDigitTxt1, btnPdf;
    LinearLayout llLocation,
            lldestiLocation;
    View view4;
    RelativeLayout service_charge_relative;
    ProgressDialog pd;
    String dirpath;
    File pictureFileDir,pictureFileDir2;
    String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        mContext = ViewInvoice.this;
        pd = new ProgressDialog(ViewInvoice.this);
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        servicecharge = findViewById(R.id.service_digit_txt1);
        if (getIntent().hasExtra(Consts.BOOKING_ID)) {
            booking_id = getIntent().getStringExtra(Consts.BOOKING_ID);
        }
        getUiAction();

    }

    private void getUiAction() {
        service_charge_relative = findViewById(R.id.service_charge_relative);
        ivCross = findViewById(R.id.ivCross);
        ivProfile = findViewById(R.id.ivProfile);
        rv_cart = findViewById(R.id.rv_cart);
        tvDate = findViewById(R.id.tvDate);
        tvInvoiceId = findViewById(R.id.tvInvoiceId);
        llLocation = findViewById(R.id.llLocation);
        lldestiLocation = findViewById(R.id.lldestiLocation);
        view4 = findViewById(R.id.view4);
        tvName = findViewById(R.id.tvName);
        tvServiceType = findViewById(R.id.tvServiceType);
        tvPrice = findViewById(R.id.tvPrice);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        etAddressSelectsource = findViewById(R.id.etAddressSelectsource);
        etAddressSelectdesti = findViewById(R.id.etAddressSelectdesti);
        tvptype = findViewById(R.id.tvptype);
        tvTotal = findViewById(R.id.tvTotal);
        serviceTxt = findViewById(R.id.service_txt);
        discount_digit_txt = findViewById(R.id.discount_digit_txt);
        discount_txt = findViewById(R.id.discount_txt);
        serviceDigitTxt1 = findViewById(R.id.service_digit_txt1);
        btnPdf = findViewById(R.id.btnPdf);
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getHistroy();
    }

    public void setUiAction() {

        Log.e("SHIAVASKSJKJSHJKHS", historyDTO.getInvoice_id());
        Glide.with(mContext).
                load(historyDTO.getArtistImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfile);
        gridLayoutManager = new GridLayoutManager(ViewInvoice.this, 1);
        productDTOArrayList = historyDTO.getProduct();
        adapterInvoiceService = new AdapterInvoiceService(mContext, productDTOArrayList, "0");
        rv_cart.setLayoutManager(gridLayoutManager);
        rv_cart.setAdapter(adapterInvoiceService);

        if (historyDTO.getRider_order().equals("0")) {
            service_charge_relative.setVisibility(View.GONE);
        } else {
            service_charge_relative.setVisibility(View.VISIBLE);
        }
        tvDate.setText(ProjectUtils.changeDateFormate1(historyDTO.getBooking_date()) + " " + historyDTO.getBooking_time());
        tvInvoiceId.setText(historyDTO.getInvoice_id());
        tvName.setText(ProjectUtils.getFirstLetterCapital(historyDTO.getArtistName()));
        tvServiceType.setText(historyDTO.getCategoryName());
        tvPrice.setText(Html.fromHtml("&#x20B9;" + historyDTO.getArtist_amount()));
        tvSubtotal.setText(Html.fromHtml("&#x20B9;" + historyDTO.getItemtotal()));

        if (!historyDTO.getAddress().equalsIgnoreCase("")) {
            etAddressSelectsource.setVisibility(View.VISIBLE);
            etAddressSelectsource.setText(historyDTO.getAddress());
        }

        if (!historyDTO.getDestinationaddress().equalsIgnoreCase("")) {
            etAddressSelectdesti.setVisibility(View.VISIBLE);
            etAddressSelectdesti.setText(historyDTO.getDestinationaddress());
        } else {
            etAddressSelectdesti.setVisibility(View.VISIBLE);
            etAddressSelectdesti.setText(historyDTO.getAddress());
        }

        if (historyDTO.getPayment_type().equalsIgnoreCase("0")) {
            tvptype.setText("Online Payment");
        } else if (historyDTO.getPayment_type().equalsIgnoreCase("1")) {
            tvptype.setText("Cash Payment");
        } else if (historyDTO.getPayment_type().equalsIgnoreCase("2")) {
            tvptype.setText("Wallet Payment");
        }
        totalamount = Double.parseDouble(historyDTO.getTotal_amount());
        artistamount = Double.parseDouble(historyDTO.getArtist_amount());

        try {
            commisionamount = totalamount - artistamount;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }

        DecimalFormat newFormat = new DecimalFormat("##.##");

        tvTotal.setText(Html.fromHtml("&#x20B9;" + historyDTO.getNetpay()));
        s_charge = historyDTO.getServicecharge();
        discount_txt.setText(Html.fromHtml(historyDTOList.get(0).getDiscount_txt()));
        discount_digit_txt.setText(Html.fromHtml("&#x20B9;" + historyDTOList.get(0).getDiscount_digit_txt()));

        if (productDTOArrayList.get(0).getSub_category_id().equals("242") || productDTOArrayList.get(0).getSub_category_id().equals("434")) {
            serviceTxt.setText("Driver Allowance");
            serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
        } else if (productDTOArrayList.get(0).getSub_category_id().equals("453")) {
            serviceTxt.setVisibility(View.GONE);
            serviceDigitTxt1.setVisibility(View.GONE);
        } else {
            if (s_charge.equals("0") || s_charge.equals("0.00")) {
                serviceDigitTxt1.setText("Free");
            } else {
                serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
            }
        }
        if (historyDTO.getIsmap().equals("1")) {
            llLocation.setVisibility(View.VISIBLE);
            lldestiLocation.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
        } else {
            llLocation.setVisibility(View.GONE);
            lldestiLocation.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);

        }
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SaveClick();
                // Toast.makeText(mContext, "PDF Created....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        String targetPdf = tvInvoiceId.getText().toString() + ".pdf";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File myDir = new File(Environment.getExternalStorageDirectory().toString(), "/OkyBookCusomer");


            if (!myDir.exists()) {
                boolean md = myDir.mkdirs();
                if (!md) {

                }
            }

            final File file = new File(myDir, targetPdf);
            if (file.exists()) {
                file.delete();
            }
            try {
                document.writeTo(new FileOutputStream(file));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();
        }
        document.close();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.BOOKING_ID, booking_id);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "2");
        return parms;
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

    public void getHistroy() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_INVOICE_API2, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e("response_history", "" + response.toString());
                        historyDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<HistoryDTO>>() {
                        }.getType();
                        historyDTOList = (ArrayList<HistoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        historyDTO = historyDTOList.get(0);
                        setUiAction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }


    public void SaveClick() {
        pd.setMessage("saving your image");
        pd.show();
        RelativeLayout savingLayout = (RelativeLayout) findViewById(R.id.imaging);
        File file = saveBitMap(ViewInvoice.this, savingLayout);
        if (file != null) {
            try {
                Log.e("SHIVAMKOSHTI", "1234");
                imageToPDF();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            pd.cancel();
            Log.e("SHIVAMKOSHTI", "Drawing saved to the gallery!");
        } else {
            pd.cancel();
            Log.e("SHIVAMKOSHTI", "Oops! Image could not be saved.");
        }
    }

    private File saveBitMap(Context context, View drawView) {

        pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Kamaii-Invoice");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.e("SHIVAMKOSHTI", "Can't create directory to save the image");
            return null;
        }
        filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SHIVAMKOSHTI", "There was an issue saving the image.");
        }
        return pictureFile;
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void imageToPDF() throws FileNotFoundException {
        try {

            Log.e("SHIVAMKOSHTI", "5678");

            Document document = new Document();
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();

            //pictureFileDir2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Kamaii-Invoice");
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/"+productDTOArrayList.get(0).getId()+".pdf"));
            Log.e("SHIVAMKOSHTI", "1");

            document.open();
            Log.e("SHIVAMKOSHTI", "2");

            Image img = Image.getInstance(filename);
            Log.e("SHIVAMKOSHTI", "3");

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            Log.e("SHIVAMKOSHTI", "4");

            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            Log.e("SHIVAMKOSHTI", "5");

          //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dirpath + "/"+productDTOArrayList.get(0).getId()+".pdf")));

            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }
}
