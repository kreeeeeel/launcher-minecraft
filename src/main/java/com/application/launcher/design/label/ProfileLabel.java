package com.application.launcher.design.label;

import com.application.launcher.rest.response.ProfileResponse;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

public class ProfileLabel {

    private final Label login;
    private final Label balance;

    private final ImageView settings;
    private final ImageView ruble;

    public ProfileLabel(Label login, Label balance, ImageView settings, ImageView ruble) {
        this.login = login;
        this.balance = balance;
        this.settings = settings;
        this.ruble = ruble;
    }

    public void setLogin(ProfileResponse profileResponse) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime time = LocalTime.now(zoneId);
        int hour = time.getHour();
        String value = (hour < 6 ? "Доброй ночи" : hour < 12 ? "Доброго утро" :
                hour < 18 ? "Доброго дня" : "Доброго вечера") + ", " + profileResponse.getLogin();

        Text text = new Text(value);
        text.setFont(Font.font("Franklin Gothic Medium", 20));

        settings.setLayoutX(text.getLayoutBounds().getWidth() + 72);
        login.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
        login.setText(value);
    }

    public void setBalance(ProfileResponse profileResponse) {
        String value = String.format(Locale.US, "%,.2f", profileResponse.getBalance().floatValue());

        Text text = new Text(value);
        text.setFont(Font.font("Franklin Gothic Medium", 18));

        ruble.setLayoutX(text.getLayoutBounds().getWidth() + 80);
        balance.setPrefSize(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
        balance.setText(value);
    }
}
