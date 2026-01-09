-- V2__seed.sql
-- Deterministic seed data for demos and UI mocking
-- Includes:
-- - 1 tenant (Marriel)
-- - tenant_features (defaults OFF)
-- - service catalog (global)
-- - i18n
-- - suggestions (+i18n)
-- - tenant_services activation (with deterministic ids)
-- - 1 professional (primary)
-- - 1 portfolio item

-- 1) Tenant: Marriel Barber (deterministic UUID)
insert into app.tenants (
    id, slug, name, default_lang,
    phone,
    address_line1, address_line2, city, state, zip, country, timezone,
    logo_url,
    whatsapp_phone_e164, whatsapp_message_template,
    instagram_url, google_maps_url, website_url,
    billing_status, is_active
) values (
             '11111111-1111-1111-1111-111111111111',
             'marriel-barber',
             'Marriel Barber',
             'pt',
             '2034173905',
             '36 Federal Rd',
             null,
             'Danbury',
             'CT',
             '06804',
             'US',
             'America/New_York',
             null,
             '+12034173905',
             'Oi! Vim pelo site e gostaria de informações / agendar um horário.',
             null,
             null,
             null,
             'trial',
             true
         )
    on conflict (id) do nothing;

-- 1.1) Features: everything OFF by default (site-first)
insert into app.tenant_features (
    tenant_id,
    chatbot_enabled,
    multi_professional_enabled,
    self_booking_enabled,
    client_portal_enabled
) values (
             '11111111-1111-1111-1111-111111111111',
             false,
             false,
             false,
             false
         )
    on conflict (tenant_id) do nothing;

-- 2) Seed service catalog (global templates) - deterministic IDs
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category, is_active)
values
    ('22222222-2222-2222-2222-222222222201', 'HAIRCUT_MEN',           30, 3500, 'haircut', true),
    ('22222222-2222-2222-2222-222222222202', 'BEARD_TRIM',            20, 2500, 'beard',   true),
    ('22222222-2222-2222-2222-222222222203', 'HAIRCUT_AND_BEARD',     50, 5500, 'combo',   true),
    ('22222222-2222-2222-2222-222222222204', 'KIDS_HAIRCUT',          25, 3000, 'haircut', true),
    ('22222222-2222-2222-2222-222222222205', 'LINEUP',               15, 1500, 'beard',   true),

    -- Example: single service combining multiple actions (1h30, $90)
    ('22222222-2222-2222-2222-222222222206', 'HAIR_BEARD_SKIN_CLEAN', 90, 9000, 'premium', true
    )
    on conflict (id) do nothing;

-- 2.1) i18n for catalog (en/pt/es)
insert into app.service_catalog_i18n (service_id, lang, name, description) values
                                                                               ('22222222-2222-2222-2222-222222222201','en','Men Haircut','Classic men haircut.'),
                                                                               ('22222222-2222-2222-2222-222222222201','pt','Corte Masculino','Corte masculino clássico.'),
                                                                               ('22222222-2222-2222-2222-222222222201','es','Corte Masculino','Corte clásico para hombres.'),

                                                                               ('22222222-2222-2222-2222-222222222202','en','Beard Trim','Beard trim and shaping.'),
                                                                               ('22222222-2222-2222-2222-222222222202','pt','Barba','Aparar e desenhar barba.'),
                                                                               ('22222222-2222-2222-2222-222222222202','es','Barba','Recorte y diseño de barba.'),

                                                                               ('22222222-2222-2222-2222-222222222203','en','Haircut + Beard','Combo haircut and beard.'),
                                                                               ('22222222-2222-2222-2222-222222222203','pt','Corte + Barba','Combo de corte e barba.'),
                                                                               ('22222222-2222-2222-2222-222222222203','es','Corte + Barba','Combo de corte y barba.'),

                                                                               ('22222222-2222-2222-2222-222222222204','en','Kids Haircut','Kids haircut (up to 12).'),
                                                                               ('22222222-2222-2222-2222-222222222204','pt','Corte Infantil','Corte infantil (até 12).'),
                                                                               ('22222222-2222-2222-2222-222222222204','es','Corte Infantil','Corte infantil (hasta 12).'),

                                                                               ('22222222-2222-2222-2222-222222222205','en','Line-up','Hairline / edge-up detailing.'),
                                                                               ('22222222-2222-2222-2222-222222222205','pt','Pezinho / Acabamento','Acabamento e alinhamento.'),
                                                                               ('22222222-2222-2222-2222-222222222205','es','Line-up','Acabado y alineación.'),

                                                                               ('22222222-2222-2222-2222-222222222206','en','Haircut + Beard + Skin Clean','Premium combo service (90 min).'),
                                                                               ('22222222-2222-2222-2222-222222222206','pt','Cabelo + Barba + Limpeza de Pele','Combo premium (90 min).'),
                                                                               ('22222222-2222-2222-2222-222222222206','es','Corte + Barba + Limpieza de Piel','Combo premium (90 min).')
    on conflict do nothing;

