ALTER TABLE files_lucia
DROP COLUMN IF EXISTS step;

ALTER TABLE files_lucia
    ADD COLUMN idea_id BIGINT;

CREATE INDEX idx_files_lucia_idea_id ON files_lucia(idea_id);
