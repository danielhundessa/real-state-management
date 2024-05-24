import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { HeartIcon } from '@heroicons/react/24/outline';
import { USER_ROLES } from '../app/constants';
import { httpDelete, httpPost } from '../api';
import { updateUserDetails } from '../store/slices/auth';

function Property({ p }) {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const isCustomer = USER_ROLES.CUSTOMER === user.role;
  const likedProperties = user.details?.properties || [];
  // const location = useLocation();
  const dispatch = useDispatch();

  const heartClick = async (type) => {
    if (type === 'like') {
      await httpPost({
        url: `/properties/${p.id}/favourite`,
      });
      dispatch(
        updateUserDetails({
          properties: [...likedProperties, p],
        })
      );
    } else {
      await httpDelete({
        url: `/properties/${p.id}/favourite`,
      });
      dispatch(
        updateUserDetails({
          properties: likedProperties.filter((lp) => lp.id !== p.id),
        })
      );
    }
  };

  const renderHeart = () => {
    if (!isCustomer) return null;

    const isLiked = likedProperties.findIndex((lp) => lp.id === p.id) > -1;
    return isLiked ? (
      <HeartIcon
        className="h-6 w-6 text-red-500 hover:text-grey-500 cursor-pointer"
        onClick={() => heartClick('dislike')}
      />
    ) : (
      <HeartIcon
        className="h-6 w-6 text-gray-500 hover:text-red-500 cursor-pointer"
        onClick={() => heartClick('like')}
      />
    );
  };

  return (
    <div className="relative">
      <div className="absolute z-10 right-0 w-fit text-center px-2 py-1 bg-indigo-500 text-white">
        {p.propertyStatus}
      </div>
      <div className="absolute z-10 w-fit right-0 text-center bg-indigo-500 text-white px-2 py-1 top-8">
        {p.listingType}
      </div>
      <Link key={p.id} to={`/properties/${p.id}`} className="group">
        <div className="relative aspect-w-1 aspect-h-1 w-full overflow-hidden rounded-lg bg-gray-200 xl:aspect-w-7 xl:aspect-h-8">
          <img
            src={p.image}
            alt={p.title}
            className="h-full w-full object-cover object-center group-hover:opacity-75"
          />
        </div>
        <h3 className="mt-4 text-sm text-gray-700">Title: {p.title}</h3>
        <h3 className="mt-4 text-sm text-gray-700">Description: {p.description}</h3>
        <h3 className="mt-4 text-sm text-gray-700">
          Location: {p.address} {p.city} {p.state}, {p.zipCode}
        </h3>
        <p className="mt-1 text-lg font-medium text-gray-900">${p.price}</p>
      </Link>
      {renderHeart()}
    </div>
  );
}

export default Property;
