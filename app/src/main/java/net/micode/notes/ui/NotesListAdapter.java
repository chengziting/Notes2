package net.micode.notes.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.HashMap;

/**
 * Created by 10191042 on 1/6/2017.
 */
public class NotesListAdapter extends CursorAdapter {

    private static final String TAG = "NotesListAdapter";
    private Context m_context;
    private HashMap<Integer,Boolean> m_selectedIndex;
    private int m_notesCount;
    private boolean m_choiceMode;

    public static class AppWidgetAttribute{
        public static int widgetId;
        public static int widgetType;
    };

    public NotesListAdapter(Context context) {
        super(context,null);
        m_selectedIndex = new HashMap<>();
        m_context = context;
        m_notesCount = 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new NotesListItem(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(view instanceof NotesListItem){

        }
    }
}
