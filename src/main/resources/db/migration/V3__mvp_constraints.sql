-- V3__mvp_constraints_and_professional_user_link.sql
-- Purpose:
-- 1) Avoid fragile uniqueness on customer phone (US phone formatting/normalization will come later)
-- 2) Link professionals to users to prevent identity mismatch between login (users) and professional records

-- 1) Drop UNIQUE(tenant_id, phone) from app.customers if it exists (name may be auto-generated)
DO $$
DECLARE
constraint_name text;
BEGIN
SELECT c.conname
INTO constraint_name
FROM pg_constraint c
         JOIN pg_class t ON t.oid = c.conrelid
         JOIN pg_namespace n ON n.oid = t.relnamespace
WHERE n.nspname = 'app'
  AND t.relname = 'customers'
  AND c.contype = 'u';

IF constraint_name IS NOT NULL THEN
    -- This will drop the first unique constraint found on customers.
    -- In MVP we only created one unique constraint, so it's safe.
    EXECUTE format('ALTER TABLE app.customers DROP CONSTRAINT %I', constraint_name);
END IF;
END $$;

-- Add a helpful index for customer lookups by phone (non-unique)
CREATE INDEX IF NOT EXISTS idx_customers_tenant_phone
    ON app.customers(tenant_id, phone);

-- 2) Link professionals to users (optional for existing data, but critical for consistency)
ALTER TABLE app.professionals
    ADD COLUMN IF NOT EXISTS user_id uuid;

-- Ensure one professional profile per user (within the whole platform)
-- If you prefer per-tenant uniqueness, we can change this later.
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint c
    JOIN pg_class t ON t.oid = c.conrelid
    JOIN pg_namespace n ON n.oid = t.relnamespace
    WHERE n.nspname = 'app'
      AND t.relname = 'professionals'
      AND c.conname = 'uk_professionals_user_id'
  ) THEN
ALTER TABLE app.professionals
    ADD CONSTRAINT uk_professionals_user_id UNIQUE (user_id);
END IF;
END $$;

-- Add FK to users
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint c
    JOIN pg_class t ON t.oid = c.conrelid
    JOIN pg_namespace n ON n.oid = t.relnamespace
    WHERE n.nspname = 'app'
      AND t.relname = 'professionals'
      AND c.conname = 'fk_professionals_user_id'
  ) THEN
ALTER TABLE app.professionals
    ADD CONSTRAINT fk_professionals_user_id
        FOREIGN KEY (user_id) REFERENCES app.users(id) ON DELETE SET NULL;
END IF;
END $$;
