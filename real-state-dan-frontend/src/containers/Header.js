import { Popover } from '@headlessui/react';
import { Link } from 'react-router-dom';
import { Bars3Icon } from '@heroicons/react/24/outline';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { useNavigate } from 'react-router';
import { signOut, updateUserDetails } from '../store/slices/auth';
import { notifySuccess } from '../helpers/notification';
import { USER_ROLES } from '../app/constants';
import { httpGet } from '../api';

function Header() {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSignOut = () => {
    dispatch(signOut());
    notifySuccess('Successfully signed out');
    navigate('/');
  };

  const isAdmin = USER_ROLES.ADMIN === user.role;
  const isOwner = USER_ROLES.OWNER === user.role;
  const isCustomer = USER_ROLES.CUSTOMER === user.role;

  useEffect(() => {
    if (user.userId) {
      httpGet({
        url: `/users/${user.userId}`,
      }).then((r) => {
        dispatch(updateUserDetails(r.data));
      });
    }
  }, []);

  return (
    <Popover className="relative bg-white">
      <div className="mx-auto max-w-7xl px-6">
        <div className="flex items-center justify-between border-b-2 border-gray-100 py-6 md:justify-start md:space-x-10">
          <div className="flex justify-start lg:w-0 lg:flex-1">
            <Link to="/">
              <span className="sr-only">Your Company</span>
              <img
                className="h-8 w-auto sm:h-10"
                src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
                alt=""
              />
            </Link>
          </div>
          <div className="-my-2 -mr-2 md:hidden">
            <Popover.Button className="inline-flex items-center justify-center rounded-md bg-white p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500">
              <span className="sr-only">Open menu</span>
              <Bars3Icon className="h-6 w-6" aria-hidden="true" />
            </Popover.Button>
          </div>
          <Popover.Group as="nav" className="hidden space-x-10 md:flex">
            {!isOwner && !isAdmin && (
              <Link
                to="/properties"
                className="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Properties
              </Link>
            )}
            {isCustomer && (
              <Link
                to="/properties/saved"
                className="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Saved Properties
              </Link>
            )}
            {(isCustomer || isOwner) && (
              <Link
                to="/inquiries"
                className="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Inquiries
              </Link>
            )}
            {(isCustomer || isOwner) && (
              <Link
                to="/offers"
                className="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                My Offers
              </Link>
            )}
            {isOwner && (
              <Link
                to="/properties/new"
                className="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Add Property
              </Link>
            )}
            {isOwner && (
              <Link
                to="/properties/owner"
                className="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                My Properties
              </Link>
            )}
            {isAdmin && (
              <>
                <Link
                  to="/admin/users"
                  className="text-base font-medium text-gray-500 hover:text-gray-900"
                >
                  Users
                </Link>
                <Link
                  to="/admin/requiring-approvals"
                  className="text-base font-medium text-gray-500 hover:text-gray-900"
                >
                  Requiring Approvals
                </Link>
              </>
            )}
          </Popover.Group>
          {!user.email ? (
            <div className="hidden items-center justify-end md:flex md:flex-1 lg:w-0">
              <Link
                to="/sign-in"
                className="whitespace-nowrap text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Sign in
              </Link>
              <Link
                to="/sign-up"
                className="ml-8 inline-flex items-center justify-center whitespace-nowrap rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-base font-medium text-white shadow-sm hover:bg-indigo-700"
              >
                Sign up
              </Link>
            </div>
          ) : (
            <div className="hidden items-center justify-end md:flex md:flex-1 lg:w-0">
              {user.email}({user.userId})
              <button
                onClick={handleSignOut}
                type="button"
                className="whitespace-nowrap text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Sign out
              </button>
            </div>
          )}
        </div>
      </div>
    </Popover>
  );
}

export default Header;
