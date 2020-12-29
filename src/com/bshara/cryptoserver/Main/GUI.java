package com.bshara.cryptoserver.Main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.bshara.cryptoserver.Utils.JSONUtil;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Keys.Key;
import com.bshara.cryptoserver.Utils.RSA_ICE;
import com.bshara.cryptoserver.Utils.Entities.StatusedMessage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

public class GUI extends Application {
	public static String root = "http://localhost:8080/JRA2/main/";

	public static TextArea ta1, ta2, ta3;

	public static void main(String[] args) {
		launch(args);
	}

	private final String msg = "Hello this is ";

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("HBox Experiment 1");

		Button btn1 = new Button("Say hello to Eve");
		Button btn2 = new Button("Say hello to Bob");
		Button btn3 = new Button("Say hello to Alice");

		ta1 = new TextArea();
		ta2 = new TextArea();
		ta3 = new TextArea();

		ta1.setPrefHeight(700);
		ta2.setPrefHeight(700);
		ta3.setPrefHeight(700);

		btn1.setOnAction(actionEvent -> {
			String myMsg = msg + "Alice" + "\n";

			try {
				Send("Alice", myMsg, 0, 1);
			} catch (NoSuchAlgorithmException | IOException e) {
				//e.printStackTrace();
			}

		});

		btn2.setOnAction(actionEvent -> {
			String myMsg = msg + "Eve" + "\n";

			try {
				Send("Eve", myMsg, 1, 2);
			} catch (NoSuchAlgorithmException | IOException e) {
				//e.printStackTrace();
			}

		});

		btn3.setOnAction(actionEvent -> {
			String myMsg = msg + "Bob" + "\n";

			try {
				Send("Bob", myMsg, 2, 0);
			} catch (NoSuchAlgorithmException | IOException e) {
				//e.printStackTrace();
			}

		});

		Thread thread = new Thread() {
			public void run() {

				try {
					GetAllMessages();
				} catch (NoSuchAlgorithmException | IOException e1) {
					//e1.printStackTrace();
				}
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
				run();
			}
		};
		thread.start();

		Label lbl1 = new Label("Alice");
		Label lbl2 = new Label("Eve");
		Label lbl3 = new Label("Bob");

		VBox v1 = new VBox(lbl1, ta1, btn1);
		VBox v2 = new VBox(lbl2, ta2, btn2);
		VBox v3 = new VBox(lbl3, ta3, btn3);

		HBox hbox = new HBox(v1, v2, v3);

		Scene scene = new Scene(hbox, 1200, 800);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void Send(String username, String message, int privateKeyIndex, int publicKeyIndex)
			throws NoSuchAlgorithmException, IOException {
		Key myK = Keys.INSTANCE.getKey(privateKeyIndex);
		Key k1 = Keys.INSTANCE.getKey(publicKeyIndex);

		int myD = myK.d;
		int myN = myK.n;
		int e = k1.e;
		int n = k1.n;

		String c = RSA_ICE.Encrypt(message, myD, myN, e, n);
		c = new String(Base64.getUrlEncoder().encode(c.getBytes()));

		String generateUrl = root + "chatx/" + username + "?msg=" + c;

		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl, StatusedMessage.class));
	}

	@SuppressWarnings("unchecked")
	public void GetAllMessages() throws IOException, NoSuchAlgorithmException {
		System.out.println("Fetching messages");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String generateUrl = root + "allmessages/";

				StatusedMessage response;
				try {
					response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl, StatusedMessage.class));
					
					ArrayList<String> cmsgs = JSON.parseObject(response.getMessage(), ArrayList.class);

					ta1.setText("");
					ta2.setText("");
					ta3.setText("");

					for (String c : cmsgs) {
						c = new String(Base64.getUrlDecoder().decode(c));

						UpdateTextArea(c, 0, 1, ta1);
						UpdateTextArea(c, 1, 2, ta2);
						UpdateTextArea(c, 2, 0, ta3);

					}
				} catch (IOException e) {
					//e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					//e.printStackTrace();
				}

				
			}
		});

	}

	public static void UpdateTextArea(String c, int privateKeyIndex, int publicKeyIndex, TextArea ta)
			throws NoSuchAlgorithmException {
		TreeMap<String, String> msg = decrypt(c, privateKeyIndex, publicKeyIndex);
		String m = msg.get("m");
		ta.setText(ta.getText() + "\n" + m);
	}

	public static TreeMap<String, String> decrypt(String c, int privateKeyIndex, int publicKeyIndex)
			throws NoSuchAlgorithmException {

		Key myK = Keys.INSTANCE.getKey(privateKeyIndex);
		Key k1 = Keys.INSTANCE.getKey(publicKeyIndex);

		int myD = myK.d;
		int myN = myK.n;
		int e = k1.e;
		int n = k1.n;

		try {
			return RSA_ICE.Decrypt(c, myD, myN, e, n);
		} catch (Exception e2) {
			TreeMap<String, String> map = new TreeMap<String, String>();
			map.put("m", c);
			return map;
		}

	}

}
