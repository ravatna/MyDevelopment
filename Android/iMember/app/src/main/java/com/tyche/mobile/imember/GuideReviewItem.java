package com.tyche.mobile.imember;

/**
 * Created by rewat on 31/5/2560.
 */

class GuideReviewItem {
    int m_id;
    String m_name;
    String m_shortDesc;
    String m_rateScore;
    int m_resImg;

    public GuideReviewItem(int m_id, String m_name, String m_shortDesc, String m_rateScore, int m_resImg) {
        this.m_id = m_id;
        this.m_name = m_name;
        this.m_shortDesc = m_shortDesc;
        this.m_rateScore = m_rateScore;
        this.m_resImg = m_resImg;
    }
}
