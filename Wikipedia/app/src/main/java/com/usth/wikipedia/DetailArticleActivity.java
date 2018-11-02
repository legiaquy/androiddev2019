package com.usth.wikipedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailArticleActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ARTICLENO = "articleNo";
    EditText detail_content_article;
    ImageView detail_image_article;
    TextView detail_title_article;
    ImageButton edit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        detail_content_article = findViewById(R.id.detail_content_article);
        detail_image_article = findViewById(R.id.detail_image_article);
        detail_title_article = findViewById(R.id.detail_title_article);
        edit_button = findViewById(R.id.edit_button);

        int articleNo = (Integer) getIntent().getExtras().get(EXTRA_ARTICLENO); // Get article's position
        ModelObject selectedArticle = ModelObject.values()[articleNo]; // Get the selected article from ModelObject

        detail_image_article.setImageResource(selectedArticle.getmImageResId());

        detail_title_article.setText(selectedArticle.getmTitleResId());

        switch (articleNo){
            case 0:
                detail_content_article.setText("The 2009 World Series was the championship series of Major League Baseball\\'s (MLB) 2009 season. As the 105th[1] edition of the World Series, it was a best-of-seven playoff contested between the Philadelphia Phillies, champions of the National League (NL) and defending World Series champions, and the New York Yankees, champions of the American League (AL). The Yankees defeated the Phillies, 4 games to 2, winning their 27th World Series championship. The series was played between October 28 and November 4, broadcast on Fox, and watched by an average of roughly 19 million viewers. Due to the start of the season being pushed back by the 2009 World Baseball Classic in March, this was the first World Series regularly scheduled to be played into the month of November. This series was a rematch of the 1950 World Series. With both teams having symbols of American Liberty (The Statue of Liberty for New York and The Liberty Bell in Philadelphia) the Series was given the nickname, the \"Liberty Series\" by some.[2]");
                break;
            case 1:
                detail_content_article.setText("S. serrator is a parasitoid of the larvae of wood-boring beetles. Despite being able to fly, these wasps usually move about by walking and usually avoid sunlight. Spiders such as Nuctenea umbratica and Parasteatoda spp. sometimes feed on the wasps, but the wasps usually manage to evade them. Female wasps that are trying to locate beetle larvae in wood adopt a characteristic posture with fore and hind legs spread widely, middle legs folded tightly against the body, antennae lowered and ovipositor sheath pressed against the wood; they then move a few centimetres to a new location and repeat the process. When a potential target is located, the ovipositor is bored into the substrate. Boring may take many hours, with rests in between the boring efforts, at which times the females withdraw their ovipositors.[4] They seem to be able to re-locate the hole they were working on when they recommence boring. As the ovipositor is pushed deeper into the wood, the sheath arches upwards in a loop.[4]The eggs are laid in the galleries created by the beetle larvae. The developing wasp larvae feed on the beetle larvae, rejecting the most heavily chitinised parts. When fully developed they pupate in the galleries left by the beetle larvae. Male wasps emerge some ten days before the females and adopt a similar search posture, perhaps waiting for the females to emerge.[4] ");
                break;
            case 2:
                detail_content_article.setText("The Statue of Liberty (Liberty Enlightening the World; French: La Liberté éclairant le monde) is a colossal neoclassical sculpture on Liberty Island in New York Harbor in New York City, in the United States. The copper statue, a gift from the people of France to the people of the United States, was designed by French sculptor Frédéric Auguste Bartholdi and built by Gustave Eiffel. The statue was dedicated on October 28, 1886.The Statue of Liberty is a figure of a robed woman representing Libertas, a Roman liberty goddess. She holds a torch above her head with her right hand, and in her left hand carries a tabula ansata inscribed in Roman numerals with \"JULY IV MDCCLXXVI\" (July 4, 1776), the date of the U.S. Declaration of Independence. A broken chain lies at her feet as she walks forward. The statue became an icon of freedom and of the United States, and was a welcoming sight to immigrants arriving from abroad. Bartholdi was inspired by a French law professor and politician, Édouard René de Laboulaye, who is said to have commented in 1865 that any monument raised to U.S. independence would properly be a joint project of the French and U.S. peoples. Because of the post-war instability in France, work on the statue did not commence until the early 1870s. In 1875, Laboulaye proposed that the French finance the statue and the U.S. provide the site and build the pedestal. Bartholdi completed the head and the torch-bearing arm before the statue was fully designed, and these pieces were exhibited for publicity at international expositions. ");
        }

        detail_content_article.setEnabled(false);

        edit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                enableDisableEditText();
                break;
            default:
                break;
        }
    }

    //method for enable or disable editttext
    private void enableDisableEditText() {
        if (detail_content_article.isEnabled()) {
            detail_content_article.setEnabled(false);
            edit_button.setImageResource(R.drawable.ic_edit_black_24dp);
        } else {
            detail_content_article.setEnabled(true);
            edit_button.setImageResource(R.drawable.ic_done_black_24dp);
        }
    }


}
