import axios from 'axios';

export async function reIssueToken() {
  return axios.post('/api/reissue/token', null, {
    withCredentials: true
  });
}
