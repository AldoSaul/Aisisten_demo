import React, { useEffect, useRef } from 'react';
import { View, Animated, StyleSheet } from 'react-native';
import { COLORS } from '../constants/theme';

export default function TypingIndicator() {
  const dot1 = useRef(new Animated.Value(0)).current;
  const dot2 = useRef(new Animated.Value(0)).current;
  const dot3 = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    const bounce = (dot, delay) =>
      Animated.loop(
        Animated.sequence([
          Animated.delay(delay),
          Animated.timing(dot, { toValue: -5, duration: 250, useNativeDriver: true }),
          Animated.timing(dot, { toValue:  0, duration: 250, useNativeDriver: true }),
          Animated.delay(600),
        ])
      );

    const a1 = bounce(dot1,   0);
    const a2 = bounce(dot2, 160);
    const a3 = bounce(dot3, 320);

    a1.start();
    a2.start();
    a3.start();

    return () => {
      a1.stop();
      a2.stop();
      a3.stop();
    };
  }, [dot1, dot2, dot3]);

  return (
    <View style={styles.row}>
      <View style={styles.bubble}>
        {[dot1, dot2, dot3].map((dot, i) => (
          <Animated.View
            key={i}
            style={[styles.dot, { transform: [{ translateY: dot }] }]}
          />
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  row: {
    paddingVertical: 4,
    alignItems:      'flex-start',
  },
  bubble: {
    flexDirection:         'row',
    alignItems:            'center',
    gap:                   4,
    backgroundColor:       COLORS.surface,
    borderRadius:          14,
    borderBottomLeftRadius: 3,
    borderWidth:           1,
    borderColor:           COLORS.border,
    paddingHorizontal:     14,
    paddingVertical:       10,
  },
  dot: {
    width:           6,
    height:          6,
    borderRadius:    3,
    backgroundColor: COLORS.sub,
  },
});
