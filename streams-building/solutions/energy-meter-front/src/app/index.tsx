import * as React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import { AppLayout } from '@app/AppLayout/AppLayout';
import '@app/app.css';
import { Dashboard } from './Dashboard/Dashboard';

const App: React.FunctionComponent = () => (
  <AppLayout>
    <Dashboard></Dashboard>
  </AppLayout>
);

export default App;
