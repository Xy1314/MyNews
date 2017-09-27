package text.bwie.com.mynews;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class autoActivity extends AppCompatActivity {
    int time=3;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            time--;
            handler.sendEmptyMessageDelayed(3,3000);
            if (time == 0) {
                startActivity(new Intent(autoActivity.this, MainActivity.class));
            }else  if(time<0){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        handler.sendEmptyMessageDelayed(1,1000);

    }

    public void auto_Btn(View view) {
        time=-1;

        handler.sendEmptyMessage(0);
        startActivity(new Intent(autoActivity.this, MainActivity.class));


    }
}
