import axios from 'axios';

export async function userJoin(object) {
  return axios.post('/api/v1/auth/register', object);
}

export async function userLogin(object) {
  return axios.post('/api/v1/auth/login', object);
}

export async function checkDuplicateUsername(object) {
  return axios.post('/api/v1/auth/login', object);
}
