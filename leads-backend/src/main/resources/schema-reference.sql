-- ============================================================
-- LEADS_DB — Schema completo
-- Motor: MySQL 8.x   Charset: utf8mb4
-- ============================================================

CREATE DATABASE IF NOT EXISTS leads_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE leads_db;

-- ── Tenants (negocios conectados) ───────────────────────────
CREATE TABLE tenants (
  id                      BIGINT        NOT NULL AUTO_INCREMENT,
  nombre                  VARCHAR(255)  NOT NULL,
  facebook_page_id        VARCHAR(100)  UNIQUE,
  instagram_account_id    VARCHAR(100)  UNIQUE,
  whatsapp_phone_number_id VARCHAR(100) UNIQUE,
  access_token            VARCHAR(1000),
  token_expira            DATETIME,
  activo                  TINYINT(1)   NOT NULL DEFAULT 1,
  created_at              DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at              DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Users (login web/app) ────────────────────────────────────
CREATE TABLE users (
  id            BIGINT        NOT NULL AUTO_INCREMENT,
  email         VARCHAR(255)  NOT NULL,
  password_hash VARCHAR(255)  NOT NULL,
  role          ENUM('ADMIN','AGENT') NOT NULL,
  tenant_id     BIGINT        NOT NULL,
  active        TINYINT(1)    NOT NULL DEFAULT 1,
  created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_email (email),
  KEY idx_users_tenant (tenant_id),
  CONSTRAINT fk_users_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Leads (personas que escriben) ───────────────────────────
CREATE TABLE leads (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  sender_id       VARCHAR(100) NOT NULL,
  channel         ENUM('WHATSAPP','INSTAGRAM','FACEBOOK') NOT NULL,
  nombre          VARCHAR(255),
  email           VARCHAR(255),
  telefono        VARCHAR(50),
  profile_pic_url VARCHAR(500),
  tenant_id       BIGINT       NOT NULL,
  created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_lead_sender_channel_tenant (sender_id, channel, tenant_id),
  CONSTRAINT fk_leads_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Conversations ────────────────────────────────────────────
CREATE TABLE conversations (
  id              BIGINT   NOT NULL AUTO_INCREMENT,
  lead_id         BIGINT   NOT NULL,
  tenant_id       BIGINT   NOT NULL,
  channel         ENUM('WHATSAPP','INSTAGRAM','FACEBOOK') NOT NULL,
  last_message_at DATETIME,
  unread_count    INT      NOT NULL DEFAULT 0,
  archivada       TINYINT(1) NOT NULL DEFAULT 0,
  created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_conv_tenant_last (tenant_id, last_message_at DESC),
  INDEX idx_conv_channel (channel),
  CONSTRAINT fk_conv_lead   FOREIGN KEY (lead_id)   REFERENCES leads(id),
  CONSTRAINT fk_conv_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Messages ─────────────────────────────────────────────────
CREATE TABLE messages (
  id                  BIGINT        NOT NULL AUTO_INCREMENT,
  external_message_id VARCHAR(255)  UNIQUE,
  conversation_id     BIGINT        NOT NULL,
  channel             ENUM('WHATSAPP','INSTAGRAM','FACEBOOK') NOT NULL,
  contenido           TEXT,
  media_url           VARCHAR(500),
  media_type          VARCHAR(50),
  status              ENUM('RECEIVED','PROCESSED','READ','REPLIED') NOT NULL DEFAULT 'RECEIVED',
  es_entrante         TINYINT(1)   NOT NULL DEFAULT 1,
  sent_at             DATETIME,
  created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_msg_conv_created (conversation_id, created_at ASC),
  CONSTRAINT fk_msg_conv FOREIGN KEY (conversation_id) REFERENCES conversations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Usuario de aplicación (permisos mínimos) ─────────────────
-- Ejecutar como root:
-- CREATE USER 'leads_user'@'%' IDENTIFIED BY 'TU_PASSWORD_AQUI';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON leads_db.* TO 'leads_user'@'%';
-- FLUSH PRIVILEGES;
SELECT * FROM messages;
SELECT * FROM conversations;
SELECT * FROM leads;
SELECT * FROM tenants;
SELECT * FROM users;





-- ============================================================
-- DATOS DEMO — Leads Platform
-- Ejecutar después de schema.sql
-- ============================================================
 
USE leads_db;
 
-- ── Tenants ──────────────────────────────────────────────────
INSERT INTO tenants (nombre, facebook_page_id, instagram_account_id, whatsapp_phone_number_id, access_token, token_expira, activo) VALUES
('Agencia Digital Nexus',   'page_001', 'ig_001', 'wa_001', 'demo_token_nexus',   DATE_ADD(NOW(), INTERVAL 55 DAY), 1),
('Restaurante Don Tacos',   'page_002', 'ig_002', 'wa_002', 'demo_token_tacos',   DATE_ADD(NOW(), INTERVAL 45 DAY), 1),
('Clínica Salud Total',     'page_003', 'ig_003', 'wa_003', 'demo_token_clinica', DATE_ADD(NOW(), INTERVAL 12 DAY), 1);

-- ── Users (dev login) ────────────────────────────────────────
INSERT INTO users (email, password_hash, role, tenant_id, active, created_at, updated_at) VALUES
('admin@admin.com', '$2a$10$LgjiXX.UeJO6En1d.ooyB.1xM47dNv2D8pl6Vp44FfXo2AZnhKOXO', 'ADMIN', 1, 1, NOW(), NOW());
 
-- ── Leads ─────────────────────────────────────────────────────
INSERT INTO leads (sender_id, channel, nombre, email, telefono, tenant_id) VALUES
-- Tenant 1 — Nexus
('521234567890', 'WHATSAPP',  'Carlos Mendoza',   'carlos@gmail.com',   '+52 1 234 567 890', 1),
('ig_user_4421', 'INSTAGRAM', 'Sofía Ramírez',    'sofia.r@hotmail.com','',                  1),
('fb_user_8812', 'FACEBOOK',  'Andrés Torres',    '',                   '',                  1),
('521112223333', 'WHATSAPP',  'Valeria Cruz',     'vale.cruz@gmail.com','+52 1 111 222 333', 1),
('ig_user_9934', 'INSTAGRAM', 'Diego Hernández',  '',                   '',                  1),
-- Tenant 2 — Don Tacos
('524445556666', 'WHATSAPP',  'María López',      'maria@yahoo.com',    '+52 4 445 556 666', 2),
('fb_user_3301', 'FACEBOOK',  'Roberto Gómez',    '',                   '',                  2),
('ig_user_2277', 'INSTAGRAM', 'Fernanda Ruiz',    'fer.ruiz@gmail.com', '',                  2);
 
-- ── Conversations ─────────────────────────────────────────────
INSERT INTO conversations (lead_id, tenant_id, channel, last_message_at, unread_count, archivada) VALUES
(1, 1, 'WHATSAPP',  DATE_SUB(NOW(), INTERVAL 5  MINUTE),  3, 0),
(2, 1, 'INSTAGRAM', DATE_SUB(NOW(), INTERVAL 20 MINUTE),  1, 0),
(3, 1, 'FACEBOOK',  DATE_SUB(NOW(), INTERVAL 2  HOUR),    0, 0),
(4, 1, 'WHATSAPP',  DATE_SUB(NOW(), INTERVAL 3  HOUR),    0, 0),
(5, 1, 'INSTAGRAM', DATE_SUB(NOW(), INTERVAL 1  DAY),     0, 0),
(6, 2, 'WHATSAPP',  DATE_SUB(NOW(), INTERVAL 10 MINUTE),  2, 0),
(7, 2, 'FACEBOOK',  DATE_SUB(NOW(), INTERVAL 45 MINUTE),  0, 0),
(8, 2, 'INSTAGRAM', DATE_SUB(NOW(), INTERVAL 2  DAY),     0, 1);  -- archivada
 
-- ── Messages ─────────────────────────────────────────────────
-- Conversación 1 — Carlos / WhatsApp (activa, 3 no leídos)
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_wa_001', 1, 'WHATSAPP', 'Hola! Vi su publicación sobre diseño web', 'READ', 1, DATE_SUB(NOW(), INTERVAL 62 MINUTE)),
('ext_wa_002', 1, 'WHATSAPP', 'Buenos días Carlos, claro que sí. ¿Qué tipo de proyecto tienes en mente?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 58 MINUTE)),
('ext_wa_003', 1, 'WHATSAPP', 'Necesito una tienda en línea para vender ropa', 'READ', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('ext_wa_004', 1, 'WHATSAPP', 'Perfecto, tenemos paquetes desde $8,000 MXN. ¿Te envío el catálogo?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
('ext_wa_005', 1, 'WHATSAPP', 'Sí por favor!', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
('ext_wa_006', 1, 'WHATSAPP', 'También quiero saber si hacen apps móviles', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 7 MINUTE)),
('ext_wa_007', 1, 'WHATSAPP', 'Cuánto tiempo tardan en entregar?', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 5 MINUTE));
 
-- Conversación 2 — Sofía / Instagram (1 no leído)
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_ig_001', 2, 'INSTAGRAM', 'Hola! Vi su story y me interesa el servicio de redes sociales', 'READ', 1, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
('ext_ig_002', 2, 'INSTAGRAM', 'Hola Sofía! Con gusto te informamos. ¿Manejas alguna red actualmente?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 35 MINUTE)),
('ext_ig_003', 2, 'INSTAGRAM', 'Solo Instagram y TikTok, pero quiero crecer más rápido', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 20 MINUTE));
 
-- Conversación 3 — Andrés / Facebook (leída)
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_fb_001', 3, 'FACEBOOK', 'Buenos días, ¿manejan campañas de Google Ads?', 'READ', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_fb_002', 3, 'FACEBOOK', 'Sí Andrés, manejamos Google Ads, Meta Ads y SEO. ¿Cuál es tu presupuesto mensual?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('ext_fb_003', 3, 'FACEBOOK', 'Tengo alrededor de $5,000 MXN al mes', 'READ', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('ext_fb_004', 3, 'FACEBOOK', 'Es un buen punto de partida. Te agendo una llamada con nuestro especialista?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 118 MINUTE));
 
-- Conversación 4 — Valeria / WhatsApp (leída)
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_wa_010', 4, 'WHATSAPP', 'Necesito un logo para mi negocio', 'READ', 1, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('ext_wa_011', 4, 'WHATSAPP', 'Hola Valeria! Claro, manejamos diseño de identidad. ¿De qué giro es tu negocio?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_wa_012', 4, 'WHATSAPP', 'Vendo pasteles y postres artesanales', 'READ', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('ext_wa_013', 4, 'WHATSAPP', 'Qué bonito! Te mando ejemplos de nuestro portafolio de branding food', 'READ', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR));
 
-- Conversación 5 — Diego / Instagram (leída, hace 1 día)
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_ig_010', 5, 'INSTAGRAM', 'Cuánto cuesta manejar mi Instagram?', 'READ', 1, DATE_SUB(NOW(), INTERVAL 25 HOUR)),
('ext_ig_011', 5, 'INSTAGRAM', 'Hola Diego! Depende del plan. Los precios van desde $3,500/mes. ¿Quieres un presupuesto?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
('ext_ig_012', 5, 'INSTAGRAM', 'Sí, mándame info por favor', 'READ', 1, DATE_SUB(NOW(), INTERVAL 24 HOUR));
 
-- Conversación 6 — María / WhatsApp — Tenant 2 (2 no leídos)
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_wa_020', 6, 'WHATSAPP', 'Hola! Quiero apartar mesa para 8 personas el sábado', 'READ', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('ext_wa_021', 6, 'WHATSAPP', 'Claro María! ¿A qué hora sería?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
('ext_wa_022', 6, 'WHATSAPP', 'A las 2pm', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 12 MINUTE)),
('ext_wa_023', 6, 'WHATSAPP', 'Tienen estacionamiento?', 'PROCESSED', 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE));
 
-- Conversación 7 — Roberto / Facebook — Tenant 2
INSERT INTO messages (external_message_id, conversation_id, channel, contenido, status, es_entrante, sent_at) VALUES
('ext_fb_020', 7, 'FACEBOOK', 'Tienen opción sin gluten en el menú?', 'READ', 1, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
('ext_fb_021', 7, 'FACEBOOK', 'Hola Roberto! Sí contamos con opciones sin gluten. ¿Cuáles te interesan?', 'READ', 0, DATE_SUB(NOW(), INTERVAL 45 MINUTE)),
('ext_fb_022', 7, 'FACEBOOK', 'Qué bueno! Me interesan los tacos y las quesadillas', 'READ', 1, DATE_SUB(NOW(), INTERVAL 44 MINUTE));
 
SELECT '✅ Demo data insertada correctamente' AS resultado;
 
-- ── Verificación rápida ───────────────────────────────────────
SELECT
  t.nombre AS tenant,
  c.channel,
  l.nombre AS lead,
  c.unread_count AS no_leidos,
  c.last_message_at,
  (SELECT COUNT(*) FROM messages m WHERE m.conversation_id = c.id) AS total_mensajes
FROM conversations c
JOIN leads l        ON l.id = c.lead_id
JOIN tenants t      ON t.id = c.tenant_id
ORDER BY t.id, c.last_message_at DESC;