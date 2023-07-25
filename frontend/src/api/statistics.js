import axios from 'axios';

export async function searchTotalCveByEachYear() {
  return axios.get('/api/v1/statistic/years');
}
