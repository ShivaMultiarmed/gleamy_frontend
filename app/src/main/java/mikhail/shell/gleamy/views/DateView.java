package mikhail.shell.gleamy.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.DateViewBinding;

public class DateView extends LinearLayout {
    private final static String[] months;
    static {
        months = new String[]{
                "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        };
    }
    private DateViewBinding B;

    public DateView(Context context, LocalDate date, boolean withYear) {
        super(context);
        init(null, 0, date, withYear);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(AttributeSet attrs, int defStyle, LocalDate date, boolean withYear) {
        B = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.date_view,
                this,
                true);
        B.setDateString(createDateString(date, withYear));
    }
    private String createDateString(LocalDate date, boolean withYear)
    {
        StringBuilder builder = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.append(date.getDayOfMonth());
            builder.append(" " +months[date.getMonthValue()-1]);
            if (withYear)
                builder.append(" " + date.getYear()+ " года");
        }
        return builder.toString();

    }

}