// ── Static display helpers (no API) ───────────────────────────────────────────
export const TENANTS = [
  { id: 1, nombre: "Agencia Digital Nexus" },
  { id: 2, nombre: "Restaurante Don Tacos" },
  { id: 3, nombre: "Clínica Salud Total" },
];

export const CH_COLOR  = { WHATSAPP: "#25D366", INSTAGRAM: "#E1306C", FACEBOOK: "#1877F2" };
export const CH_LABEL  = { WHATSAPP: "WhatsApp", INSTAGRAM: "Instagram", FACEBOOK: "Facebook" };
export const CH_LIGHT  = { WHATSAPP: "#e6faf0", INSTAGRAM: "#fce8f0", FACEBOOK: "#e7f0fd" };
export const CH_DARK   = { WHATSAPP: "#064e2e", INSTAGRAM: "#5c0e26", FACEBOOK: "#0c2d5a" };

// ── DTO normalizers (map backend shape → UI shape) ────────────────────────────
export function fmtTime(iso: string): string {
  const d = new Date(iso);
  const diffMin = Math.floor((Date.now() - d.getTime()) / 60000);
  if (diffMin < 1)  return "ahora";
  if (diffMin < 60) return `${diffMin} min`;
  const diffH = Math.floor(diffMin / 60);
  if (diffH < 24)   return `${diffH} h`;
  const diffD = Math.floor(diffH / 24);
  if (diffD === 1)  return "ayer";
  return `${diffD} días`;
}

function getInitials(nombre: string): string {
  return nombre.split(" ").slice(0, 2).map(w => w[0]).join("").toUpperCase();
}

export function normalizeConv(c: any, tenantId: number) {
  return {
    id:         c.id,
    tenantId,
    leadNombre: c.leadNombre,
    channel:    c.channel,
    preview:    c.lastMessage   ?? "",
    time:       c.lastMessageAt ? fmtTime(c.lastMessageAt) : "",
    unread:     c.unreadCount   ?? 0,
    initials:   getInitials(c.leadNombre ?? ""),
  };
}

export function normalizeMsg(m: any) {
  return {
    id:   m.id,
    txt:  m.contenido ?? "",
    out:  !m.esEntrante,
    time: m.sentAt
            ? new Date(m.sentAt).toLocaleTimeString("es-MX", { hour: "2-digit", minute: "2-digit" })
            : "",
  };
}
