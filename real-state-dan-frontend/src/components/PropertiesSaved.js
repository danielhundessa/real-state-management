import { useSelector } from 'react-redux';
import Property from './Property';

function PropertiesSaved() {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const likedProperties = user.details?.properties || [];

  const renderProperties = () => likedProperties.map((p) => <Property key={p.id} p={p} />);

  return (
    <div>
      <div className="bg-white">
        <div className="mx-auto max-w-2xl py-16 px-4 sm:py-24 sm:px-6 lg:max-w-7xl lg:px-8">
          <h2 className="sr-only">ps</h2>
          <div className="grid grid-cols-1 gap-y-10 gap-x-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-8">
            {renderProperties()}
          </div>
        </div>
      </div>
    </div>
  );
}

export default PropertiesSaved;
