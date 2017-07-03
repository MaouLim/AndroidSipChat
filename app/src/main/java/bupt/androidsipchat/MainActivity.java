package bupt.androidsipchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

	boolean isLoginFront = true;
	CardView login_card;
	CardView register_card;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_login_register);

		if (savedInstanceState == null) {
			getFragmentManager()
					.beginTransaction()
					.add(R.id.lanuch_login_register, new CardFrontFragment())
					.commit();
		}


		//	setCameraDistance();
		//initButton();
	}

//	private void initButton(){
//		loginRegister.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				flipCard();
//			}
//		});
//
//		registerLogin.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				flipCard();
//			}
//		});
//	}

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
		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.card_login, container, false);
		}
	}

	public static class CardBackFragment extends android.app.Fragment {
		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.card_register, container, false);
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
				.replace(R.id.lanuch_login_register, new CardBackFragment());
		setCameraDistance();
		ft.commit();


	}





}
