import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { CH_COLOR } from '../constants/theme';

export default function Avatar({ initials, channel, size = 40 }) {
  return (
    <View
      style={[
        styles.circle,
        {
          width:           size,
          height:          size,
          borderRadius:    size / 2,
          backgroundColor: CH_COLOR[channel],
        },
      ]}
    >
      <Text style={[styles.text, { fontSize: size * 0.35 }]}>{initials}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  circle: {
    alignItems:     'center',
    justifyContent: 'center',
    flexShrink:     0,
  },
  text: {
    color:      '#fff',
    fontWeight: '700',
  },
});
