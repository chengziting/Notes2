package net.micode.notes.ui;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.micode.notes.R;
import net.micode.notes.data.Notes;

public class NotesListActivity extends AppCompatActivity {

//
    private static final int FOLDER_NOTE_LIST_QUERY_TOKEN   = 0;
    private static final int FOLDER_LIST_QUERY_TOKEN        = 1;
    private static final int MENU_FOLDER_DELTE              = 0;
    private static final int MENU_FOLDER_VIEW               = 1;
    private static final int MENU_FOLDER_CHANGE_NAME        = 2;
    private static final String PREFERENCE_AND_INTRODUCTION = "net.micode.notes.introduction";

    private final static int REQUEST_CODE_OPEN_NODE = 102;
    private final static int REQUEST_CODE_NEW_NODE = 103;

    private enum ListEditState{
        NOTE_LIST,
        SUB_FOLDER,
        CALL_RECORD_FOLDER
    };

    private long        m_currentFolderId;

    private ContentResolver                 m_contentResolver;
    private BackgroundQueryHandler          m_backgroundQueryHandler;
    private ListView                        m_notesListView;
    private Button                          m_addNewNote;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        initResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initResources(){
        m_contentResolver = this.getContentResolver();
        m_backgroundQueryHandler = new BackgroundQueryHandler(m_contentResolver);
        m_currentFolderId  = Notes.ID_ROOT_FOLDER;
        m_notesListView    = (ListView)findViewById(R.id.notes_list);
        m_notesListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.note_list_footer,null,false));
        m_notesListView.setOnItemClickListener(new OnListItemClickListener());
        m_notesListView.setOnItemLongClickListener(new OnListItemLongClickListener());
        m_addNewNote = (Button)findViewById(R.id.btn_new_note);
        m_addNewNote.setOnClickListener(new OnButtonClickListener());

    }

    private final class BackgroundQueryHandler extends AsyncQueryHandler{

        public BackgroundQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

            switch (token){
                case FOLDER_NOTE_LIST_QUERY_TOKEN:

                    break;
                case FOLDER_LIST_QUERY_TOKEN:

                    break;
                default:
                    return;
            }
        }
    }

    private void createNewNote(){
        Intent intent = new Intent(this,NoteEditActivity.class);
        intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
        intent.putExtra(Notes.INTENT_EXTRA_FOLDER_ID,m_currentFolderId);
        this.startActivityForResult(intent,REQUEST_CODE_NEW_NODE);
//        this.startActivity(intent);
    }

    private class OnListItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    private class OnListItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    }

    private class OnButtonClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_new_note:
                    createNewNote();
                    break;
            }
        }
    }

}
