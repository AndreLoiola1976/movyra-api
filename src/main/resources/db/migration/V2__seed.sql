-- V2__seed.sql
-- Seeds idempotentes: platform admin, showcase tenant (para landing),
-- demo tenant, catálogo + i18n, sugestões, ativações e portfólio padrão.

-- PLATFORM ADMIN (placeholder auth_subject for now)
insert into app.users (id, auth_subject, email, full_name, is_platform_admin)
values ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'local-admin-sub', 'andre@movyra.ai', 'Andre Loiola', true)
    on conflict (email) do update
                               set is_platform_admin = true,
                               full_name = excluded.full_name;

-- =========================
-- SHOWCASE TENANT (para o site público antes do 1º cliente)
-- =========================
insert into app.tenants (id, slug, name, timezone, billing_status, is_active, logo_url, city, state, country)
values (
           'cccccccc-cccc-cccc-cccc-cccccccccccc',
           'movyra-showcase',
           'Movyra Showcase Barbershop',
           'America/New_York',
           'trial',
           true,
           'https://upload.wikimedia.org/wikipedia/commons/1/19/Barber_Shop_-_The_Noun_Project.svg',
           'Danbury',
           'CT',
           'US'
       )
    on conflict (slug) do nothing;

-- Você como owner do showcase (útil pra testes do painel admin)
insert into app.user_tenants (tenant_id, user_id, role)
values ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'tenant_owner')
    on conflict (tenant_id, user_id) do nothing;

-- =========================
-- DEMO TENANT (ambiente interno)
-- =========================
insert into app.tenants (id, slug, name, timezone, billing_status, is_active)
values ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'demo-barbershop', 'Demo Barbershop', 'America/New_York', 'trial', true)
    on conflict (slug) do nothing;

insert into app.user_tenants (tenant_id, user_id, role)
values ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'tenant_owner')
    on conflict (tenant_id, user_id) do nothing;

-- =========================
-- PROFESSIONALS (showcase)
-- =========================
insert into app.professionals (id, tenant_id, display_name, phone, photo_url, is_active)
values
    (
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Mike (Showcase)',
        '+1-203-555-0101',
        'https://images.pexels.com/photos/16030378/pexels-photo-16030378.jpeg?cs=srgb&dl=pexels-ali-rezaei-83910116-16030378.jpg&fm=jpg',
        true
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'John (Showcase)',
        '+1-203-555-0102',
        'https://images.pexels.com/photos/2166642/pexels-photo-2166642.jpeg?cs=srgb&dl=pexels-thgusstavo-santana-2166642.jpg&fm=jpg',
        true
    )
    on conflict (id) do nothing;

-- =========================
-- SERVICE CATALOG (12 services) + i18n
-- (mesmo conteúdo que você já tinha)
-- =========================

-- 1) Classic Haircut
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000001', 'HAIRCUT_CLASSIC', 30, 3000, 'haircut')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000001','en','Classic Haircut','Standard men’s haircut'),
    ('10000000-0000-0000-0000-000000000001','pt','Corte Clássico','Corte masculino padrão'),
    ('10000000-0000-0000-0000-000000000001','es','Corte Clásico','Corte masculino estándar')
    on conflict (service_id, lang) do nothing;

-- 2) Skin Fade
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000002', 'HAIRCUT_SKIN_FADE', 45, 4000, 'haircut')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000002','en','Skin Fade','Fade haircut with skin finish'),
    ('10000000-0000-0000-0000-000000000002','pt','Degradê Navalhado','Degradê com acabamento na pele'),
    ('10000000-0000-0000-0000-000000000002','es','Degradado a Cero','Degradado con acabado a piel')
    on conflict (service_id, lang) do nothing;

-- 3) Beard Trim
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000003', 'BEARD_TRIM', 20, 2000, 'beard')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000003','en','Beard Trim','Beard shaping and trim'),
    ('10000000-0000-0000-0000-000000000003','pt','Barba','Modelagem e acabamento da barba'),
    ('10000000-0000-0000-0000-000000000003','es','Barba','Perfilado y recorte de barba')
    on conflict (service_id, lang) do nothing;

