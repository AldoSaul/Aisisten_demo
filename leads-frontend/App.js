import 'react-native-url-polyfill/auto';
import 'react-native-gesture-handler';
import React from 'react';
import { StatusBar } from 'expo-status-bar';
import { View, Text } from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { AppProvider }  from './src/context/AppContext';
import AppNavigator     from './src/navigation/AppNavigator';

// ── Inner app (functional, so hooks work) ────────────────────────────────────
function RootApp() {
  return (
    <SafeAreaProvider>
      <StatusBar style="dark" />
      <AppProvider>
        <AppNavigator />
      </AppProvider>
    </SafeAreaProvider>
  );
}

// ── Error boundary wrapping the whole tree ───────────────────────────────────
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error('Error Boundary caught:', error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <View style={{ flex: 1, padding: 20, justifyContent: 'center', alignItems: 'center', backgroundColor: '#fff' }}>
          <Text style={{ fontSize: 18, fontWeight: '700', marginBottom: 10 }}>Oops! Algo salió mal</Text>
          <Text style={{ fontSize: 14, color: '#666' }}>{this.state.error?.toString()}</Text>
        </View>
      );
    }
    return <RootApp />;
  }
}

export default ErrorBoundary;
