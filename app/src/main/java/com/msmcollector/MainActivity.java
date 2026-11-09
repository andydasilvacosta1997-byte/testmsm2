package com.msmcollector;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    private SharedPreferences preferences;

    private final String[] islands = {
        "Plant Island",
        "Cold Island",
        "Air Island",
        "Water Island",
        "Earth Island",
        "Fire Haven",
        "Fire Oasis",
        "Light Island",
        "Psychic Island",
        "Faerie Island",
        "Bone Island",
        "Ethereal Island",
        "Ethereal Workshop",
        "Wublin Island",
        "Amber Island",
        "Shugabush Island",
        "Gold Island",
        "Magical Sanctum",
        "Magical Nexus",
        "Paranormal Carnival",
        "Seasonal Shanty",
        "Celestial Island",
        "Tribal Island",
        "Composer Island",
        "Clubbox",
        "Colossingum",
        "Mirror Plant Island",
        "Mirror Cold Island",
        "Mirror Air Island",
        "Mirror Water Island",
        "Mirror Earth Island",
        "Mirror Fire Haven",
        "Mirror Fire Oasis"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("collector", MODE_PRIVATE);

        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28, 28, 28, 28);

        scroll.addView(layout);

        TextView heading = new TextView(this);
        heading.setText("My Singing Monsters\nResource Collector");
        heading.setTextSize(25);
        heading.setPadding(0, 0, 0, 25);
        layout.addView(heading);

        TextView islandHeading = new TextView(this);
        islandHeading.setText("Choose an island");
        islandHeading.setTextSize(18);
        layout.addView(islandHeading);

        Spinner islandSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            islands
        );
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );
        islandSpinner.setAdapter(adapter);
        layout.addView(islandSpinner);

        TextView selected = new TextView(this);
        selected.setText("Selected island: Plant Island");
        selected.setTextSize(16);
        selected.setPadding(0, 15, 0, 20);
        layout.addView(selected);

        islandSpinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
                ) {
                    selected.setText(
                        "Selected island: " + islands[position]
                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            }
        );

        TextView resourceHeading = new TextView(this);
        resourceHeading.setText("Collection settings");
        resourceHeading.setTextSize(18);
        layout.addView(resourceHeading);

        CheckBox autoCollect = new CheckBox(this);
        autoCollect.setText("Enable automatic collection");
        autoCollect.setChecked(
            preferences.getBoolean("auto_collect", false)
        );

        autoCollect.setOnCheckedChangeListener((button, checked) -> {
            preferences.edit()
                .putBoolean("auto_collect", checked)
                .apply();
        });

        layout.addView(autoCollect);

        TextView information = new TextView(this);
        information.setText(
            "\nResources this project aims to support:\n\n" +
            "• Coins — Collect All\n" +
            "• Bakery food — ready outputs\n" +
            "• Diamonds — Mini-Mines\n" +
            "• Diamonds — Maximum Mines\n\n" +
            "Maximum Mines are only available on Plant " +
            "Island and Mirror Plant Island.\n"
        );
        information.setTextSize(16);
        layout.addView(information);

        Button accessibilityButton = new Button(this);
        accessibilityButton.setText(
            "Open Android Accessibility Settings"
        );

        accessibilityButton.setOnClickListener(v -> {
            startActivity(
                new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            );
        });

        layout.addView(accessibilityButton);

        TextView warning = new TextView(this);
        warning.setText(
            "\nAutomatic collection is experimental. " +
            "It only recognises certain accessible button labels. " +
            "Keep it disabled until you have tested it safely."
        );
        warning.setTextSize(14);
        layout.addView(warning);

        setContentView(scroll);
    }
}
