package myapplication.sqlite;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    myDBHelper myHelper;
    EditText edtName, edtNum, edtNameResult, edtNumResult;
    Button btnInit, btnInput, btnFind, btnRe, btnRm;
    SQLiteDatabase sqlDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("가수 그룹 관리 DB");

        edtName = (EditText) findViewById(R.id.edtName);
        edtNum = (EditText) findViewById(R.id.edtNum);
        edtNameResult = (EditText) findViewById(R.id.edtNameResult);
        edtNumResult = (EditText) findViewById(R.id.edtNumResult);
        btnInit = (Button) findViewById(R.id.btnInit);
        btnInput = (Button) findViewById(R.id.btnInput);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnRe = (Button) findViewById(R.id.btnRe);
        btnRm = (Button) findViewById(R.id.btnRm);

        myHelper = new myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
            }
        });

        btnInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES ( '"
                        + edtName.getText().toString() + "' , "
                        + edtNum.getText().toString() + ");");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

                String strNames = "그룹 이름" + "\r\n" + "------" + "\r\n";
                String strNums = "인원" + "\r\n" + "------" + "\r\n";

                while (cursor.moveToNext()){
                    strNames += cursor.getString(0) + "\r\n";
                    strNums += cursor.getString(1) + "\r\n";
                }

                edtNameResult.setText(strNames);
                edtNumResult.setText(strNums);

                cursor.close();
                sqlDB.close();
            }
        });
        btnRe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE groupTBL SET gNumber = " + edtNum.getText().toString()
                        + " WHERE gName = '" + edtName.getText().toString() + "';");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "수정됨", Toast.LENGTH_SHORT).show();
            }
        });
        btnRm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '" + edtName.getText().toString() + "';");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "삭제됨", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