-- 4) Haircut + Beard
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000004', 'COMBO_HAIRCUT_BEARD', 60, 5500, 'combo')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000004','en','Haircut + Beard','Combo service: haircut and beard'),
    ('10000000-0000-0000-0000-000000000004','pt','Corte + Barba','Serviço combinado: corte e barba'),
    ('10000000-0000-0000-0000-000000000004','es','Corte + Barba','Servicio combinado: corte y barba')
    on conflict (service_id, lang) do nothing;

-- 5) Line Up
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000005', 'LINE_UP', 15, 1500, 'beard')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000005','en','Line Up','Edge up / shape up'),
    ('10000000-0000-0000-0000-000000000005','pt','Pezinho','Acabamento / contorno'),
    ('10000000-0000-0000-0000-000000000005','es','Perfilado','Contorno / line up')
    on conflict (service_id, lang) do nothing;

-- 6) Kids Haircut
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000006', 'HAIRCUT_KIDS', 30, 2500, 'haircut')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000006','en','Kids Haircut','Haircut for kids'),
    ('10000000-0000-0000-0000-000000000006','pt','Corte Infantil','Corte para crianças'),
    ('10000000-0000-0000-0000-000000000006','es','Corte Infantil','Corte para niños')
    on conflict (service_id, lang) do nothing;

-- 7) Eyebrows
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000007', 'EYEBROWS', 10, 1200, 'extras')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000007','en','Eyebrows','Eyebrow cleanup'),
    ('10000000-0000-0000-0000-000000000007','pt','Sobrancelha','Acabamento de sobrancelha'),
    ('10000000-0000-0000-0000-000000000007','es','Cejas','Arreglo de cejas')
    on conflict (service_id, lang) do nothing;

-- 8) Wash + Style
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000008', 'WASH_STYLE', 20, 2000, 'extras')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000008','en','Wash + Style','Shampoo and basic styling'),
    ('10000000-0000-0000-0000-000000000008','pt','Lavar + Finalizar','Lavagem e finalização básica'),
    ('10000000-0000-0000-0000-000000000008','es','Lavar + Peinar','Lavado y peinado básico')
    on conflict (service_id, lang) do nothing;

-- 9) Hot Towel
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000009', 'HOT_TOWEL', 10, 1000, 'extras')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000009','en','Hot Towel','Hot towel treatment'),
    ('10000000-0000-0000-0000-000000000009','pt','Toalha Quente','Tratamento com toalha quente'),
    ('10000000-0000-0000-0000-000000000009','es','Toalla Caliente','Tratamiento con toalla caliente')
    on conflict (service_id, lang) do nothing;

-- 10) Razor Shave
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000010', 'SHAVE_RAZOR', 30, 3000, 'beard')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000010','en','Razor Shave','Clean shave with razor'),
    ('10000000-0000-0000-0000-000000000010','pt','Barbear Navalha','Barba feita na navalha'),
    ('10000000-0000-0000-0000-000000000010','es','Afeitado a Navaja','Afeitado con navaja')
    on conflict (service_id, lang) do nothing;

-- 11) Hair Design
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000011', 'HAIR_DESIGN', 20, 2500, 'extras')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000011','en','Hair Design','Hair design / lines'),
    ('10000000-0000-0000-0000-000000000011','pt','Risco / Design','Desenho no cabelo'),
    ('10000000-0000-0000-0000-000000000011','es','Diseño','Diseño / líneas en el cabello')
    on conflict (service_id, lang) do nothing;

-- 12) Consultation
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category)
values ('10000000-0000-0000-0000-000000000012', 'CONSULTATION', 10, 0, 'extras')
    on conflict (code) do nothing;

insert into app.service_catalog_i18n (service_id, lang, name, description)
values
    ('10000000-0000-0000-0000-000000000012','en','Consultation','Quick consultation'),
    ('10000000-0000-0000-0000-000000000012','pt','Consulta','Consulta rápida'),
    ('10000000-0000-0000-0000-000000000012','es','Consulta','Consulta rápida')
    on conflict (service_id, lang) do nothing;

