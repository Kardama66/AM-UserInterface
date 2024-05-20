package com.example.projecte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoggedIn extends AppCompatActivity {

    private TextView textViewNickname;
    private TextView textViewEmail;
    private EditText editTextItemName;
    private EditText editTextItemPrice;
    private Button buttonAddItem;
    private Button buttonLogout;
    private ListView listViewShoppingList;
    private User user;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in);

        textViewNickname = findViewById(R.id.textViewUserInfo);
        editTextItemName = findViewById(R.id.editTextNewItemName);
        editTextItemPrice = findViewById(R.id.editTextNewItemPrice);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonLogout = findViewById(R.id.buttonLogout);
        listViewShoppingList = findViewById(R.id.listViewShoppingList);

        user = getIntent().getParcelableExtra("user");

        // Inicjalizacja adaptera z pustą listą
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewShoppingList.setAdapter(adapter);

        // Wyświetlanie nazwy użytkownika i adresu e-mail
        textViewNickname.setText("Nickname: " + user.getLogin() + " Email: " + user.getEmail());

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Wyświetlanie listy zakupów po uruchomieniu aktywności
        displayShoppingList(user.getShoppingListMap());

        // Obsługa kliknięcia na element listy
        listViewShoppingList.setOnItemClickListener((parent, view, position, id) -> {
            // Usunięcie elementu z bazy danych i adaptera
            removeItemFromList(position);
        });
    }

    private void addItemToList() {
        String itemName = editTextItemName.getText().toString().trim();
        String itemPriceText = editTextItemPrice.getText().toString().trim();
        if (itemName.isEmpty() || itemPriceText.isEmpty()) {
            return;
        }

        double itemPrice = Double.parseDouble(itemPriceText);

        // Dodanie nowego przedmiotu do listy zakupów
        Map<Double, String> shoppingList = user.getShoppingListMap();
        shoppingList.put(itemPrice, itemName);
        user.setShoppingListMap(shoppingList);

        // Aktualizacja widoku listy zakupów
        displayShoppingList(shoppingList);

        // Wyczyszczenie pól EditText
        editTextItemName.getText().clear();
        editTextItemPrice.getText().clear();

        // Aktualizacja bazy danych
        updateUserInDatabase();
    }

    private void displayShoppingList(Map<Double, String> shoppingList) {
        // Usunięcie starych elementów z adaptera
        adapter.clear();

        // Dodanie nowych elementów do adaptera z ceną i nazwą przedmiotu
        for (Map.Entry<Double, String> entry : shoppingList.entrySet()) {
            String item = entry.getValue() + " - " + entry.getKey(); // Dodanie ceny do nazwy przedmiotu
            adapter.add(item);
        }

        // Powiadomienie adaptera o zmianach w danych
        adapter.notifyDataSetChanged();
    }

    private void removeItemFromList(int position) {
        // Usunięcie elementu z listy zakupów
        Map<Double, String> shoppingList = user.getShoppingListMap();
        List<Double> prices = new ArrayList<>(shoppingList.keySet());
        if (position >= 0 && position < prices.size()) {
            double price = prices.get(position);
            shoppingList.remove(price);
            user.setShoppingListMap(shoppingList);

            // Usunięcie elementu z adaptera
            adapter.remove(adapter.getItem(position));
            adapter.notifyDataSetChanged();

            // Aktualizacja bazy danych
            updateUserInDatabase();
        }
    }

    private void updateUserInDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Wywołaj metodę updateUser z UserDao, przekazując aktualizowanego użytkownika
                UserDAO userDao = UserDatabase.getInstance(getApplicationContext()).useUserDao();
                userDao.updateUser(user);
            }
        }).start();
    }

    // Metoda obsługująca wylogowanie
    private void logout() {
        Intent intent = new Intent(LoggedIn.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
