package example.com.qqlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText et_qqnumber;
    private EditText et_passwd;
    private CheckBox cb_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_qqnumber = (EditText) findViewById(R.id.et_qqnumber);
        et_passwd = (EditText) findViewById(R.id.et_passwd);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        restoreInfo();
    }

    /**
     * 根据原来保存的文件信息，把QQ号码和密码信息显示到界面
     */
    private void restoreInfo() {
        // File file = new File(this.getFilesDir(), "info.txt");
        // 用openFileInput就不需要判断file.exists() && file.length() > 0了
        // 函数封装会处理的
        // 自动关流写法要jdk1.7以上
        try (//FileInputStream fis = new FileInputStream(file);
             FileInputStream fis = openFileInput("info.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(fis));) {
            String line = br.readLine();
            if (line != null){
                String[] info = line.split("##");
                String qq = info[0];
                String pwd = info[1];
                et_qqnumber.setText(qq);
                et_qqnumber.setSelection(qq.length());
                et_passwd.setText(pwd);
                cb_remember.setChecked(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录按钮的点击事件
     *
     * @param view
     */
    public void login(View view) {
        String qq = et_qqnumber.getText().toString().trim();
        String passwd = et_passwd.getText().toString().trim();
        if (TextUtils.isEmpty(qq) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 是否需要记住密码
            try (
                    //FileOutputStream fos = new FileOutputStream(new File(this.getFilesDir(), "info.txt"));
                    FileOutputStream fos = openFileOutput("info.txt", MODE_PRIVATE);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));) {
                // 登录操作，模拟登陆，数据应该提交给服务器比较是否正确
                if ("10000".equals(qq) && "123456".equals(passwd)) {
                    Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                    if (cb_remember.isChecked()) {
                        // 被选中状态，需要记住密码
                        String info = qq + "##" + passwd;
                        //fos.write(info.getBytes());
                        writer.write(info);
                    } else {
                        writer.write(""); // 空文件
                    }
                } else {
                    Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
