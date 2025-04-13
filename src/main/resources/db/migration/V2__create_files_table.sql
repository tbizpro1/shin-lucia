CREATE TABLE files_lucia (
                             id BIGSERIAL PRIMARY KEY,
                             file_url VARCHAR(255) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             type VARCHAR(100),
                             user_id BIGINT NOT NULL,
                             author VARCHAR(255),
                             step DOUBLE PRECISION NOT NULL,
                             name VARCHAR(255)
);