-- =========================
-- SUGGESTIONS (all 12 recommended)
-- =========================
insert into app.service_suggestions (service_id, is_recommended, sort_order, score)
values
    ('10000000-0000-0000-0000-000000000001', true, 10, 80),
    ('10000000-0000-0000-0000-000000000002', true, 20, 85),
    ('10000000-0000-0000-0000-000000000003', true, 30, 70),
    ('10000000-0000-0000-0000-000000000004', true, 40, 75),
    ('10000000-0000-0000-0000-000000000005', true, 50, 55),
    ('10000000-0000-0000-0000-000000000006', true, 60, 45),
    ('10000000-0000-0000-0000-000000000007', true, 70, 35),
    ('10000000-0000-0000-0000-000000000008', true, 80, 40),
    ('10000000-0000-0000-0000-000000000009', true, 90, 30),
    ('10000000-0000-0000-0000-000000000010', true, 100, 50),
    ('10000000-0000-0000-0000-000000000011', true, 110, 25),
    ('10000000-0000-0000-0000-000000000012', true, 120, 10)
    on conflict (service_id) do update
                                    set is_recommended = excluded.is_recommended,
                                    sort_order = excluded.sort_order,
                                    score = excluded.score;

insert into app.service_suggestions_i18n (service_id, lang, headline, reason)
values
-- EN
('10000000-0000-0000-0000-000000000001','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000002','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000003','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000004','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000005','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000006','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000007','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000008','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000009','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000010','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000011','en','Recommended','Commonly offered by barbershops'),
('10000000-0000-0000-0000-000000000012','en','Recommended','Commonly offered by barbershops'),
-- PT
('10000000-0000-0000-0000-000000000001','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000002','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000003','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000004','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000005','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000006','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000007','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000008','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000009','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000010','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000011','pt','Recomendado','Serviço comum em barbearias'),
('10000000-0000-0000-0000-000000000012','pt','Recomendado','Serviço comum em barbearias'),
-- ES
('10000000-0000-0000-0000-000000000001','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000002','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000003','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000004','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000005','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000006','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000007','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000008','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000009','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000010','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000011','es','Recomendado','Servicio común en barberías'),
('10000000-0000-0000-0000-000000000012','es','Recomendado','Servicio común en barberías')
    on conflict (service_id, lang) do update
                                          set headline = excluded.headline,
                                          reason = excluded.reason;

-- =========================
-- Showcase Portfolio (landing)
-- =========================
insert into app.portfolio_items (tenant_id, title, description, image_url, is_featured, sort_order)
values
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Skin Fade + Razor Line',
        'Clean fade with razor lineup finish.',
        'https://images.pexels.com/photos/2166642/pexels-photo-2166642.jpeg?cs=srgb&dl=pexels-thgusstavo-santana-2166642.jpg&fm=jpg',
        true,
        10
    ),
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Beard Trim (Straight Razor)',
        'Precision beard shaping with straight razor.',
        'https://images.pexels.com/photos/3992876/pexels-photo-3992876.jpeg?cs=srgb&dl=pexels-thgusstavo-santana-3992876.jpg&fm=jpg',
        true,
        20
    ),
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Classic Barbershop Session',
        'A professional cut in a modern barbershop environment.',
        'https://images.pexels.com/photos/1813272/pexels-photo-1813272.jpeg?cs=srgb&dl=pexels-thgusstavo-santana-1813272.jpg&fm=jpg',
        false,
        30
    ),
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Beard Design / Detail Work',
        'Detail work for beard lines and shape.',
        'https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg?cs=srgb&dl=pexels-thgusstavo-santana-1018911.jpg&fm=jpg',
        false,
        40
    )
    on conflict do nothing;

-- =========================
-- OPTIONAL: activate a couple services for demo tenant (for immediate UI tests)
-- =========================
insert into app.tenant_services (tenant_id, service_id, price_cents, duration_minutes, is_active)
values
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','10000000-0000-0000-0000-000000000001', 3000, 30, true),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','10000000-0000-0000-0000-000000000003', 2000, 20, true)
    on conflict (tenant_id, service_id) do nothing;
