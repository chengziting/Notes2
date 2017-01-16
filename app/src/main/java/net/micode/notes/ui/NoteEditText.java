package net.micode.notes.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;
import net.micode.notes.R;

/**
 * Created by 10191042 on 1/9/2017.
 */
public class NoteEditText extends EditText {

    private static final String TAG = "NoteEditText";
    private int m_index;
    private int m_selectionStartBeforeDelete;
    private static final String SCHEME_TEL = "tel:";
    private static final String SCHEME_HTTP = "http:";
    private static final String SCHEME_EMAIL = "mailto:";

    private static final Map<String,Integer> s_schemaActionResMap = new HashMap<>();

    private OnTextViewChangeListener m_textViewChangeListener;

    static {
        s_schemaActionResMap.put(SCHEME_TEL, R.string.note_link_tel);
        s_schemaActionResMap.put(SCHEME_HTTP,R.string.note_link_web);
        s_schemaActionResMap.put(SCHEME_EMAIL,R.string.note_link_email);
    }

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_index = 0;
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_index = 0;
    }

    public NoteEditText(Context context) {
        super(context);
        m_index = 0;
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        m_index = 0;
    }

    public interface OnTextViewChangeListener{
        void onEditTestDelete(int index,String text);
        void onEditTestEnter(int index,String text);
        void onTextChange(int index,boolean hasText);
    }



    public void setIndex(int index){
        this.m_index = index;
    }

    public void setTextViewChangeListener(OnTextViewChangeListener listener){
        this.m_textViewChangeListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            int x = (int)event.getX();
            int y = (int)event.getY();
            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();
            x += getScrollX();
            y += getScrollY();

            Layout layout = getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line,x);
            Selection.setSelection(getText(),off);
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
                if(m_textViewChangeListener != null){
                    return false;
                }
                break;
            case KeyEvent.KEYCODE_DEL:
                m_selectionStartBeforeDelete = getSelectionStart();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DEL:
                if(m_textViewChangeListener != null){
                    if(0 == m_selectionStartBeforeDelete && m_index != 0){
                        m_textViewChangeListener.onEditTestDelete(m_index,getText().toString());
                        return true;
                    }
                }
                else{
                    Log.d(TAG, "OnTextViewChangeListener was not seted");
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                if(m_textViewChangeListener != null){
                    int selectionStart = getSelectionStart();
                    String text = getText().subSequence(selectionStart,length()).toString();
                    setText(getText().subSequence(0,selectionStart));
                    m_textViewChangeListener.onEditTestEnter(m_index+1,text);
                }
                else{
                    Log.d(TAG, "OnTextViewChangeListener was not seted");
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (m_textViewChangeListener != null) {
            if (!focused && TextUtils.isEmpty(getText())) {
                m_textViewChangeListener.onTextChange(m_index, false);
            } else {
                m_textViewChangeListener.onTextChange(m_index, true);
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        if (getText() instanceof Spanned) {
            int selStart = getSelectionStart();
            int selEnd = getSelectionEnd();

            int min = Math.min(selStart, selEnd);
            int max = Math.max(selStart, selEnd);

            final URLSpan[] urls = ((Spanned) getText()).getSpans(min, max, URLSpan.class);
            if (urls.length == 1) {
                int defaultResId = 0;
                for(String schema: s_schemaActionResMap.keySet()) {
                    if(urls[0].getURL().indexOf(schema) >= 0) {
                        defaultResId = s_schemaActionResMap.get(schema);
                        break;
                    }
                }

                if (defaultResId == 0) {
                    defaultResId = R.string.note_link_other;
                }

                menu.add(0, 0, 0, defaultResId).setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                // goto a new intent
                                urls[0].onClick(NoteEditText.this);
                                return true;
                            }
                        });
            }
        }
        super.onCreateContextMenu(menu);
    }
}
