package bupt.androidsipchat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import bupt.androidsipchat.datestruct.UserInformation;
import bupt.androidsipchat.service.MessageService;

public class LoginActivity extends AppCompatActivity {

    boolean isLoginFront = true;
    CardView login_card;
    CardView register_card;

    CardFrontFragment cardFrontFragment;
    CardBackFragment cardBackFragment;

    private MessageService messageService;

    public boolean isLogin = false;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.DataBinder dataBinder = (MessageService.DataBinder) service;
            messageService = dataBinder.getService();

            initLoginListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_login_register);
        cardFrontFragment = new CardFrontFragment();

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.lanuch_login_register, cardFrontFragment)
                    .commit();
        }

        bindService(new Intent(LoginActivity.this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);

    }


    private void initLoginListener() {
        cardBackFragment = new CardBackFragment();

        cardFrontFragment.setLoginListener(new CardFrontFragment.LoginListener() {
            @Override
            public void onLogin(String user, String password) {
                if (!isLogin) {
                    messageService.closeController();

                    messageService.initSipController(user, password);
                    UserInformation usera = new UserInformation(user);
                    usera.password = password;
                    messageService.login(usera);
                    isLogin = true;
                } else {
                    Toast.makeText(LoginActivity.this, "正在尝试登录中，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardBackFragment.setRegisteListener(new CardBackFragment.RegisteListener() {
            @Override
            public void onRegister(String user, String password) {
                if (!isLogin) {
                    messageService.closeController();
                    messageService.initSipController(user, password);
                    messageService.initSipController(user, password);
                    UserInformation usera = new UserInformation(user);
                    usera.password = password;
                    messageService.login(usera);
                    isLogin = true;
                } else {
                    Toast.makeText(LoginActivity.this, "正在尝试登录中，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });


        messageService.setRegistLoginResult(new MessageService.RegistLoginResult() {
            @Override
            public void onRegistLogin(boolean status) {
                isLogin = false;
                if (status) {
                    isLogin = false;
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    messageService.closeController();
                }
            }
        });
    }

    private void setCameraDistance() {
        login_card = (CardView) findViewById(R.id.login_card);
        register_card = (CardView) findViewById(R.id.register_card);
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        if (login_card != null) {
            login_card.setCameraDistance(scale);
        }
        if (register_card != null) {
            register_card.setCameraDistance(scale);
        }
    }

    public static class CardFrontFragment extends android.app.Fragment {


        public interface LoginListener {
            void onLogin(String user, String password);
        }

        LoginListener loginListener;

        public void setLoginListener(LoginListener loginListener) {
            this.loginListener = loginListener;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_login, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button login = (Button) view.findViewById(R.id.login_button);
            final MaterialEditText loginUserName = (MaterialEditText) view.findViewById(R.id.login_username);
            final MaterialEditText loginPassword = (MaterialEditText) view.findViewById(R.id.login_password);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.e("Login", "Click Login");
                    String user = loginUserName.getText().toString();
                    String pass = loginPassword.getText().toString();

                    if (!user.equals("") && !pass.equals("")) {
                        loginListener.onLogin(user, pass);
                    }



                }
            });
        }
    }

    public static class CardBackFragment extends android.app.Fragment {
        public interface RegisteListener {
            void onRegister(String user, String password);
        }

        RegisteListener registeListener;

        public void setRegisteListener(RegisteListener registeListener) {
            this.registeListener = registeListener;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_register, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Button regist = (Button) view.findViewById(R.id.register_button);
            MaterialEditText u = (MaterialEditText) view.findViewById(R.id.register_username);
            MaterialEditText p = (MaterialEditText) view.findViewById(R.id.register_password);
            MaterialEditText pe = (MaterialEditText) view.findViewById(R.id.register_password_ensure);

            final String userName = u.getText().toString();
            final String password = p.getText().toString();
            final String passwordEnsure = pe.getText().toString();
            regist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (password.equals(passwordEnsure)) {
                        registeListener.onRegister(userName, password);
                    } else {
                        Toast.makeText(getActivity(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    public void flipCard(View view) {

        if (!isLoginFront) {
            getFragmentManager().popBackStack();
            isLoginFront = true;
            return;
        }
        isLoginFront = false;
        android.app.FragmentTransaction ft = getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.anim_right_in, R.animator.anim_right_out,
                        R.animator.anim_left_in, R.animator.anim_left_out)
                .replace(R.id.lanuch_login_register, cardBackFragment);
        setCameraDistance();
        ft.commit();


    }


}
