import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../constants/theme';

export default function LoginScreen({ onLogin }) {
  const [email,    setEmail]    = useState('');
  const [password, setPassword] = useState('');

  const canSubmit = email.trim().length > 0 && password.trim().length > 0;

  const handleLogin = () => {
    if (canSubmit) onLogin();
  };

  return (
    <SafeAreaView style={styles.container}>
      <KeyboardAvoidingView
        style={styles.inner}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      >
        {/* ── Brand ── */}
        <Text style={styles.logo}>
          Leads<Text style={styles.logoAccent}>Hub</Text>
        </Text>
        <Text style={styles.subtitle}>Inicia sesión para continuar</Text>

        {/* ── Card ── */}
        <View style={styles.card}>
          <TextInput
            style={styles.input}
            placeholder="Correo electrónico"
            placeholderTextColor={COLORS.sub}
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
            autoCapitalize="none"
            autoCorrect={false}
            returnKeyType="next"
          />
          <TextInput
            style={styles.input}
            placeholder="Contraseña"
            placeholderTextColor={COLORS.sub}
            value={password}
            onChangeText={setPassword}
            secureTextEntry
            returnKeyType="done"
            onSubmitEditing={handleLogin}
          />
          <TouchableOpacity
            style={[styles.btn, !canSubmit && styles.btnDisabled]}
            onPress={handleLogin}
            activeOpacity={0.8}
            disabled={!canSubmit}
          >
            <Text style={styles.btnText}>Entrar</Text>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex:            1,
    backgroundColor: COLORS.sidebar,
  },
  inner: {
    flex:            1,
    justifyContent:  'center',
    paddingHorizontal: 28,
  },
  logo: {
    fontSize:      32,
    fontWeight:    '800',
    color:         '#F7F5F0',
    letterSpacing: -0.8,
    textAlign:     'center',
    marginBottom:  6,
  },
  logoAccent: {
    color: COLORS.accent,
  },
  subtitle: {
    fontSize:     13,
    color:        '#8A8075',
    textAlign:    'center',
    marginBottom: 32,
  },
  card: {
    backgroundColor: '#1e1a16',
    borderRadius:    16,
    padding:         24,
    gap:             14,
  },
  input: {
    height:            48,
    backgroundColor:   '#2a2520',
    borderWidth:       1,
    borderColor:       '#3a342e',
    borderRadius:      10,
    paddingHorizontal: 16,
    fontSize:          14,
    color:             '#f0ece4',
  },
  btn: {
    height:          48,
    backgroundColor: COLORS.accent,
    borderRadius:    10,
    alignItems:      'center',
    justifyContent:  'center',
    marginTop:       4,
  },
  btnDisabled: {
    opacity: 0.45,
  },
  btnText: {
    color:      '#fff',
    fontSize:   15,
    fontWeight: '600',
  },
});
