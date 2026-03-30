export const TENANTS = [
  { id: 1, nombre: 'Agencia Digital Nexus' },
  { id: 2, nombre: 'Restaurante Don Tacos' },
  { id: 3, nombre: 'Clínica Salud Total' },
];

// ── DTO normalizers (map backend shape → UI shape) ────────────────────────────
export function fmtTime(iso) {
  const d = new Date(iso);
  const diffMin = Math.floor((Date.now() - d.getTime()) / 60000);
  if (diffMin < 1)  return 'ahora';
  if (diffMin < 60) return `${diffMin} min`;
  const diffH = Math.floor(diffMin / 60);
  if (diffH < 24)   return `${diffH} h`;
  const diffD = Math.floor(diffH / 24);
  if (diffD === 1)  return 'ayer';
  return `${diffD} días`;
}

function getInitials(nombre) {
  return (nombre ?? '')
    .split(' ')
    .slice(0, 2)
    .map(w => w[0])
    .join('')
    .toUpperCase();
}

export function normalizeConv(c, tenantId) {
  return {
    id:         c.id,
    tenantId,
    leadNombre: c.leadNombre,
    channel:    c.channel,
    preview:    c.lastMessage   ?? '',
    time:       c.lastMessageAt ? fmtTime(c.lastMessageAt) : '',
    unread:     c.unreadCount   ?? 0,
    initials:   getInitials(c.leadNombre),
  };
}

export function normalizeMsg(m) {
  return {
    id:   m.id,
    txt:  m.contenido ?? '',
    out:  !m.esEntrante,
    time: m.sentAt
            ? new Date(m.sentAt).toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' })
            : '',
  };
}

// ── REMOVED: ALL_CONVS, ALL_MSGS, FAKE_INCOMING (replaced by real API) ────────
// Legacy exports kept as empty values so any missed import doesn't crash at runtime
export const ALL_CONVS    = [];
export const ALL_MSGS     = {};
export const FAKE_INCOMING = [];
