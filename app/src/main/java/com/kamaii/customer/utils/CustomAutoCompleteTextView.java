package com.kamaii.customer.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by WebKnight Infosystem on 01/01/19.
 */
public class CustomAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView
{

    public CustomAutoCompleteTextView(Context context) {

        super(context);
        applyCustomFont(context);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Poppins-Regular.otf", context);
        setTypeface(customFont);
    }
}
