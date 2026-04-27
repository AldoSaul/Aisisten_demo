import React, { useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import LoginScreen from '../screens/LoginScreen';
import InboxScreen from '../screens/InboxScreen';
import ChatScreen  from '../screens/ChatScreen';
import { useApp } from '../context/AppContext';
import { login as apiLogin } from '../api/auth';
import { saveSession, clearSession } from '../auth/session';

const Stack = createStackNavigator();

export default function AppNavigator() {
  const [authError, setAuthError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { isAuthenticated, setIsAuthenticated, selectTenant } = useApp();

  const handleLogin = async ({ email, password }) => {
    setAuthError('');
    setIsSubmitting(true);
    try {
      const response = await apiLogin(email, password);
      saveSession({
        token: response.accessToken,
        tokenType: response.tokenType ?? 'Bearer',
        user: response.user,
      });
      if (response?.user?.tenantId) {
        selectTenant(response.user.tenantId);
      }
      setIsAuthenticated(true);
    } catch (error) {
      clearSession();
      setIsAuthenticated(false);
      setAuthError('Credenciales inválidas o sesión no autorizada.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false, cardStyle: { flex: 1 } }}>
        {!isAuthenticated ? (
          <Stack.Screen name="Login">
            {props => (
              <LoginScreen
                {...props}
                onLogin={handleLogin}
                authError={authError}
                isSubmitting={isSubmitting}
              />
            )}
          </Stack.Screen>
        ) : (
          <>
            <Stack.Screen name="Inbox" component={InboxScreen} />
            <Stack.Screen name="Chat"  component={ChatScreen}  />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
