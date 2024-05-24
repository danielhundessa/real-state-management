/* eslint-disable operator-linebreak */
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import { httpGet } from '../api';
import { PROPERTY_STATUS, USER_ROLES } from '../app/constants';
import Property from './Property';

function Properties() {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const isOwner = USER_ROLES.OWNER === user.role;
  const isAdmin = USER_ROLES.ADMIN === user.role;

  const navigate = useNavigate();

  if (isAdmin) navigate('/admin/users');
  if (isOwner) navigate('/properties/owner');

  const [formData, setFormData] = useState({
    listing_type: '',
    property_type: '',
    min_price: '',
    max_price: '',
  });

  const [properties, setProperties] = useState([]);

  const fetchProperties = async (params = {}) => {
    const res = await httpGet({
      url: `/properties?property_status=${PROPERTY_STATUS.AVAILABLE}`,
      params,
    });
    setProperties(res.data);
  };

  useEffect(() => {
    fetchProperties();
  }, []);

  const renderProperties = () => {
    if (!properties.length) return <div>No properties</div>;

    return properties.map((p) => <Property key={p.id} p={p} />);
  };

  const handleFilterChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleFilterSubmit = async (e) => {
    e.preventDefault();
    await fetchProperties(formData);
  };

  return (
    <div>
      <div className="bg-white">
        <div className="mx-auto max-w-2xl py-16 px-4 sm:py-24 sm:px-6 lg:max-w-7xl lg:px-8">
          <h2 className="sr-only">ps</h2>
          <form onSubmit={handleFilterSubmit} className="flex items-center">
            <div className="w-1/4 mr-10 mb-10">
              <select
                className="form-select block w-full bg-white border border-gray-400 hover:border-gray-500 px-4 py-2 pr-8 rounded-lg shadow-sm focus:outline-none focus:shadow-outline"
                name="listing_type"
                value={formData.listing_type}
                onChange={handleFilterChange}
              >
                <option value="">Listing Type</option>
                <option>SALE</option>
                <option>RENT</option>
              </select>
            </div>
            <div className="w-1/4 mr-10 mb-10">
              <select
                className="form-select block w-full bg-white border border-gray-400 hover:border-gray-500 px-4 py-2 pr-8 rounded-lg shadow-sm focus:outline-none focus:shadow-outline"
                name="property_type"
                value={formData.property_type}
                onChange={handleFilterChange}
              >
                <option value="">Property Type</option>
                <option>HOUSE</option>
                <option>APARTMENT</option>
                <option>CONDO</option>
              </select>
            </div>
            <input
              type="number"
              className="w-1/6 mr-10 mb-10 form-input block w-full bg-white border border-gray-400 hover:border-gray-500 px-4 py-2 pr-8 rounded-lg shadow-sm focus:outline-none focus:shadow-outline"
              name="min_price"
              value={formData.min_price}
              placeholder="Min Price..."
              onChange={handleFilterChange}
            />
            <input
              type="number"
              className="w-1/6 mr-10 mb-10 form-input block w-full bg-white border border-gray-400 hover:border-gray-500 px-4 py-2 pr-8 rounded-lg shadow-sm focus:outline-none focus:shadow-outline"
              name="max_price"
              value={formData.max_price}
              placeholder="Max Price..."
              onChange={handleFilterChange}
            />
            <button
              type="submit"
              className="mr-10 mb-10 bg-indigo-500 hover:bg-indigo-600 text-white font-medium py-2 px-4 rounded-lg"
            >
              Filter
            </button>
          </form>

          <div className="grid grid-cols-1 gap-y-10 gap-x-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-8">
            {renderProperties()}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Properties;
