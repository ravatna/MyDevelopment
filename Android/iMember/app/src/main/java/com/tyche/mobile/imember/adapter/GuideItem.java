package com.tyche.mobile.imember.adapter;

/**
 * Created by rewat on 31/5/2560.
 */

public class GuideItem {
    int m_id;
    String m_name;
    String m_shortDesc;
    String m_rateScore;
    int m_resImg;

    public GuideItem(int m_id, String m_name, String m_shortDesc, String m_rateScore, int m_resImg) {
        this.m_id = m_id;
        this.m_name = m_name;
        this.m_shortDesc = m_shortDesc;
        this.m_rateScore = m_rateScore;
        this.m_resImg = m_resImg;
    }
}
