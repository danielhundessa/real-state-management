import { LockClosedIcon } from '@heroicons/react/20/solid';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useDispatch } from 'react-redux';
import Cookies from 'universal-cookie';
import jwtDecode from 'jwt-decode';
import { notifyError, notifySuccess } from '../helpers/notification';
import { signIn } from '../store/slices/auth';
import { httpPost } from '../api';
import { ACCESS_TOKEN } from '../app/constants';

function SignIn() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleSuccessfulLogin = (data) => {
    const cookies = new Cookies();
    const { accessToken } = data || {};
    const jwt = jwtDecode(accessToken);

    cookies.set(ACCESS_TOKEN, accessToken, {
      path: '/',
      expires: new Date(jwt.exp * 1000),
    });

    dispatch(signIn({ ...jwt, email: jwt.sub }));
    notifySuccess('Successfully signed in');
    navigate('/', { replace: true });
  };

  const handleSignInSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { email, password };
      const res = await httpPost({
        url: '/authenticate/login',
        data: payload,
      });

      if (!res.data.accessToken) {
        throw new Error('No token returned from backend');
      }

      handleSuccessfulLogin(res.data);
    } catch (err) {
      notifyError('Failed to sign-in to system');
      console.log(err);
    }
  };

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  return (
    <div className="flex min-h-full items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="w-full max-w-md space-y-8">
        <div>
          <img
            className="mx-auto h-12 w-auto"
            src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
            alt="Your Company"
          />
          <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
            Sign in to your account
          </h2>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSignInSubmit}>
          <input type="hidden" name="remember" defaultValue="true" />
          <div className="-space-y-px rounded-md shadow-sm">
            <div>
              Email address
              <input
                name="email"
                type="email"
                value={email}
                onChange={handleEmailChange}
                required
                className="relative block w-full appearance-none rounded-none rounded-t-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                placeholder="Email address"
              />
            </div>
            <div className="pt-4">
              Password
              <input
                name="password"
                type="password"
                value={password}
                onChange={handlePasswordChange}
                required
                className="relative block w-full appearance-none rounded-none rounded-b-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                placeholder="Password"
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              className="group relative flex w-full justify-center rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
            >
              <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                <LockClosedIcon
                  className="h-5 w-5 text-indigo-500 group-hover:text-indigo-400"
                  aria-hidden="true"
                />
              </span>
              Sign in
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SignIn;
