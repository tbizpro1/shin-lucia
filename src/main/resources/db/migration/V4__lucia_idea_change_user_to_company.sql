ALTER TABLE lucia_idea
    DROP COLUMN IF EXISTS user_id;

-- Adiciona o novo campo company_id
ALTER TABLE lucia_idea
    ADD COLUMN company_id BIGINT NOT NULL;