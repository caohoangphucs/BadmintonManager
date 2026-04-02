package com.example.winfinal.dao;

import com.example.winfinal.entity.Feedback;

public class FeedbackDAO extends BaseDAO<Feedback, Integer> {
    public FeedbackDAO() {
        super(Feedback.class);
    }
}
