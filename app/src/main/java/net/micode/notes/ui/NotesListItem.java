package net.micode.notes.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 10191042 on 1/6/2017.
 */
public class NotesListItem extends LinearLayout {

    private ImageView m_alert;
    private TextView m_title;
    private TextView m_time;
    private TextView m_callName;


    public NotesListItem(Context context) {
        super(context);
    }
}
