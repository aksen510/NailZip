package com.example.nailzip.member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.nailzip.model.NailshopData;
import com.example.nailzip.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberViewModel extends AndroidViewModel {

    private MemberModel memberModel;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logoutMutableLiveData;
    private MutableLiveData<Boolean> saveUserInfoMutableLiveData;
    private MutableLiveData<Boolean> isSuccessful;
    private MutableLiveData<FirebaseFirestore> shopMutableLiveData;

    public MemberViewModel(@NonNull Application application){
        super(application);

        memberModel = new MemberModel(application);
        userMutableLiveData = memberModel.getUserMutableLiveData();
        logoutMutableLiveData = memberModel.getLogoutMutableLiveData();
        saveUserInfoMutableLiveData = memberModel.getSaveUserInfoMutableLiveData();
        isSuccessful = memberModel.getIsSuccessful();
        shopMutableLiveData = memberModel.getShopMutableLiveData();
    }

    public void register(String email, String password, User userAccount){
        memberModel.register(email,password,userAccount);
    }

    public void storeInfo(NailshopData shopAccount){
        memberModel.storeInfo(shopAccount);
    }

    public void login(String email, String password){
        memberModel.login(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLogoutMutableLiveData() {
        return logoutMutableLiveData;
    }

    public MutableLiveData<Boolean> getSaveUserInfoMutableLiveData() {
        return saveUserInfoMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsSuccessful() {
        return isSuccessful;
    }

    public MutableLiveData<FirebaseFirestore> getShopMutableLiveData() {
        return shopMutableLiveData;
    }
}
