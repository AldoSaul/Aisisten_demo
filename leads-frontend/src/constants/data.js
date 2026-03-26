export const TENANTS = [
  { id: 1, nombre: 'Agencia Digital Nexus' },
  { id: 2, nombre: 'Restaurante Don Tacos' },
  { id: 3, nombre: 'Clínica Salud Total' },
];

export const ALL_CONVS = [
  { id: 1, tenantId: 1, leadNombre: 'Carlos Mendoza',   channel: 'WHATSAPP',  preview: 'Cuánto tiempo tardan en entregar?',                     time: '5 min',  unread: 3, initials: 'CM' },
  { id: 2, tenantId: 1, leadNombre: 'Sofía Ramírez',    channel: 'INSTAGRAM', preview: 'Solo Instagram y TikTok, pero quiero crecer más rápido', time: '20 min', unread: 1, initials: 'SR' },
  { id: 3, tenantId: 1, leadNombre: 'Andrés Torres',    channel: 'FACEBOOK',  preview: 'Te agendo una llamada con nuestro especialista?',         time: '2 h',    unread: 0, initials: 'AT' },
  { id: 4, tenantId: 1, leadNombre: 'Valeria Cruz',     channel: 'WHATSAPP',  preview: 'Qué bonito! Te mando ejemplos del portafolio',            time: '3 h',    unread: 0, initials: 'VC' },
  { id: 5, tenantId: 1, leadNombre: 'Diego Hernández',  channel: 'INSTAGRAM', preview: 'Sí, mándame info por favor',                             time: '1 día',  unread: 0, initials: 'DH' },
  { id: 6, tenantId: 2, leadNombre: 'María López',      channel: 'WHATSAPP',  preview: 'Tienen estacionamiento?',                                time: '10 min', unread: 2, initials: 'ML' },
  { id: 7, tenantId: 2, leadNombre: 'Roberto Gómez',    channel: 'FACEBOOK',  preview: 'Qué bueno! Me interesan los tacos y las quesadillas',     time: '45 min', unread: 0, initials: 'RG' },
  { id: 8, tenantId: 2, leadNombre: 'Fernanda Ruiz',    channel: 'INSTAGRAM', preview: 'Gracias por la información',                             time: '2 días', unread: 0, initials: 'FR' },
];

export const ALL_MSGS = {
  1: [
    { id: 1, txt: 'Hola! Vi su publicación sobre diseño web',                                     out: false, time: '10:02' },
    { id: 2, txt: 'Buenos días Carlos! ¿Qué tipo de proyecto tienes en mente?',                   out: true,  time: '10:06' },
    { id: 3, txt: 'Necesito una tienda en línea para vender ropa',                                out: false, time: '10:34' },
    { id: 4, txt: 'Perfecto, tenemos paquetes desde $8,000 MXN. ¿Te envío el catálogo?',          out: true,  time: '10:39' },
    { id: 5, txt: 'Sí por favor!',                                                                out: false, time: '10:54' },
    { id: 6, txt: 'También quiero saber si hacen apps móviles',                                   out: false, time: '10:57' },
    { id: 7, txt: 'Cuánto tiempo tardan en entregar?',                                            out: false, time: '10:59' },
  ],
  2: [
    { id: 1, txt: 'Hola! Vi su story y me interesa el servicio de redes sociales',                out: false, time: '11:20' },
    { id: 2, txt: 'Hola Sofía! Con gusto te informamos. ¿Manejas alguna red actualmente?',        out: true,  time: '11:25' },
    { id: 3, txt: 'Solo Instagram y TikTok, pero quiero crecer más rápido',                       out: false, time: '11:40' },
  ],
  3: [
    { id: 1, txt: 'Buenos días, ¿manejan campañas de Google Ads?',                                out: false, time: '09:00' },
    { id: 2, txt: 'Sí Andrés, manejamos Google Ads, Meta Ads y SEO. ¿Cuál es tu presupuesto mensual?', out: true, time: '09:10' },
    { id: 3, txt: 'Tengo alrededor de $5,000 MXN al mes',                                         out: false, time: '09:12' },
    { id: 4, txt: 'Es un buen punto de partida. ¿Te agendo una llamada con nuestro especialista?', out: true,  time: '09:22' },
  ],
  4: [
    { id: 1, txt: 'Necesito un logo para mi negocio',                                             out: false, time: '08:00' },
    { id: 2, txt: 'Hola Valeria! Claro, manejamos diseño de identidad. ¿De qué giro es tu negocio?', out: true, time: '08:15' },
    { id: 3, txt: 'Vendo pasteles y postres artesanales',                                          out: false, time: '08:20' },
    { id: 4, txt: 'Qué bonito! Te mando ejemplos de nuestro portafolio de branding food',          out: true,  time: '08:30' },
  ],
  5: [
    { id: 1, txt: 'Cuánto cuesta manejar mi Instagram?',                                          out: false, time: 'ayer' },
    { id: 2, txt: 'Hola Diego! Los precios van desde $3,500/mes. ¿Quieres un presupuesto?',        out: true,  time: 'ayer' },
    { id: 3, txt: 'Sí, mándame info por favor',                                                   out: false, time: 'ayer' },
  ],
  6: [
    { id: 1, txt: 'Hola! Quiero apartar mesa para 8 personas el sábado',                          out: false, time: '11:50' },
    { id: 2, txt: 'Claro María! ¿A qué hora sería?',                                              out: true,  time: '11:55' },
    { id: 3, txt: 'A las 2pm',                                                                    out: false, time: '12:08' },
    { id: 4, txt: 'Tienen estacionamiento?',                                                      out: false, time: '12:10' },
  ],
  7: [
    { id: 1, txt: 'Tienen opción sin gluten en el menú?',                                         out: false, time: '12:30' },
    { id: 2, txt: 'Hola Roberto! Sí contamos con opciones sin gluten. ¿Cuáles te interesan?',     out: true,  time: '12:35' },
    { id: 3, txt: 'Qué bueno! Me interesan los tacos y las quesadillas',                          out: false, time: '12:36' },
  ],
  8: [
    { id: 1, txt: 'Hola! ¿Tienen servicio a domicilio?',                                          out: false, time: 'ayer' },
    { id: 2, txt: 'Hola Fernanda! Sí, hacemos entregas en un radio de 5 km.',                     out: true,  time: 'ayer' },
    { id: 3, txt: 'Gracias por la información',                                                   out: false, time: 'ayer' },
  ],
};

export const FAKE_INCOMING = [
  { convId: 1, txt: 'Cuándo me mandan el catálogo?' },
  { convId: 2, txt: 'Tienen paquetes para pequeños negocios?' },
  { convId: 6, txt: 'También hay mesa disponible el domingo?' },
];
