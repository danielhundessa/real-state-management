import axios from 'axios';
import Cookies from 'universal-cookie';
import { notifyError } from '../helpers/notification';
import { ACCESS_TOKEN } from '../app/constants';

export const API_BASE_URL = 'http://localhost:8082/api/v1';

const cookies = new Cookies();

const httpClient = axios.create({
  baseURL: API_BASE_URL,
});

httpClient.interceptors.request.use(
  (config) => {
    const token = cookies.get(ACCESS_TOKEN);

    if (token && !config.headers.Authorization) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// const { status, data, statusText } = response;

httpClient.interceptors.response.use(
  (response) => response,
  ({ response }) => {
    // on error

    const { status } = response ?? {};

    if (parseInt(status, 10) === 401) {
      notifyError('You are not authorized');
    }

    if (parseInt(status, 10) === 403) {
      notifyError("You don't  have permission to this resource!");
    }

    if (parseInt(status, 10) === 500) {
      notifyError('Server side error 500');
    }

    return Promise.reject(response);
  }
);

export const httpGet = (props) => httpClient.request({ method: 'get', ...props });

export const httpPost = (props) => httpClient.request({ method: 'post', ...props });

export const httpPut = (props) => httpClient.request({ method: 'put', ...props });

export const httpDelete = (props) => httpClient.request({ method: 'delete', ...props });
