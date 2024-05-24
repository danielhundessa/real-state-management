import { BrowserRouter } from 'react-router-dom';
import Dashboard from './containers/DashBoard';

function App() {
  return (
    <div>
      <BrowserRouter>
        <Dashboard />
      </BrowserRouter>
    </div>
  );
}

export default App;
