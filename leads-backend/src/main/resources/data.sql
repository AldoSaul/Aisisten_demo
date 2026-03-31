-- ================================================================
-- DEV seed data — loaded by Spring Boot after Hibernate init
-- Uses INSERT IGNORE so restarts are safe (no duplicate errors)
-- ================================================================

-- ── Tenants ──────────────────────────────────────────────────
INSERT IGNORE INTO tenants (id, nombre, facebook_page_id, instagram_account_id, whatsapp_phone_number_id, access_token, token_expira, activo) VALUES
(1, 'Agencia Digital Nexus',  'page_001', 'ig_001', 'wa_001', 'demo_token_nexus',   DATE_ADD(NOW(), INTERVAL 55 DAY), 1),
(2, 'Restaurante Don Tacos',  'page_002', 'ig_002', 'wa_002', 'demo_token_tacos',   DATE_ADD(NOW(), INTERVAL 45 DAY), 1),
(3, 'Clínica Salud Total',    'page_003', 'ig_003', 'wa_003', 'demo_token_clinica', DATE_ADD(NOW(), INTERVAL 12 DAY), 1);

-- ── Leads ─────────────────────────────────────────────────────
INSERT IGNORE INTO leads (id, sender_id, channel, nombre, email, telefono, tenant_id) VALUES
(1, '521234567890', 'WHATSAPP',  'Carlos Mendoza',   'carlos@gmail.com',   '+52 1 234 567 890', 1),
(2, 'ig_user_4421', 'INSTAGRAM', 'Sofía Ramírez',    'sofia.r@hotmail.com','',                  1),
(3, 'fb_user_8812', 'FACEBOOK',  'Andrés Torres',    '',                   '',                  1),
(4, '521112223333', 'WHATSAPP',  'Valeria Cruz',     'vale.cruz@gmail.com','+52 1 111 222 333', 1),
(5, 'ig_user_9934', 'INSTAGRAM', 'Diego Hernández',  '',                   '',                  1),
(6, '524445556666', 'WHATSAPP',  'María López',      'maria@yahoo.com',    '+52 4 445 556 666', 2),
(7, 'fb_user_3301', 'FACEBOOK',  'Roberto Gómez',    '',                   '',                  2),
(8, 'ig_user_2277', 'INSTAGRAM', 'Fernanda Ruiz',    'fer.ruiz@gmail.com', '',                  2);

-- ── Conversations ─────────────────────────────────────────────
INSERT IGNORE INTO conversations (id, lead_id, tenant_id, channel, last_message_at, unread_count, archivada) VALUES
(1, 1, 1, 'WHATSAPP',  DATE_SUB(NOW(), INTERVAL 5  MINUTE),  3, 0),
(2, 2, 1, 'INSTAGRAM', DATE_SUB(NOW(), INTERVAL 20 MINUTE),  1, 0),
(3, 3, 1, 'FACEBOOK',  DATE_SUB(NOW(), INTERVAL 2  HOUR),    0, 0),
(4, 4, 1, 'WHATSAPP',  DATE_SUB(NOW(), INTERVAL 3  HOUR),    0, 0),
(5, 5, 1, 'INSTAGRAM', DATE_SUB(NOW(), INTERVAL 1  DAY),     0, 0),
(6, 6, 2, 'WHATSAPP',  DATE_SUB(NOW(), INTERVAL 10 MINUTE),  2, 0),
(7, 7, 2, 'FACEBOOK',  DATE_SUB(NOW(), INTERVAL 45 MINUTE),  0, 0),
(8, 8, 2, 'INSTAGRAM', DATE_SUB(NOW(), INTERVAL 2  DAY),     0, 1);

