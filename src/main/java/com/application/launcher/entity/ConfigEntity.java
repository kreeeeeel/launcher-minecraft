package com.application.launcher.entity;

public class ConfigEntity {
    private long size;
    private boolean fullscreen;
    private boolean autoConnect;
    private boolean loggerGame;
    private boolean saveAccount;

    private AccountEntity accountEntity;

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

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isLoggerGame() {
        return loggerGame;
    }

    public void setLoggerGame(boolean loggerGame) {
        this.loggerGame = loggerGame;
    }

    public boolean isSaveAccount() {
        return saveAccount;
    }

    public void setSaveAccount(boolean saveAccount) {
        this.saveAccount = saveAccount;
    }

    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }
}
