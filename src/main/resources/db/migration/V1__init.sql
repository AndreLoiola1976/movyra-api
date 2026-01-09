-- V1__init.sql
-- Movyra (MVP) - Clean, cohesive schema for prototyping (DB is disposable)
-- Key decisions:
-- 1) appointments references tenant_services via tenant_service_id (surrogate PK) ✅
-- 2) tenant_services uses surrogate PK + unique(tenant_id, service_id) ✅
-- 3) portfolio_items unified ✅
-- 4) customers has is_active ✅
-- 5) professionals can be linked to users via user_id (unique when not null) ✅
-- 6) tenant_features is 1:1 with tenants (defaults OFF) ✅
-- 7) professionals supports single vs multi-barber UX (is_primary, sort_order, public_slug) ✅
-- 8) optional mapping tenant_service_professionals prepares "book with barber X" ✅
-- 9) Trilingual support: tenant.default_lang is fallback (en/pt/es) ✅

create schema if not exists app;

-- UUID helper
create extension if not exists pgcrypto;

-- =========================
-- TENANTS
-- =========================
create table if not exists app.tenants (
                                           id uuid primary key default gen_random_uuid(),
    slug text not null unique,
    name text not null,

    -- Trilingual fallback (UI can override per visitor)
    default_lang char(2) not null default 'en'
    check (default_lang in ('en','pt','es')),

    -- contact / location (site-first)
    phone text,
    address_line1 text,
    address_line2 text,
    city text,
    state text,
    zip text,
    country char(2) not null default 'US',
    timezone text not null default 'America/New_York',

    -- branding assets (MVP: external URLs)
    logo_url text,

    -- social / links (MVP: keep simple; refine later)
    whatsapp_phone_e164 text,        -- ex: +12034173905 (recommended)
    whatsapp_message_template text,  -- ex: "Hi, I'd like to book..."
    instagram_url text,
    google_maps_url text,
    website_url text,

    -- Billing placeholders
    billing_email text,
    billing_name text,
    billing_status text not null default 'trial'
    check (billing_status in ('trial','active','past_due','cancelled')),

    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create index if not exists idx_tenants_slug on app.tenants(slug);

-- =========================
-- TENANT FEATURES (plan toggles) - 1:1 with tenants
-- Defaults are OFF (site-only baseline)
-- =========================
create table if not exists app.tenant_features (
                                                   tenant_id uuid primary key references app.tenants(id) on delete cascade,

    -- site/whatsapp evolution
    chatbot_enabled boolean not null default false,
    multi_professional_enabled boolean not null default false,
    self_booking_enabled boolean not null default false,
    client_portal_enabled boolean not null default false,

    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

-- =========================
-- USERS (platform identity)
-- Do not store passwords here.
-- auth_subject = provider "sub" (Supabase/Clerk/etc.)
-- =========================
create table if not exists app.users (
                                         id uuid primary key default gen_random_uuid(),
    auth_subject text not null unique,
    email text not null unique,
    full_name text,
    is_platform_admin boolean not null default false,
    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

-- =========================
-- USER ↔ TENANT (roles)
-- =========================
create table if not exists app.user_tenants (
                                                tenant_id uuid not null references app.tenants(id) on delete cascade,
    user_id uuid not null references app.users(id) on delete cascade,
    role text not null check (role in ('tenant_owner','tenant_manager','tenant_staff')),
    created_at timestamptz not null default now(),
    primary key (tenant_id, user_id)
    );

create index if not exists idx_user_tenants_user_id on app.user_tenants(user_id);
create index if not exists idx_user_tenants_tenant_id on app.user_tenants(tenant_id);

-- =========================
-- GLOBAL SERVICE CATALOG (templates)
-- =========================
create table if not exists app.service_catalog (
                                                   id uuid primary key,
                                                   code text not null unique,
                                                   default_duration_minutes int not null check (default_duration_minutes > 0),
    default_price_cents int not null check (default_price_cents >= 0),
    category text not null,
    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create table if not exists app.service_catalog_i18n (
                                                        service_id uuid not null references app.service_catalog(id) on delete cascade,
    lang char(2) not null check (lang in ('en','pt','es')),
    name text not null,
    description text,
    primary key (service_id, lang)
    );

create index if not exists idx_service_catalog_category on app.service_catalog(category);

-- =========================
-- SUGGESTIONS (onboarding / AI hints)
-- =========================
create table if not exists app.service_suggestions (
                                                       service_id uuid primary key references app.service_catalog(id) on delete cascade,
    is_recommended boolean not null default true,
    sort_order int not null default 100,
    score int not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create index if not exists idx_service_suggestions_order
    on app.service_suggestions (is_recommended, sort_order);

create table if not exists app.service_suggestions_i18n (
                                                            service_id uuid not null references app.service_suggestions(service_id) on delete cascade,
    lang char(2) not null check (lang in ('en','pt','es')),
    headline text not null,
    reason text,
    primary key (service_id, lang)
    );

-- =========================
-- TENANT SERVICE ACTIVATION / OVERRIDES
-- Surrogate PK to simplify frontend + appointments FK
-- =========================
create table if not exists app.tenant_services (
                                                   id uuid primary key default gen_random_uuid(),
    tenant_id uuid not null references app.tenants(id) on delete cascade,
    service_id uuid not null references app.service_catalog(id) on delete restrict,

    -- overrides; if null use defaults from service_catalog
    price_cents int check (price_cents is null or price_cents >= 0),
    duration_minutes int check (duration_minutes is null or duration_minutes > 0),

    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz,

    unique (tenant_id, service_id)
    );

create index if not exists idx_tenant_services_tenant on app.tenant_services(tenant_id);
create index if not exists idx_tenant_services_service on app.tenant_services(service_id);

-- =========================
-- PROFESSIONALS (tenant-scoped)
-- Supports:
-- - single-barber focus (is_primary=true)
-- - multi-barber list (sort_order)
-- - future profile routes (public_slug)
-- =========================
create table if not exists app.professionals (
                                                 id uuid primary key default gen_random_uuid(),
    tenant_id uuid not null references app.tenants(id) on delete cascade,

    display_name text not null,
    phone text,

    -- profile photo (MVP: external URL)
    photo_url text,

    -- single vs multi UX
    is_primary boolean not null default false,
    sort_order int not null default 0,
    public_slug text,

    -- optional link to users (consistency between login identity and professional profile)
    user_id uuid references app.users(id) on delete set null,

    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create index if not exists idx_professionals_tenant on app.professionals(tenant_id);

-- One professional profile per user (global uniqueness) when user_id is present
create unique index if not exists uk_professionals_user_id
    on app.professionals(user_id)
    where user_id is not null;

-- At most one primary professional per tenant
create unique index if not exists uq_professionals_primary_per_tenant
    on app.professionals(tenant_id)
    where is_primary = true;

-- Optional: nice URLs per tenant
create unique index if not exists uq_professionals_public_slug_per_tenant
    on app.professionals(tenant_id, public_slug)
    where public_slug is not null;

-- =========================
-- CUSTOMERS (tenant-scoped)
-- phone is NOT unique (formatting/normalization later)
-- =========================
create table if not exists app.customers (
                                             id uuid primary key default gen_random_uuid(),
    tenant_id uuid not null references app.tenants(id) on delete cascade,
    full_name text not null,
    phone text,
    email text,
    city text,

    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create index if not exists idx_customers_tenant on app.customers(tenant_id);
create index if not exists idx_customers_tenant_phone on app.customers(tenant_id, phone);

-- =========================
-- APPOINTMENTS (tenant-scoped)
-- Critical: references tenant_services by tenant_service_id
-- =========================
create table if not exists app.appointments (
                                                id uuid primary key default gen_random_uuid(),
    tenant_id uuid not null references app.tenants(id) on delete cascade,

    customer_id uuid references app.customers(id) on delete set null,
    professional_id uuid references app.professionals(id) on delete set null,

    -- points to effective tenant service (override + defaults)
    tenant_service_id uuid not null references app.tenant_services(id) on delete restrict,

    start_at timestamptz not null,
    end_at timestamptz not null,
    status text not null check (status in ('requested','confirmed','cancelled','completed','no_show')),

    -- snapshot: what was actually charged (optional, but recommended)
    price_cents int check (price_cents is null or price_cents >= 0),
    notes text,

    created_by_user_id uuid references app.users(id) on delete set null,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create index if not exists idx_appt_tenant_start on app.appointments(tenant_id, start_at);
create index if not exists idx_appt_tenant_status on app.appointments(tenant_id, status);
create index if not exists idx_appt_tenant_professional_start
    on app.appointments(tenant_id, professional_id, start_at);

-- Helpful consistency check: end_at must be after start_at
do $$
begin
  if not exists (
    select 1 from pg_constraint c
    join pg_class t on t.oid = c.conrelid
    join pg_namespace n on n.oid = t.relnamespace
    where n.nspname = 'app'
      and t.relname = 'appointments'
      and c.conname = 'ck_appointments_time_range'
  ) then
alter table app.appointments
    add constraint ck_appointments_time_range check (end_at > start_at);
end if;
end $$;

-- =========================
-- PORTFOLIO ITEMS (marketing/branding) - tenant-scoped
-- MVP: store image_url only (no blobs)
-- =========================
create table if not exists app.portfolio_items (
                                                   id uuid primary key default gen_random_uuid(),
    tenant_id uuid not null references app.tenants(id) on delete cascade,

    title text not null,
    description text,
    image_url text,
    sort_order int not null default 0,

    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz
    );

create index if not exists portfolio_items_tenant_idx
    on app.portfolio_items(tenant_id);

create index if not exists portfolio_items_tenant_active_sort_idx
    on app.portfolio_items(tenant_id, is_active, sort_order);

-- =========================
-- OPTIONAL: Service ↔ Professional mapping (prepares Booksy-like selection)
-- If empty for a tenant, frontend/backend can assume "all professionals can do all services"
-- =========================
create table if not exists app.tenant_service_professionals (
                                                                tenant_service_id uuid not null references app.tenant_services(id) on delete cascade,
    professional_id uuid not null references app.professionals(id) on delete cascade,
    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    primary key (tenant_service_id, professional_id)
    );

create index if not exists idx_tsp_professional
    on app.tenant_service_professionals(professional_id);

create index if not exists idx_tsp_tenant_service
    on app.tenant_service_professionals(tenant_service_id);
