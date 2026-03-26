import { createStackNavigator } from '@react-navigation/stack';
import LeadsApp from './LeadsApp';

const Stack = createStackNavigator();

export default function AppNavigator() {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name="Leads"
        component={LeadsApp}
        options={{ title: 'Leads App' }}
      />
    </Stack.Navigator>
  );
}