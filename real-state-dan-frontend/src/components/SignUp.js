import { useState } from 'react';
import { useNavigate } from 'react-router';
import { notifyError, notifySuccess } from '../helpers/notification';
import { httpPost } from '../api';

function SignUp() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    isOwner: false,
  });
  const navigate = useNavigate();

  const handleSignupInputChange = (e) => {
    let { value } = e.target;
    if (e.target.name === 'isOwner') {
      value = e.target.checked;
    }
    setFormData({
      ...formData,
      [e.target.name]: value,
    });
  };

  const handleSignupSubmit = async (e) => {
    e.preventDefault();
    try {
      await httpPost({
        url: '/authenticate/register',
        data: formData,
      });
      notifySuccess('Successfully signed up');
      // replace: true means Browser navigation history will be cleared
      navigate('/sign-in', { replace: true });
    } catch (err) {
      notifyError(`Error while signing up ${err}`);
    }
  };

  return (
    <div className="flex items-center justify-center py-16">
      <div className="md:grid-cols-3 md:gap-6">
        <div className="mt-5 md:col-span-2 md:mt-0">
          <form onSubmit={handleSignupSubmit}>
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className="bg-white px-4 py-5 sm:p-6">
                <div className="grid grid-cols-6 gap-6">
                  <div className="col-span-6 sm:col-span-3">
                    Email address
                    <input
                      type="text"
                      name="email"
                      onChange={handleSignupInputChange}
                      value={formData.email}
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    />
                  </div>

                  <div className="col-span-6 sm:col-span-3">
                    Password
                    <input
                      type="password"
                      name="password"
                      onChange={handleSignupInputChange}
                      value={formData.password}
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                    />
                  </div>

                  <div className="col-span-6">
                    {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
                    <label>
                      <input
                        id="isOwner"
                        type="checkbox"
                        name="isOwner"
                        onChange={handleSignupInputChange}
                        checked={formData.isOwner}
                      />{' '}
                      I am an owner
                    </label>
                  </div>
                </div>
              </div>
              <div className="bg-gray-50 px-4 py-3 text-right sm:px-6">
                <button
                  type="submit"
                  className="inline-flex justify-center rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                >
                  Sign up
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignUp;
