CREATE TABLE IF NOT EXISTS assessments (
                                           id BIGSERIAL PRIMARY KEY,
                                           user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    assessment_date TIMESTAMP WITH TIME ZONE NOT NULL,
                                                                                            score INT NOT NULL,
                                                                                            max_score INT NOT NULL,
                                                                                            risk_level VARCHAR(20) NOT NULL,
    answers JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                                                                                            );