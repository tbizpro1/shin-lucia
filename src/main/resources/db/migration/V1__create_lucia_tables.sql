CREATE TABLE lucia_idea (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    step DOUBLE PRECISION,
    problem TEXT,
    solution TEXT,
    who_is TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lucia_response (
    id BIGSERIAL PRIMARY KEY,
    related_step DOUBLE PRECISION,
    content TEXT,
    author VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    url_history VARCHAR(255),
    object_name VARCHAR(255),
    idea_id BIGINT,
    CONSTRAINT fk_response_idea FOREIGN KEY (idea_id) REFERENCES lucia_idea(id) ON DELETE CASCADE
);

CREATE TABLE lucia_summaryideas (
    id BIGSERIAL PRIMARY KEY,
    object_name VARCHAR(255),
    url_file VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    idea_id BIGINT,
    CONSTRAINT fk_summary_idea FOREIGN KEY (idea_id) REFERENCES lucia_idea(id) ON DELETE CASCADE
);
