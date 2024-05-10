package com.example.projecte;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String login;
    private String password;
    private String email;
    private List<ShoppingList> shoppingList;

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.shoppingList = new ArrayList<>();
    }

    public void addToList(ShoppingList item){
        this.shoppingList.add(item);
    }

    public void deleteFromList(ShoppingList item){
        this.shoppingList.remove(item);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ShoppingList> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingList> shoppingList) {
        this.shoppingList = shoppingList;
    }
}
