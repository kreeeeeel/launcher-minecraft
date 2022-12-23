package com.application.launcher.rest.response;

import java.math.BigDecimal;

public class ProfileResponse {
    private String login;
    private String name;
    private BigDecimal balance;
    private ServerResponse[] servers;

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public ServerResponse[] getServers() {
        return servers;
    }
}
