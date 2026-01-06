-- V2__seed.sql (trecho sugerido)

-- 1) Tenant: Marriel Barber (deterministic UUID)
insert into app.tenants (
    id, slug, name, phone,
    address_line1, address_line2, city, state, zip, country, timezone,
    logo_url, billing_status, is_active
) values (
             '11111111-1111-1111-1111-111111111111',
             'marriel-barber',
             'Marriel Barber',
             '2034173905',
             '36 Federal Rd',
             null,
             'Danbury',
             'CT',
             '06804',
             'US',
             'America/New_York',
             null,          -- TODO: set a real logo URL later (Supabase Storage / S3)
             'trial',
             true
         )
    on conflict (id) do nothing;

-- 2) Seed a small service catalog (global templates)
-- Keep IDs deterministic. Codes are stable.
insert into app.service_catalog (id, code, default_duration_minutes, default_price_cents, category, is_active)
values
    ('22222222-2222-2222-2222-222222222201', 'HAIRCUT_MEN',         30, 3500, 'haircut', true),
    ('22222222-2222-2222-2222-222222222202', 'BEARD_TRIM',          20, 2500, 'beard',   true),
    ('22222222-2222-2222-2222-222222222203', 'HAIRCUT_AND_BEARD',   50, 5500, 'combo',   true),
    ('22222222-2222-2222-2222-222222222204', 'KIDS_HAIRCUT',        25, 3000, 'haircut', true),
    ('22222222-2222-2222-2222-222222222205', 'LINEUP',             15, 1500, 'beard',   true)
    on conflict (id) do nothing;

-- i18n for catalog (pt/en/es minimal)
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
                                                                               ('22222222-2222-2222-2222-222222222205','es','Line-up','Acabado y alineación.')
    on conflict do nothing;

-- 3) Suggestions (what the UI shows as recommended)
insert into app.service_suggestions (service_id, is_recommended, sort_order, score)
values
    ('22222222-2222-2222-2222-222222222201', true,  10, 90),
    ('22222222-2222-2222-2222-222222222203', true,  20, 85),
    ('22222222-2222-2222-2222-222222222202', true,  30, 80),
    ('22222222-2222-2222-2222-222222222205', true,  40, 70),
    ('22222222-2222-2222-2222-222222222204', true,  50, 60)
    on conflict (service_id) do nothing;

insert into app.service_suggestions_i18n (service_id, lang, headline, reason) values
                                                                                  ('22222222-2222-2222-2222-222222222201','en','Most booked','Great default service to start.'),
                                                                                  ('22222222-2222-2222-2222-222222222201','pt','Mais pedido','Ótimo serviço padrão para começar.'),
                                                                                  ('22222222-2222-2222-2222-222222222201','es','Más pedido','Excelente opción para comenzar.'),

                                                                                  ('22222222-2222-2222-2222-222222222203','en','Best value combo','Haircut + beard in one slot.'),
                                                                                  ('22222222-2222-2222-2222-222222222203','pt','Combo com melhor custo','Corte + barba no mesmo horário.'),
                                                                                  ('22222222-2222-2222-2222-222222222203','es','Mejor combo','Corte + barba en un solo turno.')
    on conflict do nothing;

-- 4) Activate services for Marriel tenant (overrides optional)
insert into app.tenant_services (tenant_id, service_id, price_cents, duration_minutes, is_active)
values
    ('11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222201', null, null, true),
    ('11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222202', null, null, true),
    ('11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222203', null, null, true),
    ('11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222204', null, null, true),
    ('11111111-1111-1111-1111-111111111111','22222222-2222-2222-2222-222222222205', null, null, true)
    on conflict do nothing;

-- 5) Professional (basic)
insert into app.professionals (id, tenant_id, display_name, phone, photo_url, is_active)
values (
           '33333333-3333-3333-3333-333333333333',
           '11111111-1111-1111-1111-111111111111',
           'Marriel',
           '2034173905',
           null, -- TODO: set a real profile photo URL later
           true
       )
    on conflict (id) do nothing;

-- 6) Portfolio items (use external URLs for MVP)
-- Instagram post URL may not be a direct image; keep as link for now.
insert into app.portfolio_items (id, tenant_id, title, description, image_url, is_featured, sort_order)
values
    ('44444444-4444-4444-4444-444444444401','11111111-1111-1111-1111-111111111111',
     'Corte e acabamento (Instagram)',
     'Reference from public Instagram post.',
     'https://www.instagram.com/p/DJ-XgxwsLqw/',
     true,
     10
    )
    on conflict (id) do nothing;