-- 3) Suggestions (what the UI shows as recommended)
insert into app.service_suggestions (service_id, is_recommended, sort_order, score)
values
    ('22222222-2222-2222-2222-222222222201', true,  10, 90),
    ('22222222-2222-2222-2222-222222222203', true,  20, 85),
    ('22222222-2222-2222-2222-222222222202', true,  30, 80),
    ('22222222-2222-2222-2222-222222222205', true,  40, 70),
    ('22222222-2222-2222-2222-222222222204', true,  50, 60),
    ('22222222-2222-2222-2222-222222222206', true,   5, 95)
    on conflict (service_id) do nothing;

insert into app.service_suggestions_i18n (service_id, lang, headline, reason) values
                                                                                  ('22222222-2222-2222-2222-222222222201','en','Most booked','Great default service to start.'),
                                                                                  ('22222222-2222-2222-2222-222222222201','pt','Mais pedido','Ótimo serviço padrão para começar.'),
                                                                                  ('22222222-2222-2222-2222-222222222201','es','Más pedido','Excelente opción para comenzar.'),

                                                                                  ('22222222-2222-2222-2222-222222222203','en','Best value combo','Haircut + beard in one slot.'),
                                                                                  ('22222222-2222-2222-2222-222222222203','pt','Combo com melhor custo','Corte + barba no mesmo horário.'),
                                                                                  ('22222222-2222-2222-2222-222222222203','es','Mejor combo','Corte + barba en un solo turno.'),

                                                                                  ('22222222-2222-2222-2222-222222222206','en','Premium combo','A complete 90-minute premium service.'),
                                                                                  ('22222222-2222-2222-2222-222222222206','pt','Combo premium','Um serviço premium completo de 90 minutos.'),
                                                                                  ('22222222-2222-2222-2222-222222222206','es','Combo premium','Un servicio premium completo de 90 minutos.')
    on conflict do nothing;

-- 4) Activate services for Marriel tenant (tenant_services with deterministic IDs)
-- If price_cents/duration_minutes are NULL, defaults from service_catalog apply.
insert into app.tenant_services (id, tenant_id, service_id, price_cents, duration_minutes, is_active)
values
    ('55555555-5555-5555-5555-555555555501','11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222201', null, null, true),
    ('55555555-5555-5555-5555-555555555502','11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222202', null, null, true),
    ('55555555-5555-5555-5555-555555555503','11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222203', null, null, true),
    ('55555555-5555-5555-5555-555555555504','11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222204', null, null, true),
    ('55555555-5555-5555-5555-555555555505','11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222205', null, null, true),
    ('55555555-5555-5555-5555-555555555506','11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222206', null, null, true)
    on conflict (tenant_id, service_id) do nothing;

-- 5) Professional (primary)
insert into app.professionals (
    id, tenant_id, display_name, phone, photo_url,
    is_primary, sort_order, public_slug,
    user_id, is_active
) values (
             '33333333-3333-3333-3333-333333333333',
             '11111111-1111-1111-1111-111111111111',
             'Marriel',
             '2034173905',
             null,
             true,
             10,
             'marriel',
             null,
             true
         )
    on conflict (id) do nothing;

-- 6) Portfolio item (basic)
insert into app.portfolio_items (
    id, tenant_id, title, description, image_url, sort_order, is_active
) values (
             '44444444-4444-4444-4444-444444444444',
             '11111111-1111-1111-1111-111111111111',
             'Classic Fade',
             'Corte clássico com degradê. (Demo item)',
             null,
             10,
             true
         )
    on conflict (id) do nothing;
