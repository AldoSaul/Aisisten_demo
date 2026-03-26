import React, { useState, useRef, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TextInput,
  TouchableOpacity,
  KeyboardAvoidingView,
  Platform,
  Animated,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons }       from '@expo/vector-icons';
import { useApp }         from '../context/AppContext';
import { COLORS, CH_COLOR, CH_LABEL } from '../constants/theme';
import Avatar             from '../components/Avatar';
import TypingIndicator    from '../components/TypingIndicator';

// ── Live dot (pulsing green indicator) ───────────────────────────────────────
function LiveDot() {
  const opacity = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    const anim = Animated.loop(
      Animated.sequence([
        Animated.timing(opacity, { toValue: 0.3, duration: 1000, useNativeDriver: true }),
        Animated.timing(opacity, { toValue: 1.0, duration: 1000, useNativeDriver: true }),
      ])
    );
    anim.start();
    return () => anim.stop();
  }, [opacity]);

  return <Animated.View style={[styles.liveDot, { opacity }]} />;
}

// ── Chat screen ───────────────────────────────────────────────────────────────
export default function ChatScreen({ route, navigation }) {
  const { convId } = route.params;
  const { conversations, messages, showTyping, sendMessage } = useApp();

  const conv = conversations.find(c => c.id === convId);
  const msgs = messages[convId] || [];

  const [input,    setInput]    = useState('');
  const flatListRef = useRef(null);

  // Scroll to bottom whenever messages or typing state changes
  useEffect(() => {
    const timer = setTimeout(() => {
      flatListRef.current?.scrollToEnd({ animated: true });
    }, 80);
    return () => clearTimeout(timer);
  }, [msgs.length, showTyping]);

  const handleSend = () => {
    const text = input.trim();
    if (!text || !conv) return;
    sendMessage(convId, text);
    setInput('');
  };

  if (!conv) return null;

  return (
    <SafeAreaView style={styles.container}>

      {/* ── Header ── */}
      <View style={[styles.header, { borderBottomColor: CH_COLOR[conv.channel] }]}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          style={styles.backBtn}
          hitSlop={{ top: 10, bottom: 10, left: 10, right: 10 }}
        >
          <Ionicons name="chevron-back" size={26} color={COLORS.text} />
        </TouchableOpacity>

        <Avatar initials={conv.initials} channel={conv.channel} size={36} />

        <View style={styles.headerInfo}>
          <Text style={styles.headerName} numberOfLines={1}>{conv.leadNombre}</Text>
          <Text style={styles.headerSub}>
            vía {CH_LABEL[conv.channel]} · ID {conv.id}
          </Text>
        </View>

        <LiveDot />
      </View>

      {/* ── Messages + input wrapped in KAV ── */}
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={0}
      >
        <FlatList
          ref={flatListRef}
          data={msgs}
          keyExtractor={item => String(item.id)}
          contentContainerStyle={styles.msgsList}
          onLayout={() => flatListRef.current?.scrollToEnd({ animated: false })}
          onContentSizeChange={() => flatListRef.current?.scrollToEnd({ animated: false })}
          ListFooterComponent={showTyping === convId ? <TypingIndicator /> : null}
          renderItem={({ item: msg }) => (
            <View style={[styles.msgRow, msg.out && styles.msgRowOut]}>
              <View style={[styles.bubble, msg.out ? styles.bubbleOut : styles.bubbleIn]}>
                <Text style={[styles.bubbleTxt, msg.out && styles.bubbleTxtOut]}>
                  {msg.txt}
                </Text>
                <Text style={[styles.bubbleTime, msg.out && styles.bubbleTimeOut]}>
                  {msg.time}
                </Text>
              </View>
            </View>
          )}
        />

        {/* ── Input area ── */}
        <View style={styles.inputArea}>
          <TextInput
            style={styles.input}
            placeholder={`Responder a ${conv.leadNombre}...`}
            placeholderTextColor={COLORS.sub}
            value={input}
            onChangeText={setInput}
            onSubmitEditing={handleSend}
            returnKeyType="send"
            blurOnSubmit={false}
            multiline={false}
          />
          <TouchableOpacity style={styles.sendBtn} onPress={handleSend} activeOpacity={0.8}>
            <Ionicons name="send" size={17} color="#fff" />
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>

    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex:            1,
    backgroundColor: COLORS.bg,
  },

  // Header
  header: {
    flexDirection:     'row',
    alignItems:        'center',
    gap:               10,
    paddingHorizontal: 12,
    paddingVertical:   12,
    backgroundColor:   COLORS.surface,
    borderBottomWidth: 2,
  },
  backBtn: {
    marginRight: 2,
  },
  headerInfo: {
    flex:    1,
    minWidth: 0,
  },
  headerName: {
    fontSize:      16,
    fontWeight:    '700',
    color:         COLORS.text,
    letterSpacing: -0.2,
  },
  headerSub: {
    fontSize:  12,
    color:     COLORS.sub,
    marginTop: 1,
  },
  liveDot: {
    width:           8,
    height:          8,
    borderRadius:    4,
    backgroundColor: COLORS.green,
  },

  // Messages list
  msgsList: {
    paddingHorizontal: 16,
    paddingVertical:   14,
    gap:               6,
  },
  msgRow: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
  },
  msgRowOut: {
    justifyContent: 'flex-end',
  },
  bubble: {
    maxWidth:      '72%',
    paddingHorizontal: 13,
    paddingTop:    9,
    paddingBottom: 7,
    borderRadius:  14,
  },
  bubbleIn: {
    backgroundColor:      COLORS.surface,
    borderBottomLeftRadius: 3,
    borderWidth:          1,
    borderColor:          COLORS.border,
  },
  bubbleOut: {
    backgroundColor:       COLORS.text,
    borderBottomRightRadius: 3,
  },
  bubbleTxt: {
    fontSize:    14,
    lineHeight:  21,
    color:       COLORS.text,
  },
  bubbleTxtOut: {
    color: '#F7F5F0',
  },
  bubbleTime: {
    fontSize:  10,
    color:     COLORS.sub,
    marginTop: 4,
    textAlign: 'right',
    opacity:   0.6,
  },
  bubbleTimeOut: {
    color: 'rgba(247,245,240,0.6)',
  },

  // Input area
  inputArea: {
    flexDirection:     'row',
    alignItems:        'center',
    gap:               10,
    paddingHorizontal: 14,
    paddingVertical:   12,
    backgroundColor:   COLORS.surface,
    borderTopWidth:    1,
    borderTopColor:    COLORS.border,
  },
  input: {
    flex:              1,
    height:            42,
    backgroundColor:   COLORS.bg,
    borderWidth:       1,
    borderColor:       COLORS.border,
    borderRadius:      22,
    paddingHorizontal: 16,
    fontSize:          14,
    color:             COLORS.text,
  },
  sendBtn: {
    width:           42,
    height:          42,
    borderRadius:    21,
    backgroundColor: COLORS.accent,
    alignItems:      'center',
    justifyContent:  'center',
  },
});
