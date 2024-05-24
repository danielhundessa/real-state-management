import { React, useState } from 'react';
import { CurrentUser } from '../context/CurrentUser';
import Header from './Header';
import PageRoutes from './PageRoutes';

function Dashboard() {
  const [curentUser] = useState({});

  return (
    <div>
      <CurrentUser.Provider value={curentUser}>
        <div>
          <Header />
        </div>
        <div>
          <PageRoutes />
        </div>
      </CurrentUser.Provider>
    </div>
  );
}

export default Dashboard;
