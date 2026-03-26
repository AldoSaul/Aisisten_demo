import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  FlatList,
  TouchableOpacity,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useApp }   from '../context/AppContext';
import { TENANTS }  from '../constants/data';
import { COLORS, CH_COLOR, CH_LABEL, CH_LIGHT } from '../constants/theme';
import Avatar        from '../components/Avatar';

export default function InboxScreen({ navigation }) {
  const {
    activeTenant,  selectTenant,
    channelFilter, setChannelFilter,
    tenantConvs,   totalUnread,
    showTyping,
    selectConv,
  } = useApp();

  const handleConvPress = (conv) => {
    selectConv(conv.id);
    navigation.navigate('Chat', { convId: conv.id });
  };

  return (
    <SafeAreaView style={styles.container}>

      {/* ── Brand ── */}
      <View style={styles.brand}>
        <Text style={styles.brandText}>
          Leads<Text style={styles.brandAccent}>Hub</Text>
        </Text>
      </View>

      {/* ── Tenant tabs ── */}
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        style={styles.tenantScroll}
        contentContainerStyle={styles.tenantScrollContent}
      >
        {TENANTS.map(t => (
          <TouchableOpacity
            key={t.id}
            style={[styles.tenantTab, activeTenant === t.id && styles.tenantTabActive]}
            onPress={() => selectTenant(t.id)}
            activeOpacity={0.7}
          >
            <View style={[styles.tenantDot, activeTenant === t.id && styles.tenantDotActive]} />
            <Text
              style={[styles.tenantName, activeTenant === t.id && styles.tenantNameActive]}
              numberOfLines={1}
            >
              {t.nombre}
            </Text>
          </TouchableOpacity>
        ))}
      </ScrollView>

      {/* ── Stats row ── */}
      <View style={styles.statsRow}>
        <View style={styles.statItem}>
          <Text style={styles.statN}>{tenantConvs.length}</Text>
          <Text style={styles.statL}>Chats</Text>
        </View>
        <View style={[styles.statItem, styles.statBorder]}>
          <Text style={[styles.statN, totalUnread > 0 && { color: COLORS.accent }]}>
            {totalUnread}
          </Text>
          <Text style={styles.statL}>Sin leer</Text>
        </View>
        <View style={[styles.statItem, styles.statBorder, { flex: 1 }]}>
          <Text style={styles.statConnected}>● Conectado</Text>
          <Text style={styles.statL}>WebSocket activo</Text>
        </View>
      </View>

      {/* ── White inbox section ── */}
      <View style={styles.inboxSection}>

        {/* Header */}
        <View style={styles.inboxHeader}>
          <View style={styles.inboxHeaderRow}>
            <Text style={styles.inboxTitle}>Bandeja</Text>
            {totalUnread > 0 && (
              <View style={styles.unreadTotal}>
                <Text style={styles.unreadTotalText}>{totalUnread} nuevos</Text>
              </View>
            )}
          </View>
        </View>

        {/* Filter pills */}
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          style={styles.filters}
          contentContainerStyle={styles.filtersContent}
        >
          {[null, 'WHATSAPP', 'INSTAGRAM', 'FACEBOOK'].map(ch => {
            const isActive = channelFilter === ch;
            return (
              <TouchableOpacity
                key={ch || 'all'}
                style={[
                  styles.pill,
                  isActive && {
                    backgroundColor: ch ? CH_COLOR[ch] : COLORS.text,
                    borderColor:     'transparent',
                  },
                ]}
                onPress={() => setChannelFilter(ch)}
                activeOpacity={0.7}
              >
                <Text style={[styles.pillText, isActive && { color: '#fff' }]}>
                  {ch ? CH_LABEL[ch] : 'Todos'}
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>

        {/* Conversation list */}
        <FlatList
          data={tenantConvs}
          keyExtractor={item => String(item.id)}
          style={styles.convList}
          contentContainerStyle={styles.convListContent}
          ItemSeparatorComponent={() => <View style={styles.separator} />}
          ListEmptyComponent={
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>Sin conversaciones</Text>
            </View>
          }
          renderItem={({ item: conv }) => (
            <TouchableOpacity
              style={styles.convItem}
              onPress={() => handleConvPress(conv)}
              activeOpacity={0.75}
            >
              <Avatar initials={conv.initials} channel={conv.channel} size={42} />
              <View style={styles.convBody}>
                <View style={styles.convRow}>
                  <Text
                    style={[styles.convName, conv.unread > 0 && { fontWeight: '600' }]}
                    numberOfLines={1}
                  >
                    {conv.leadNombre}
                  </Text>
                  <Text style={styles.convTime}>{conv.time}</Text>
                </View>

                <View style={styles.convMeta}>
                  <View
                    style={[
                      styles.chBadge,
                      {
                        backgroundColor: CH_LIGHT[conv.channel],
                        borderColor:     CH_COLOR[conv.channel] + '55',
                      },
                    ]}
                  >
                    <Text style={[styles.chBadgeText, { color: CH_COLOR[conv.channel] }]}>
                      {CH_LABEL[conv.channel]}
                    </Text>
                  </View>
                  {conv.unread > 0 && (
                    <View style={styles.unreadBadge}>
                      <Text style={styles.unreadBadgeText}>{conv.unread}</Text>
                    </View>
                  )}
                </View>

                <Text
                  style={[
                    styles.convPreview,
                    conv.unread > 0 && { color: COLORS.text, fontWeight: '500' },
                  ]}
                  numberOfLines={1}
                >
                  {showTyping === conv.id ? 'Escribiendo...' : conv.preview}
                </Text>
              </View>
            </TouchableOpacity>
          )}
        />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex:            1,
    backgroundColor: COLORS.sidebar,
  },

  // Brand
  brand: {
    paddingHorizontal: 20,
    paddingTop:        16,
    paddingBottom:     14,
    borderBottomWidth: 1,
    borderBottomColor: '#2a2520',
  },
  brandText: {
    fontSize:      20,
    fontWeight:    '800',
    color:         '#F7F5F0',
    letterSpacing: -0.5,
  },
  brandAccent: {
    color: COLORS.accent,
  },

  // Tenant tabs
  tenantScroll: {
    maxHeight: 52,
  },
  tenantScrollContent: {
    paddingHorizontal: 8,
    paddingVertical:   6,
    gap:               4,
    flexDirection:     'row',
    alignItems:        'center',
  },
  tenantTab: {
    flexDirection:  'row',
    alignItems:     'center',
    gap:            8,
    paddingVertical:   7,
    paddingHorizontal: 12,
    borderRadius:   8,
  },
  tenantTabActive: {
    backgroundColor: '#2e2820',
  },
  tenantDot: {
    width:           8,
    height:          8,
    borderRadius:    4,
    backgroundColor: COLORS.accent,
  },
  tenantDotActive: {
    backgroundColor: COLORS.green,
  },
  tenantName: {
    fontSize:   13,
    color:      '#c8c0b4',
    fontWeight: '400',
  },
  tenantNameActive: {
    color:      '#f0ece4',
    fontWeight: '500',
  },

  // Stats
  statsRow: {
    flexDirection:    'row',
    paddingHorizontal: 16,
    paddingVertical:   10,
    borderTopWidth:    1,
    borderTopColor:    '#2a2520',
    gap:               0,
  },
  statItem: {
    minWidth:    60,
    marginRight: 16,
  },
  statBorder: {
    paddingLeft:     16,
    borderLeftWidth: 1,
    borderLeftColor: '#2a2520',
    marginRight:     16,
  },
  statN: {
    fontSize:   22,
    fontWeight: '700',
    color:      '#f0ece4',
  },
  statL: {
    fontSize:  10,
    color:     '#5a5248',
    marginTop: 1,
  },
  statConnected: {
    fontSize:   12,
    color:      COLORS.green,
    fontWeight: '500',
  },

  // Inbox section (white card)
  inboxSection: {
    flex:                1,
    backgroundColor:     COLORS.surface,
    borderTopLeftRadius:  16,
    borderTopRightRadius: 16,
    overflow:            'hidden',
  },
  inboxHeader: {
    paddingHorizontal: 18,
    paddingTop:        18,
    paddingBottom:     12,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  inboxHeaderRow: {
    flexDirection:   'row',
    justifyContent:  'space-between',
    alignItems:      'center',
  },
  inboxTitle: {
    fontSize:      20,
    fontWeight:    '700',
    color:         COLORS.text,
    letterSpacing: -0.3,
  },
  unreadTotal: {
    backgroundColor: COLORS.accent,
    borderRadius:    20,
    paddingHorizontal: 9,
    paddingVertical:   3,
  },
  unreadTotalText: {
    color:      '#fff',
    fontSize:   11,
    fontWeight: '600',
  },

  // Filter pills
  filters: {
    maxHeight:         46,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  filtersContent: {
    paddingHorizontal: 18,
    paddingVertical:   8,
    gap:               6,
    flexDirection:     'row',
    alignItems:        'center',
  },
  pill: {
    paddingVertical:   5,
    paddingHorizontal: 14,
    borderRadius:      20,
    backgroundColor:   COLORS.bg,
    borderWidth:       1,
    borderColor:       'transparent',
  },
  pillText: {
    fontSize:   12,
    fontWeight: '500',
    color:      COLORS.sub,
  },

  // Conversation list
  convList: {
    flex:            1,
    backgroundColor: COLORS.bg,
  },
  convListContent: {
    flexGrow:        1,
  },
  separator: {
    height:          1,
    backgroundColor: COLORS.border,
  },
  emptyState: {
    padding:    24,
    alignItems: 'center',
  },
  emptyText: {
    color:    COLORS.sub,
    fontSize: 13,
  },

  // Conversation item
  convItem: {
    flexDirection:   'row',
    alignItems:      'flex-start',
    gap:             12,
    paddingHorizontal: 18,
    paddingVertical:   13,
    backgroundColor: COLORS.surface,
  },
  convBody: {
    flex:     1,
    minWidth: 0,
  },
  convRow: {
    flexDirection:  'row',
    justifyContent: 'space-between',
    alignItems:     'center',
  },
  convName: {
    flex:       1,
    fontSize:   14,
    fontWeight: '400',
    color:      COLORS.text,
    marginRight: 8,
  },
  convTime: {
    fontSize: 11,
    color:    COLORS.sub,
  },
  convMeta: {
    flexDirection: 'row',
    alignItems:    'center',
    gap:           6,
    marginTop:     4,
  },
  chBadge: {
    borderRadius:      10,
    borderWidth:       1,
    paddingHorizontal: 7,
    paddingVertical:   2,
  },
  chBadgeText: {
    fontSize:   10,
    fontWeight: '600',
  },
  unreadBadge: {
    backgroundColor:   COLORS.accent,
    borderRadius:      10,
    paddingHorizontal: 6,
    paddingVertical:   2,
  },
  unreadBadgeText: {
    color:      '#fff',
    fontSize:   10,
    fontWeight: '700',
  },
  convPreview: {
    fontSize:  12,
    color:     COLORS.sub,
    marginTop: 4,
  },
});
