package mikhail.shell.gleamy.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.OptionViewBinding;

public class OptionView extends LinearLayout {
    private OptionViewBinding B;
    public OptionView(Context context) {
        super(context);
        init();
    }
    public OptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private void init()
    {
        B = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.option_view,
                this,
                true
        );
    }
    public void setText(String text)
    {
        B.optionText.setText(text);
    }
}