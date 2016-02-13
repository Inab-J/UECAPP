package net.inab_j.uecapp.model.System;

import android.util.SparseArray;

import net.inab_j.uecapp.R;

/**
 * 時間割のActivityのボタンとPDF名の対応マップを保持するクラス。
 * ボタンのIDとPDFファイル名を対応付けたマップを持つ。
 */
public class IdCorrespond {

    public static final SparseArray<String> ID_TO_NAME = new SparseArray<String>() {
        {
            // day
            // 1st grade, 1st term
            put(R.id.day_1st_1, "A9.pdf");
            put(R.id.day_1st_1_english, "English1.pdf");
            put(R.id.day_1st_1_foreign, "2gai1.pdf");

            // 1st grade, 2nd term
            put(R.id.day_1st_2, "A10.pdf");
            put(R.id.day_1st_2_english, "English2.pdf");
            put(R.id.day_1st_2_foreign, "2gai2.pdf");

            // 2nd grade, 1st term
            put(R.id.day_2nd_1, "A11.pdf");
            put(R.id.day_2nd_1_english, "English3.pdf");

            // 2nd grade, 2nd term
            put(R.id.day_2nd_2, "A12.pdf");
            put(R.id.day_2nd_2_english, "English4.pdf");

            // 3rd grade
            put(R.id.day_3rd_1, "A13.pdf");
            put(R.id.day_3rd_2, "A14.pdf");
            put(R.id.day_high_1, "jyoukyu7.pdf");
            put(R.id.day_high_2, "jyoukyu8.pdf");

            // 4th grade
            put(R.id.day_4th_1, "A15.pdf");
            put(R.id.day_4th_2, "A16.pdf");

            // night
            put(R.id.night_1st_1, "B9.pdf");
            put(R.id.night_1st_2, "B10.pdf");
            put(R.id.night_2nd_1, "B11.pdf");
            put(R.id.night_2nd_2, "B12.pdf");
            put(R.id.night_3rd_1, "B13.pdf");
            put(R.id.night_3rd_2, "B14.pdf");
            put(R.id.night_4th_1, "B15.pdf");
            put(R.id.night_4th_2, "B16.pdf");

            // others
            // teacher
            put(R.id.teach_1, "kyousyoku1.pdf");
            put(R.id.teach_2, "kyousyoku2.pdf");

            // international
            put(R.id.international_1, "kokusai1.pdf");
            put(R.id.international_2, "kokusai2.pdf");

            // link
            put(R.id.link_1, "inrenkei1.pdf");
            put(R.id.link_2, "inrenkei2.pdf");
        }

    };
}
