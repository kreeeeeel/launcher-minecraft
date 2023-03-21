package com.application.launcher.entity;

import java.util.ArrayList;
import java.util.List;

public class ConfigEntity {
    private long size;
    private boolean fullscreen;
    private boolean autoConnect;
    private boolean loggerGame;
    private List<AccountEntity> accounts;

    public long getSize() {
        return size;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void addAccount(AccountEntity accountEntity) {

        if (accounts == null) {
            accounts = new ArrayList<>();
        }

        if (isAccountHave(accountEntity)) {
            return;
        }

        accounts.add(accountEntity);
    }

    public boolean isAccountHave(AccountEntity accountEntity){

        if (accounts == null || accounts.size() == 0){
            return false;
        }

        for (AccountEntity account : accounts){
            if(account.getUsername().equals(accountEntity.getUsername())){
                return true;
            }
        }
        return false;
    }

    public boolean isLoggerGame() {
        return loggerGame;
    }

    public void setLoggerGame(boolean loggerGame) {
        this.loggerGame = loggerGame;
    }
}
