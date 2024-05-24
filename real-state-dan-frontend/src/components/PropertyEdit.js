/* eslint-disable jsx-a11y/label-has-associated-control */
import { useState, useEffect } from 'react';
import { useParams } from 'react-router';
import { httpGet } from '../api';
import { notifyError } from '../helpers/notification';
import PropertyEditForm from './PropertyEditForm';

function PropertyEdit() {
  const params = useParams();

  const propertyId = params.id;
  const [property, setProperty] = useState();

  const fetchProperty = async () => {
    try {
      const res = await httpGet({
        url: `/properties/${propertyId}`,
      });
      if (res.data) setProperty(res.data);
      console.log(property);
    } catch (err) {
      notifyError('Failed to fetch from system');
      console.log(err);
    }
  };

  useEffect(() => {
    fetchProperty();
  }, [propertyId]);

  return (
    <div>
      <div className="px-4 py-5 sm:px-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900">Property Edit</h3>
        <PropertyEditForm property={property} />
      </div>
    </div>
  );
}

export default PropertyEdit;
