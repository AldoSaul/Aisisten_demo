package com.leads.model;

public enum MessageStatus {
    RECEIVED,   // llegó del webhook
    PROCESSED,  // procesado y guardado
    READ,       // marcado como leído desde la app
    REPLIED     // se respondió
}
