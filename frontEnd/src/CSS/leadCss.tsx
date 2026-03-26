


export const css = `
  @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;500;600;700;800&family=DM+Sans:ital,opsz,wght@0,9..40,300;0,9..40,400;0,9..40,500;1,9..40,300&display=swap');

  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

  :root {
    --bg:       #F7F5F0;
    --surface:  #FFFFFF;
    --sidebar:  #16120E;
    --text:     #1C1814;
    --sub:      #8A8075;
    --border:   #E8E4DE;
    --accent:   #C84B2F;
    --radius:   12px;
    --font-head: 'Syne', sans-serif;
    --font-body: 'DM Sans', sans-serif;
  }

  body { font-family: var(--font-body); background: var(--bg); color: var(--text); }

  .app {
    display: grid;
    grid-template-columns: 220px 340px 1fr;
    height: 100vh;
    overflow: hidden;
  }

  /* ── Sidebar ── */
  .sidebar {
    background: var(--sidebar);
    display: flex;
    flex-direction: column;
    padding: 0;
    overflow: hidden;
  }
  .sidebar-brand {
    padding: 22px 20px 18px;
    border-bottom: 1px solid #2a2520;
  }
  .sidebar-brand h1 {
    font-family: var(--font-head);
    font-size: 18px;
    font-weight: 800;
    color: #F7F5F0;
    letter-spacing: -0.5px;
  }
  .sidebar-brand span { color: var(--accent); }
  .sidebar-section {
    padding: 16px 12px 8px;
    font-size: 10px;
    font-weight: 600;
    letter-spacing: 1.5px;
    color: #4a4540;
    text-transform: uppercase;
  }
  .tenant-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 9px 12px;
    margin: 0 8px;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.15s;
  }
  .tenant-item:hover { background: #221e18; }
  .tenant-item.active { background: #2e2820; }
  .tenant-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background: var(--accent); flex-shrink: 0;
  }
  .tenant-item.active .tenant-dot { background: #7DC97A; }
  .tenant-name {
    font-size: 13px; color: #c8c0b4; font-weight: 400;
    white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
  }
  .tenant-item.active .tenant-name { color: #f0ece4; font-weight: 500; }

  .sidebar-stats {
    margin-top: auto;
    padding: 16px;
    border-top: 1px solid #2a2520;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 8px;
  }
  .stat-card {
    background: #221e18;
    border-radius: 8px;
    padding: 10px 12px;
  }
  .stat-card .n { font-family: var(--font-head); font-size: 22px; font-weight: 700; color: #f0ece4; }
  .stat-card .l { font-size: 10px; color: #5a5248; margin-top: 1px; }

  /* ── Inbox panel ── */
  .inbox {
    background: var(--surface);
    border-right: 1px solid var(--border);
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }
  .inbox-header {
    padding: 20px 18px 14px;
    border-bottom: 1px solid var(--border);
  }
  .inbox-header h2 {
    font-family: var(--font-head);
    font-size: 20px;
    font-weight: 700;
    color: var(--text);
    letter-spacing: -0.3px;
  }
  .inbox-header-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .unread-total {
    background: var(--accent);
    color: #fff;
    font-size: 11px;
    font-weight: 600;
    padding: 3px 9px;
    border-radius: 20px;
  }

  .filters {
    display: flex;
    gap: 6px;
    padding: 10px 18px;
    border-bottom: 1px solid var(--border);
    overflow-x: auto;
    scrollbar-width: none;
  }
  .filters::-webkit-scrollbar { display: none; }
  .filter-pill {
    padding: 5px 14px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
    cursor: pointer;
    border: 1px solid transparent;
    transition: all 0.15s;
    white-space: nowrap;
    background: var(--bg);
    color: var(--sub);
    font-family: var(--font-body);
  }
  .filter-pill:hover { border-color: var(--border); color: var(--text); }
  .filter-pill.active { color: #fff; border-color: transparent; }

  .conv-list { flex: 1; overflow-y: auto; scrollbar-width: thin; }
  .conv-list::-webkit-scrollbar { width: 4px; }
  .conv-list::-webkit-scrollbar-track { background: transparent; }
  .conv-list::-webkit-scrollbar-thumb { background: var(--border); border-radius: 2px; }

  .conv-item {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 13px 18px;
    cursor: pointer;
    border-bottom: 1px solid var(--border);
    transition: background 0.12s;
    position: relative;
  }
  .conv-item:hover { background: var(--bg); }
  .conv-item.active { background: #FDF4F2; }
  .conv-item.unread { background: #FFFBFA; }
  .conv-item.active::before {
    content: '';
    position: absolute;
    left: 0; top: 0; bottom: 0;
    width: 3px;
    background: var(--accent);
    border-radius: 0 2px 2px 0;
  }

  .avatar {
    width: 40px; height: 40px; border-radius: 50%;
    display: flex; align-items: center; justify-content: center;
    font-family: var(--font-head);
    font-size: 14px; font-weight: 700; color: #fff;
    flex-shrink: 0;
  }
  .conv-body { flex: 1; min-width: 0; }
  .conv-row { display: flex; justify-content: space-between; align-items: center; }
  .conv-name { font-size: 14px; font-weight: 400; color: var(--text); }
  .conv-name.bold { font-weight: 600; }
  .conv-time { font-size: 11px; color: var(--sub); }
  .ch-badge {
    display: inline-block;
    font-size: 10px; font-weight: 600;
    padding: 2px 7px; border-radius: 10px;
    margin-top: 4px;
    border: 1px solid;
  }
  .unread-badge {
    background: var(--accent); color: #fff;
    font-size: 10px; font-weight: 700;
    padding: 2px 6px; border-radius: 10px;
    margin-left: auto;
  }
  .conv-preview {
    font-size: 12px; color: var(--sub);
    margin-top: 4px;
    white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
  }
  .conv-preview.bold { color: var(--text); font-weight: 500; }

  /* ── Chat panel ── */
  .chat {
    display: flex;
    flex-direction: column;
    background: var(--bg);
    overflow: hidden;
  }
  .chat-empty {
    flex: 1; display: flex; flex-direction: column;
    align-items: center; justify-content: center; gap: 12px;
    color: var(--sub);
  }
  .chat-empty-icon {
    width: 56px; height: 56px; border-radius: 50%;
    background: var(--border);
    display: flex; align-items: center; justify-content: center;
    font-size: 22px;
  }
  .chat-empty p { font-size: 14px; }

  .chat-header {
    background: var(--surface);
    padding: 14px 20px;
    border-bottom: 1px solid var(--border);
    display: flex;
    align-items: center;
    gap: 12px;
  }
  .chat-header-info { flex: 1; }
  .chat-header-name {
    font-family: var(--font-head);
    font-size: 16px; font-weight: 700; color: var(--text);
  }
  .chat-header-sub { font-size: 12px; color: var(--sub); margin-top: 2px; }

  .live-dot {
    width: 8px; height: 8px; border-radius: 50%;
    background: #7DC97A;
    animation: pulse 2s ease-in-out infinite;
  }
  @keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }

  .msgs {
    flex: 1; overflow-y: auto;
    padding: 16px 20px;
    display: flex; flex-direction: column; gap: 6px;
    scrollbar-width: thin;
  }
  .msgs::-webkit-scrollbar { width: 4px; }
  .msgs::-webkit-scrollbar-track { background: transparent; }
  .msgs::-webkit-scrollbar-thumb { background: var(--border); border-radius: 2px; }

  .msg-row { display: flex; }
  .msg-row.out { justify-content: flex-end; }

  .bubble {
    max-width: 68%;
    padding: 9px 13px 7px;
    border-radius: 14px;
    font-size: 14px;
    line-height: 1.5;
    animation: pop 0.2s ease-out;
  }
  @keyframes pop {
    from { transform: scale(0.92); opacity: 0; }
    to   { transform: scale(1);    opacity: 1; }
  }
  .bubble-in {
    background: var(--surface);
    color: var(--text);
    border-bottom-left-radius: 3px;
    border: 1px solid var(--border);
  }
  .bubble-out {
    background: var(--text);
    color: #F7F5F0;
    border-bottom-right-radius: 3px;
  }
  .bubble-time {
    font-size: 10px; opacity: 0.5;
    margin-top: 4px; text-align: right;
  }

  .new-msg-flash {
    animation: flash 0.5s ease-out;
  }
  @keyframes flash {
    0% { background: #fef3e2; }
    100% { background: var(--surface); }
  }

  .input-area {
    background: var(--surface);
    padding: 14px 20px;
    border-top: 1px solid var(--border);
    display: flex;
    gap: 10px;
    align-items: center;
  }
  .msg-input {
    flex: 1;
    background: var(--bg);
    border: 1px solid var(--border);
    border-radius: 22px;
    padding: 10px 16px;
    font-family: var(--font-body);
    font-size: 14px;
    color: var(--text);
    outline: none;
    transition: border-color 0.15s;
    resize: none;
    height: 42px;
    line-height: 1.3;
  }
  .msg-input:focus { border-color: var(--accent); }
  .send-btn {
    width: 42px; height: 42px; border-radius: 50%;
    background: var(--accent);
    border: none; cursor: pointer;
    display: flex; align-items: center; justify-content: center;
    transition: transform 0.12s, background 0.12s;
    flex-shrink: 0;
  }
  .send-btn:hover { transform: scale(1.08); background: #b03a20; }
  .send-btn:active { transform: scale(0.95); }
  .send-btn svg { width: 18px; height: 18px; fill: #fff; }

  /* ── Typing indicator ── */
  .typing { display: flex; align-items: center; gap: 4px; padding: 8px 12px; }
  .typing span {
    width: 6px; height: 6px; border-radius: 50%; background: var(--sub);
    animation: bounce 1.2s ease-in-out infinite;
  }
  .typing span:nth-child(2) { animation-delay: 0.2s; }
  .typing span:nth-child(3) { animation-delay: 0.4s; }
  @keyframes bounce { 0%,60%,100%{transform:translateY(0)} 30%{transform:translateY(-5px)} }

  .typing-bubble {
    background: var(--surface); border: 1px solid var(--border);
    border-radius: 14px; border-bottom-left-radius: 3px;
    display: inline-flex; padding: 8px 12px;
    animation: pop 0.2s ease-out;
  }
`;