-- ── Messages ─────────────────────────────────────────────────
INSERT IGNORE INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_wa_001', 1, 'WHATSAPP', 'Hola! Vi su publicación sobre diseño web', 'READ', 1, DATE_SUB(NOW(), INTERVAL 62 MINUTE)),
('ext_wa_002', 1, 'WHATSAPP', 'Buenos días Carlos, claro que sí. ¿Qué tipo de proyecto tienes en mente?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 58 MINUTE)),
('ext_wa_003', 1, 'WHATSAPP', 'Necesito una tienda en línea para vender ropa', 'READ', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('ext_wa_004', 1, 'WHATSAPP', 'Perfecto, tenemos paquetes desde $8,000 MXN. ¿Te envío el catálogo?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
('ext_wa_005', 1, 'WHATSAPP', 'Sí por favor!', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
('ext_wa_006', 1, 'WHATSAPP', 'También quiero saber si hacen apps móviles', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 7 MINUTE)),
('ext_wa_007', 1, 'WHATSAPP', 'Cuánto tiempo tardan en entregar?', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
('ext_ig_001', 2, 'INSTAGRAM', 'Hola! Vi su story y me interesa el servicio de redes sociales', 'READ', 1, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
('ext_ig_002', 2, 'INSTAGRAM', 'Hola Sofía! Con gusto te informamos. ¿Manejas alguna red actualmente?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 35 MINUTE)),
('ext_ig_003', 2, 'INSTAGRAM', 'Solo Instagram y TikTok, pero quiero crecer más rápido', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
('ext_fb_001', 3, 'FACEBOOK', 'Buenos días, ¿manejan campañas de Google Ads?', 'READ', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_fb_002', 3, 'FACEBOOK', 'Sí Andrés, manejamos Google Ads, Meta Ads y SEO. ¿Cuál es tu presupuesto mensual?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('ext_fb_003', 3, 'FACEBOOK', 'Tengo alrededor de $5,000 MXN al mes', 'READ', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('ext_fb_004', 3, 'FACEBOOK', 'Es un buen punto de partida. Te agendo una llamada con nuestro especialista?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 118 MINUTE)),
('ext_wa_010', 4, 'WHATSAPP', 'Necesito un logo para mi negocio', 'READ', 1, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('ext_wa_011', 4, 'WHATSAPP', 'Hola Valeria! Claro, manejamos diseño de identidad. ¿De qué giro es tu negocio?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_wa_012', 4, 'WHATSAPP', 'Vendo pasteles y postres artesanales', 'READ', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_wa_013', 4, 'WHATSAPP', 'Qué bonito! Te mando ejemplos de nuestro portafolio de branding food', 'READ', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_ig_010', 5, 'INSTAGRAM', 'Cuánto cuesta manejar mi Instagram?', 'READ', 1, DATE_SUB(NOW(), INTERVAL 25 HOUR)),
('ext_ig_011', 5, 'INSTAGRAM', 'Hola Diego! Depende del plan. Los precios van desde $3,500/mes. ¿Quieres un presupuesto?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
('ext_ig_012', 5, 'INSTAGRAM', 'Sí, mándame info por favor', 'READ', 1, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
('ext_wa_020', 6, 'WHATSAPP', 'Hola! Quiero apartar mesa para 8 personas el sábado', 'READ', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('ext_wa_021', 6, 'WHATSAPP', 'Claro María! ¿A qué hora sería?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
('ext_wa_022', 6, 'WHATSAPP', 'A las 2pm', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 12 MINUTE)),
('ext_wa_023', 6, 'WHATSAPP', 'Tienen estacionamiento?', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
('ext_fb_020', 7, 'FACEBOOK', 'Tienen opción sin gluten en el menú?', 'READ', 1, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
('ext_fb_021', 7, 'FACEBOOK', 'Hola Roberto! Sí contamos con opciones sin gluten. ¿Cuáles te interesan?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 45 MINUTE)),
('ext_fb_022', 7, 'FACEBOOK', 'Qué bueno! Me interesan los tacos y las quesadillas', 'READ', 1, DATE_SUB(NOW(), INTERVAL 44 MINUTE));
