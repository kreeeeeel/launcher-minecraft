package com.application.launcher.utils;

import com.application.launcher.entity.AccountEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AccountUtils {

    public List<AccountEntity> getAccounts(){
        try {
            File file = new File("accounts.json");
            if (!file.isFile() && !file.createNewFile()){
                return null;
            }

            ReadFileUtils readFileUtils = new ReadFileUtils(file);
            return new Gson().fromJson(readFileUtils.get(), new TypeToken<ArrayList<AccountEntity>>() {}.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(AccountEntity accountEntity){
        try {
            File file = new File("accounts.json");
            if (!file.isFile() && !file.createNewFile()){
                return;
            }

            ReadFileUtils readFileUtils = new ReadFileUtils(file);
            WriteFileUtils writeFileUtils = new WriteFileUtils(file);

            List<AccountEntity> accounts = new Gson().fromJson(readFileUtils.get(), new TypeToken<ArrayList<AccountEntity>>() {}.getType());
            accounts.add(accountEntity);
            writeFileUtils.write(new Gson().toJson(accounts));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(AccountEntity accountEntity) {
        try {
            File file = new File("accounts.json");
            if (!file.isFile() && !file.createNewFile()){
                return;
            }

            ReadFileUtils readFileUtils = new ReadFileUtils(file);
            WriteFileUtils writeFileUtils = new WriteFileUtils(file);

            List<AccountEntity> accounts = new Gson().fromJson(readFileUtils.get(), new TypeToken<ArrayList<AccountEntity>>() {}.getType());
            if(accounts == null || accounts.size() == 0){
                return;
            }

            accounts.removeIf(account -> accountEntity.getUsername().equals(account.getUsername()));
            writeFileUtils.write(new Gson().toJson(accounts));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
