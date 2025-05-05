ALTER TABLE lucia_idea
DROP COLUMN IF EXISTS user_id;

ALTER TABLE lucia_idea
    ADD COLUMN company_id BIGINT;


UPDATE lucia_idea
SET company_id = 1
WHERE company_id IS NULL;

ALTER TABLE lucia_idea
    ALTER COLUMN company_id SET NOT NULL;
