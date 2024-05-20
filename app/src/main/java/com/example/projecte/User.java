package com.example.projecte;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "users")
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    // Przechowywanie listy zakupów jako ciąg znaków w formacie "klucz:wartość,klucz:wartość,..."
    @ColumnInfo(name = "shopping_list")
    private String shoppingList;

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.shoppingList = "";
    }

    protected User(Parcel in) {
        id = in.readInt();
        login = in.readString();
        password = in.readString();
        email = in.readString();
        shoppingList = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(String shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Map<Double, String> getShoppingListMap() {
        Map<Double, String> map = new HashMap<>();
        if (shoppingList != null && !shoppingList.isEmpty()) {
            String[] items = shoppingList.split(",");
            for (String item : items) {
                String[] parts = item.split(":");
                if (parts.length == 2) {
                    try {
                        double key = Double.parseDouble(parts[0]);
                        String value = parts[1];
                        map.put(key, value);
                    } catch (NumberFormatException e) {
                        // Ignoruj nieprawidłowe dane
                    }
                }
            }
        }
        return map;
    }

    public void setShoppingListMap(Map<Double, String> shoppingListMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Double, String> entry : shoppingListMap.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        this.shoppingList = sb.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(shoppingList);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
